package com.nuctech.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

    static Logger log = Logger.getLogger(Config.class);

    public static final String CONFIG_FILE = "/config.xml";

    public static String projectName;
    public static Map<String, String> deviceTypeMap;

    public static String ftpHostName;
    public static String ftpUserName;
    public static String ftpPassword;
    public static String ftpPort;
    public static String currentFilePath;
    public static String toallowNum;
    public static Map<String, String> uploadFileMap;
    public static String isManagerSetPicConclusion;
    public static String getDEINum;
    public static String ediGetDataTime;
    public static String logfile = "D:/nuctech";
    public static String comprefile;
    public static String delayCompress;

    public static String start;
    public static String end;
    public static String first;
    public static String sencod;

    static {

        Properties properties = getConfigProperties();

        ftpHostName = properties.getProperty("ftp.HostName");
        ftpUserName = properties.getProperty("ftp.UserName");
        ftpPassword = properties.getProperty("ftp.Password");
        ftpPort = properties.getProperty("ftp.Port");
        currentFilePath = properties.getProperty("ftp.currentFilePath");
        toallowNum = properties.getProperty("ToallowNum");
        isManagerSetPicConclusion = properties.getProperty("isManagerSetPicConclusion");
        getDEINum = properties.getProperty("getDEINum");
        ediGetDataTime = properties.getProperty("ediGetDataTime");
        projectName = properties.getProperty("projectName");
        comprefile = properties.getProperty("comprefile");
        delayCompress = properties.getProperty("delayCompress");

        start = properties.getProperty("start");
        end = properties.getProperty("end");
        first = properties.getProperty("first");
        sencod = properties.getProperty("sencod");
    }

    private static Properties configProperties;

    @SuppressWarnings("rawtypes")
    public static Properties getConfigProperties() {

        InputStream filein = null;

        log.info("Start read site configuration information!!!");
        try {
            configProperties = new Properties();
            deviceTypeMap = new HashMap<String, String>();
            uploadFileMap = new HashMap<String, String>();
            File file = new File(FilePath.getAppPath(Config.class) + CONFIG_FILE);
            filein = new FileInputStream(file);
            configProperties.loadFromXML(filein);
            Enumeration pro = configProperties.propertyNames();

            for (Enumeration e = pro; e.hasMoreElements();) {
                String proName = (String) e.nextElement();
                String proValue = configProperties.getProperty(proName);

                if (proName.startsWith("DEVICEID")) {
                    String[] values = proValue.split(",");
                    deviceTypeMap.put(values[0], values[1]);
                }

                if (proName.startsWith("FTP.UPLOAD.TYPE")) {
                    uploadFileMap.put(proValue, proValue);
                }

            }

            filein.close();
        } catch (InvalidPropertiesFormatException e) {
            log.warn("config.xml format error", e);
        } catch (IOException e) {
            log.warn("IO Error", e);
        } finally {

            try {
                if (filein != null) {

                    filein.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return configProperties;
    }

}
