package com.nuctech.ftp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.nuctech.util.NuctechUtil;

/**
 * FTP连接工具类
 * 
 * @author nuctech
 *
 */
public class ConnectFTP {

    private FTPClient ftpClient;

    private static final int defaultTimeout = 30000;
    private static final int soTimeout = 30000;
    private static final int dataTimeout = 10000;

    Logger log = Logger.getLogger(this.getClass());

    /**
     * FTP主机名
     */
    private String hostName;
    /**
     * FTP端口
     */
    private int port;
    /**
     * FTP登录用户名
     */
    private String username;
    /**
     * FTP登录密码
     */
    private String password;
    /**
     * GBK编码
     */
    private static String CHARSET_GBK = "GBK";
    /**
     * UTF-8编码
     */
    private static String CHARSET_UTF8 = "UTF-8";
    /**
     * ISO-8859-1编码，FTP协议里面，规定文件名编码为ISO-8859-1
     */
    private static String CHARSET_ISO88591 = "ISO-8859-1";
    /**
     * FTP传输使用的编码
     */
    private String fileEncoding;

    public ConnectFTP(String hostName, int port, String username, String password) {
        super();
        this.hostName = hostName;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 登录FTP
     * 
     * @param mode
     * @return
     * @throws Exception
     */
    public boolean connect(String mode) throws Exception {
        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.connect(hostName, port);
        ftpClient.setSoTimeout(soTimeout);
        ftpClient.getReplyCode();
        ftpClient.login(username, password);
        ftpClient.changeWorkingDirectory("/");
        ftpClient.setDataTimeout(dataTimeout);

        if (mode.equalsIgnoreCase("passive")) {
            ftpClient.enterLocalPassiveMode();
        } else {
            ftpClient.enterLocalActiveMode();
        }

        // ftpClient.configure(new
        // FTPClientConfig("org.apache.commons.net.ftp.UnixFTPEntryParser"));
        return testLink();
    }

    /**
     * 遍历FTP文件
     * 
     * @return
     */
    private boolean testLink() {
        long t1 = System.currentTimeMillis();
        try {
            log.info("List file length:" + ftpClient.listFiles().length);
        } catch (IOException e) {
            long t2 = System.currentTimeMillis();
            long t = (t2 - t1) / 1000;
            try {
                ftpClient.disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 注销FTP并断开连接
     * 
     * @throws IOException
     */
    public void closeAll() throws IOException {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                ftpClient.disconnect();
                ftpClient = null;
            }
        }
        ftpClient = null;
    }

    /**
     * FTP上传
     * 
     * @param remote
     *        FTP目标路径
     * @param local
     *        源文件路径
     * @return
     * @throws IOException
     */
    public boolean upload(String remote, String local) throws IOException {
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        fileEncoding = CHARSET_GBK;
        // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
        if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
            fileEncoding = CHARSET_UTF8;
        }
        ftpClient.setControlEncoding(fileEncoding);
        // 对远程目录的处理
        remote = remote.replaceAll("\\\\", "/");
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            if (NuctechUtil.isNull(remoteFileName)) {
                String temp = local.replaceAll("\\\\", "/");
                remoteFileName = temp.substring(temp.lastIndexOf("/") + 1);
            }
            // 创建服务器远程目录结构，创建失败直接返回
            if (!createDirecroty(remote, ftpClient)) {
                return false;
            }
        }
        log.info("Remote file name:" + remoteFileName);

        // 检查远程是否存在文件
        FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes(fileEncoding), CHARSET_ISO88591));
        log.info("Is there a remote file:" + (files.length == 1));
        boolean result = false;
        if (files.length == 1) {
            // 断点续传
            /*
             * long remoteSize = files[0].getSize(); File f = new File(local);
             * long localSize = f.length(); if(remoteSize==localSize){ return
             * UploadStatus.File_Exits; }else if(remoteSize > localSize){ return
             * UploadStatus.Remote_Bigger_Local; }
             * //尝试移动文件内读取指针,实现断点续传 result = uploadFile(remoteFileName, f,
             * ftpClient, remoteSize);
             * //如果断点续传没有成功，则删除服务器上文件，重新上传 if(result ==
             * UploadStatus.Upload_From_Break_Failed){
             * if(!ftpClient.deleteFile(remoteFileName)){ return
             * UploadStatus.Delete_Remote_Faild; } result =
             * uploadFile(remoteFileName, f, ftpClient, 0); }
             */

            // 直接覆盖文件
            if (!ftpClient.deleteFile(remoteFileName)) {
                return false;
            }
            result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
        } else {
            log.info("Remote file name:" + remoteFileName);
            result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
        }
        ftpClient.logout();
        ftpClient.disconnect();
        ftpClient = null;
        return result;
    }

    /**
     * 断开与远程服务器的连接
     * 
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    /**
     * 递归创建远程服务器目录
     * 
     * @param remote
     *        远程服务器文件绝对路径
     * @param ftpClient
     *        FTPClient对象
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean createDirecroty(String remote, FTPClient ftpClient) throws IOException {
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if (directory.equalsIgnoreCase("/")) {
            return true;
        }
        boolean status = false;
        if (!directory.equalsIgnoreCase("/")
                && !ftpClient.changeWorkingDirectory(new String(directory.getBytes(fileEncoding), CHARSET_ISO88591))) {
            // 如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes(fileEncoding), CHARSET_ISO88591);
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        status = ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        log.info("Create directory failed");
                        return false;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/", start);

                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        } else {
            status = true;
        }
        return status;
    }

    /**
     * 上传文件到服务器,新上传和断点续传
     * 
     * @param remoteFile
     *        远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile
     *        本地文件File句柄，绝对路径
     * @param processStep
     *        需要显示的处理进度步进值
     * @param ftpClient
     *        FTPClient引用
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String remoteFile, File localFile, FTPClient ftpClient, long remoteSize)
            throws IOException {
        // 显示进度的上传
        long step = localFile.length();
        long process = 0;
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");
        String string = new String(remoteFile.getBytes(CHARSET_UTF8), CHARSET_ISO88591);
        OutputStream out = ftpClient.appendFileStream(string);
        // 断点续传
        if (remoteSize > 0) {
            ftpClient.setRestartOffset(remoteSize);
            process = remoteSize / step;
            raf.seek(remoteSize);
            localreadbytes = remoteSize;
        }
        byte[] bytes = new byte[1024];
        int c;
        log.info("localFile :" + localFile + " localreadbytes:" + localreadbytes + " step:" + step + " process:"
                + process);
        while ((c = raf.read(bytes)) != -1) {
            out.write(bytes, 0, c);
            localreadbytes += c;
            if (localreadbytes / step != process) {
                process = localreadbytes / step;
            }
        }
        out.flush();
        raf.close();
        out.close();
        boolean result = ftpClient.completePendingCommand();
        return result;
    }

    /**
     * 获取当前路径下的所有文件
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public FTPFile[] listFiles(String path) throws Exception {
        return ftpClient.listFiles(path);
    }
}