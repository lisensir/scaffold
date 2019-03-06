package org.scaffold.commons.security;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

    private static final String RSA = "RSA";// 非对称加密密钥算法
    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI6dGvkKSHB6Q3TE6TKGFR4Nyt\n" +
            "6XH3gc7/LAzvW0aDNGZjkoA7jrMTBd/T0N/R4miBK7XNMI+4Z/gd8OgS0wShPwyq\n" +
            "Fwv8Q54goPr6dAXAQifzwK+eOs+Avu9rrVfT31i8wJeIzpk1aySoYB40ozasTdXg\n" +
            "Q2AHZH0AqU/Rne5GuQIDAQAB\n" +
            "-----END PUBLIC KEY-----";//默认公钥
    public static final String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXQIBAAKBgQDI6dGvkKSHB6Q3TE6TKGFR4Nyt6XH3gc7/LAzvW0aDNGZjkoA7\n" +
            "jrMTBd/T0N/R4miBK7XNMI+4Z/gd8OgS0wShPwyqFwv8Q54goPr6dAXAQifzwK+e\n" +
            "Os+Avu9rrVfT31i8wJeIzpk1aySoYB40ozasTdXgQ2AHZH0AqU/Rne5GuQIDAQAB\n" +
            "AoGATkWphzhWoLR9aX207ufAYmG0F5zm5YIQ3qCOuYFZyyvS59/fTVSetz6GoMKz\n" +
            "L9WA2THqNfJBS5pVs3RzNUI4fthhl06Q4Nmm+HGGQ7hvj05+v49E9deqtHMaTMcG\n" +
            "j99dMt2D18k/Gw8va5/Czoj6+yGQIaNUTSmOefGeZZhhWzECQQDuKpwoDhOunDut\n" +
            "H7jx6frdm4wAR2ykWFs5XBWtyWp1dVfc/uCO5wQgpUpxmUbiQNJISyfKMvscr4ow\n" +
            "Qs336343AkEA1/UbsENrQ42GN0nbczx14Yo4C3Ds/9Hdyw12JoGg+2JPFYB1vmdZ\n" +
            "xo/xXx0wsZWYSBYEwumVd19gF6szyeBqjwJBAJlK57OIS/bJLEfj8SAT35ofcd8E\n" +
            "GdM145FpybJPv6vWWTUu/846txdkDCRN6afa3P4XEYc9hQ8TlTg252c5NcUCQQC6\n" +
            "0xj5C9onvq919TccIhn43BJQE4l0ZqJxn1uvREV4NDwmZdN2vQI1fSFbH22Ys8nh\n" +
            "0uqblGfxtVj0IO/UnYiRAkAu4helSN55HCZ8pcDITJMgENKFQYW4Rgo9IdYAX2tD\n" +
            "1zP5W2FbdJACExzwYfCunSmDgAKQIg/rNWziqc6raqer\n" +
            "-----END RSA PRIVATE KEY-----";//默认私钥
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) throws Exception {
        // 得到私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        //RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keyPublic);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    /**
     *  公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return byte[]   解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        try
        {
            // 得到公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            // 数据解密
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPublic);

            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("No algorithm is available");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("Illegal public key");
        }
        catch (NullPointerException e)
        {
            throw new Exception("public key is empty");
        }
    }
    /**
     * 使用私钥进行解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
        try
        {
            // 得到私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory kf = KeyFactory.getInstance(RSA);
            PrivateKey keyPrivate = kf.generatePrivate(keySpec);
            // 解密数据
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keyPrivate);
            int inputLen = encrypted.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encrypted, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encrypted, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Exception("No algorithm is available");
        }
        catch (InvalidKeySpecException e)
        {
            throw new Exception("Illegal private key");
        }
        catch (NullPointerException e)
        {
            throw new Exception("public key is empty");
        }
    }

}
