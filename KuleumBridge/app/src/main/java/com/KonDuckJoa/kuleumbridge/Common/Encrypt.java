package com.KonDuckJoa.kuleumbridge.Common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    public static String alg = "AES/CBC/PKCS5Padding";
    private final String iv; // 16byte

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Encrypt(String key)
    {
        iv = key.substring(0, 16);
    }

    // 평문을 받고 암호화하는 과정
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encrypt(String text) throws Exception
    {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 암호문을 받고 복호화하는 과정
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String cipherText) throws Exception
    {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
