package org.scaffold.commons.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Base64;

public class TestRSA {

    private String MSG = "加密测试贴吧家春秋@小裤裤￥#@！~~ ^^ --- RT蝶纷飞";

    @Before
    public void setUp(){
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
    }

    @Test
    public void testEncryptByPrivateKey() throws Exception {
        String Cipher = encrypt();
        System.out.println(Cipher);
        Assert.assertNotNull(Cipher);
    }

    @Test
    public void testDecryptByPublicKey() throws Exception {
        String Cipher = encrypt();
        String pubkey = RSA.PUBLIC_KEY;
        pubkey = pubkey.replace("-----BEGIN PUBLIC KEY-----", "");
        pubkey = pubkey.replace("-----END PUBLIC KEY-----", "");
        pubkey = pubkey.replace("\n", "");
        pubkey = pubkey.replace("\r", "");
        byte[] pubkey_byte = Base64.getDecoder().decode(pubkey);
        byte[] decode = Base64.getDecoder().decode(Cipher);
        byte[] conDecode = RSA.decryptByPublicKey(decode, pubkey_byte);
        String str = new String(conDecode);
        System.out.println(str);
        Assert.assertEquals(str, MSG);
    }

    private String encrypt() throws Exception {
        String pkey = RSA.PRIVATE_KEY;
        pkey = pkey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
        pkey = pkey.replace("-----END RSA PRIVATE KEY-----", "");
        pkey = pkey.replace("\n", "");
        pkey = pkey.replace("\r", "");
        byte[] private_key = Base64.getDecoder().decode(pkey);
        byte[] CipherText = RSA.encryptByPrivateKey(MSG.getBytes(), private_key);
        //String Cipher = new BASE64Encoder().encode(CipherText);// base64加密
        String Cipher = Base64.getEncoder().encodeToString(CipherText);
        return Cipher;
    }

}
