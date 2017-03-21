package com.nuctech.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Encrypter {

    public static final class Algorithms {

        public static final String MD5 = "MD5";
        public static final String SHA1 = "SHA-1";
    }

    Logger log = Logger.getLogger(this.getClass());
    private static Map<String, Encrypter> multiton = new HashMap<String, Encrypter>();

    public static Encrypter getInstance(String algorithm) {
        Encrypter encrypter = multiton.get(algorithm);
        if (encrypter == null) {
            encrypter = new Encrypter(algorithm);
            multiton.put(algorithm, encrypter);
        }
        return encrypter;
    }

    private String algorithm;

    private Encrypter(String algorithm) {
        this.algorithm = algorithm;
    }

    public String encrypt(String source) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest alga = MessageDigest.getInstance(algorithm);
            alga.update(source.getBytes("UTF-8"));
            byte[] digesta = alga.digest();
            for (byte b : digesta) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            log.fatal("Used unsuppoorted encryption algorithm", e);
        } catch (UnsupportedEncodingException e) {
            log.fatal("User an unsuppoorted character set", e);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + algorithm + "]";
    }

}
