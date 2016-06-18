package com.nuctech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;
public class SqlRemoveUtils {

	private static int indexOfByRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		int pos = -1;
		while(m.find()){
			pos = m.start();
		}
		return pos;
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 * 
	 * @param sql
	 * @return
	 */
	public static String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = indexOfByRegex(sql.toLowerCase(), "\\sfrom\\s");
		beginPos = getFirstSelcectMactchFrom(sql.toLowerCase());
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	private static int getFirstSelcectMactchFrom(String input) {
		List<Integer> selectPos = new ArrayList<>();
		List<Integer> fromPos = new ArrayList<>();
		String regex = "select";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);		
		while(m.find()){
			selectPos.add(m.start());
		}
		regex = "\\sfrom\\s";
		p = Pattern.compile(regex);
		m = p.matcher(input);		
		while(m.find()){
			fromPos.add(m.start());
		}
		Assert.isTrue(selectPos.size()==fromPos.size(), " sql : " + input + " 'select' must match 'from'");
		if(selectPos.size()==0){
			return -1;
		}
		if(selectPos.size()==1){
			return fromPos.get(0);
		}
		Integer firstSelectPos = selectPos.get(0);
		Map<Integer, Integer> map = new HashMap<>();
		for(int i=0;i<fromPos.size();i++){
			Integer fpos = fromPos.get(i);
			if(fromPos.size()==1){
				Integer spos = selectPos.get(i);
				map.put(spos, fpos);
				selectPos.remove(spos);
				fromPos.remove(fpos);
				i--;
				break;
			}
			for(int j=0;j<selectPos.size()-1;j++){
				Integer spos = selectPos.get(j);				
				Integer sposnext = selectPos.get(j+1);
				if(fpos.intValue() > spos.intValue() && fpos.intValue() < sposnext.intValue()){
					map.put(spos, fpos);
					selectPos.remove(spos);
					i--;
					fromPos.remove(fpos);
					break;
				}else if(j+1==selectPos.size()-1  && fpos.intValue()>sposnext.intValue()){
					map.put(sposnext, fpos);
					selectPos.remove(sposnext);
					i--;
					fromPos.remove(fpos);
					break;
				}
			}
		}
		return map.get(firstSelectPos).intValue();
	}

	/**
	 * 去除orderby 子句
	 * 
	 * @param sql
	 * @return
	 */
	public static String removeOrders(String sql) {
		Assert.hasText(sql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static String removeFetchKeyword(String sql) {
		return sql.replaceAll("(?i)fetch", "");
	}

	public static String removeXsqlBuilderOrders(String string) {
		Assert.hasText(string);
		Pattern p = Pattern.compile("/~.*order\\s*by[\\w|\\W|\\s|\\S]*~/", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return removeOrders(sb.toString());
	}

}
