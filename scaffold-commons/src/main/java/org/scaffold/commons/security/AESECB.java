package org.scaffold.commons.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AESECB {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String SALT = "paichat";

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password,SALT));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception ex) {
            Logger.getLogger(AESECB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password,SALT));

            //执行操作
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESECB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


    public static SecretKeySpec getSecretKey(final String password, final String salt) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(password.getBytes());
            byte[] h1 = md.digest();
            md.reset();
            md.update(h1);
            md.update(salt.getBytes());
            return new SecretKeySpec(md.digest(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(AESECB.class.getName()).log(Level.SEVERE, null, e);
        }

        return null;
    }

    //userName=zgewallet  password=zgew&123   aespassword=zhuguang@ewallet&
    public static void main(String[] args) {
        String s = "{\"accId\":2,\"accName\":\"刘伟\",\"amount\":20.000000,\"coinType\":\"GFX\",\"lockAmount\":9.000000}";
        System.out.println("s:" + s);
        String s1 = AESECB.encrypt(s, "zhuguang@ewallet&");
        System.out.println("s1:" + s1);

        System.out.println("s2:"+AESECB.decrypt(s1, "zhuguang@ewallet&"));

        String pp = "lisen123xx";
        String pps = AESECB.encrypt(pp, "^_^zkwy@ewallet#acc$");
        System.out.println("pps 1 = " + pps);

        System.out.println("pps 2 = " + AESECB.decrypt(pps, "^_^zkwy@ewallet#acc$"));
    }

}
