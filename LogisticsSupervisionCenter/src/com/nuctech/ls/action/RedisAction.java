package com.nuctech.ls.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * Redis调试Action
 * 
 *
 */
@Namespace("/redis")
public class RedisAction extends LSBaseAction {
	private static final long serialVersionUID = -4245308424567318807L;
	@Resource
	private String redisUrl;
	@Resource
	private RedisClientTemplate redisClientTemplate;

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 */
	@Action(value = "redis", results = { @Result(name = "success", location = "/test/redis.jsp") })
	public String redis() {
		return SUCCESS;
	}

	/**
	 * 获取连接
	 * 
	 * @param ip
	 * @param port
	 * @param password
	 * @throws Exception
	 */
	protected Jedis getConnection(String ip, String port, String password) throws Exception {
		try {
			Jedis jedis = new Jedis(ip, Integer.parseInt(port));
			jedis.auth(password);
			return jedis;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format("连接Redis失败！ip:%s,port:%s,password:%s", ip, port, password));
		} finally {
		}
	}

	/**
	 * 查询
	 * 
	 * @throws IOException
	 */
	@Action(value = "list")
	public String list() throws IOException {
		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String password = request.getParameter("password");

		Jedis jedis = null;
		JSONObject json = new JSONObject();
		try {
			jedis = getConnection(ip, port, password);
			String key = request.getParameter("key");
			if (NuctechUtil.isNull(key)) {
				key = "*";
			}
			Set<String> keys = jedis.keys(key);
			json.put("keys", keys);

			for (String k : keys) {
				JSONObject obj = new JSONObject();
				Object value = null;
				String type = jedis.type(k);
				switch (type) {
				case "string":
					value = jedis.get(k);
					break;
				case "list":
					value = jedis.lrange(k, 0, Integer.MAX_VALUE);
					break;
				case "set":
					value = jedis.smembers(k);
					break;
				case "hash":
					value = jedis.hgetAll(k);
					break;
				default:
					break;
				}
				obj.put("type", type);
				obj.put("value", value);
				json.put(k, obj);
			}
		} catch (Throwable e) {
			json.put("result", false);
			json.put("message", e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 删除指定Key
	 * 
	 * @throws IOException
	 */
	@Action(value = "delKey")
	public String delKey() throws IOException {
		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String password = request.getParameter("password");

		Jedis jedis = null;
		JSONObject json = new JSONObject();
		try {
			jedis = getConnection(ip, port, password);
			String key = request.getParameter("key");
			if (NuctechUtil.isNotNull(key)) {
				jedis.del(key);
			}
			json.put("result", true);
		} catch (Throwable e) {
			json.put("result", false);
			json.put("message", e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}

	/**
	 * 清空所有Key
	 * 
	 * @throws IOException
	 */
	@Action(value = "clearKey")
	public String clearKey() throws IOException {
		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String password = request.getParameter("password");

		Jedis jedis = null;
		JSONObject json = new JSONObject();
		try {
			jedis = getConnection(ip, port, password);
			Set<String> keys = jedis.keys("*");
			for (String key : keys) {
				jedis.del(key);
			}
			json.put("result", true);
		} catch (Throwable e) {
			json.put("result", false);
			json.put("message", e.getMessage());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}

	public String getRedisUrl() {
		return redisUrl;
	}

	public void setRedisUrl(String redisUrl) {
		this.redisUrl = redisUrl;
	}

}
