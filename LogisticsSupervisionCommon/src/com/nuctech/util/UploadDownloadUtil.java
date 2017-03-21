package com.nuctech.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.nuctech.ftp.ConnectFTP;

/**
 * 文件上传下载工具类
 * 
 * @author nuctech
 *
 */
public class UploadDownloadUtil {

    static Logger logger = Logger.getLogger(UploadDownloadUtil.class);
    /**
     * 从本地上传文件到FTP目录
     * 
     * @param hostName
     *        FTP主机名
     * @param port
     *        FTP端口
     * @param username
     *        FTP登录用户名
     * @param password
     *        FTP登录密码
     * @param remotePath
     *        上传的目标路径。以/结尾时，识别为路径。以文件名结尾时，该文件名将作为目标文件名，当文件存在时将覆盖。
     * @param localPath
     *        上传的本地文件路径
     * @return
     */
    public static boolean ftpUpload(String hostName, int port, String username, String password, String remotePath,
            String localPath) {
        logger.info(String.format("hostName: %s, port: %s, username: %s, password: %s, remotePath: %s, localPath: %s",
                hostName, port, username, password, remotePath, localPath));
        boolean flag = false;
        InputStream in = null;
        OutputStream out = null;
        ConnectFTP connectFTP = new ConnectFTP(hostName, port, username, password);
        try {
            boolean uploadStatus = false;
            try {
                flag = connectFTP.connect("passive");
                if (!flag) {
                    flag = connectFTP.connect("active");
                }
                logger.info("FTP connection status:" + flag);
                if (flag) {
                    uploadStatus = connectFTP.upload(remotePath, localPath);
                }
            } catch (Exception e) {
                logger.log(Priority.FATAL, String.format("upload %s connection FTP error", localPath), e);
            } finally {
                try {
                    connectFTP.closeAll();
                } catch (Exception e) {
                    logger.log(Priority.FATAL, "Break off FTP occurs exception", e);
                }
            }
            logger.info("End upload file.upload result status:" + uploadStatus);
            flag = uploadStatus;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Priority.WARN, "Close file stream occurs exception", e);
            }
        }
        return flag;
    }

    /**
     * 获得指定FTP路径下的文件名列表
     * 
     * @param remoteUrl
     *        目标路径
     * @param localFilePath
     *        本地文件绝对路径
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<String> listFtpsFiles(String hostName, int port, String username, String password,
            String remotePath) {
        logger.info(String.format("hostName: %s, port: %s, username: %s, password: %s, remotePath: %s, localPath: %s",
                hostName, port, username, password, remotePath));
        FTPFile[] files = null;
        ConnectFTP connectFTP = new ConnectFTP(hostName, port, username, password);
        try {
            logger.info("Get file list from specified FTP path: " + remotePath);
            boolean flag = connectFTP.connect("passive");
            if (!flag) {
                flag = connectFTP.connect("active");
            }
            logger.info("FTP connection status:" + flag);
            if (flag) {
                files = connectFTP.listFiles(remotePath);
            }
        } catch (Exception e) {
            logger.log(Priority.FATAL, "Fail to connect FTP", e);
        } finally {
            try {
                connectFTP.closeAll();
            } catch (Exception e) {
                logger.log(Priority.FATAL, "Break off FTP occurs exception", e);
            }
        }
        return listNames(files);
    }

    /**
     * 从FTP文件列表获取文件名
     * 
     * @param files
     * @return
     */
    private static List<String> listNames(FTPFile[] files) {
        List<String> names = new ArrayList<String>();
        if (files != null) {
            for (FTPFile file : files) {
                if (file.isFile()) {
                    names.add(file.getName());
                }
            }
        }
        return names;
    }

    public static void main(String[] args) {
        boolean flag = UploadDownloadUtil.ftpUpload("192.168.120.134", 21, "jordan", "nuctech", "/测试目录ab/",
                "D:/updateui.xml");
    }
}