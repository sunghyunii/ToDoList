package com.sunghyun.todoapp;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class VapidKeyGenerator {
    public static void main(String[] args) throws Exception{
         // EC 키쌍(Elliptic Curve) 생성
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPair = keyGen.generateKeyPair();

        // 공개키, 비공개키 base64 인코딩
        String publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(
                keyPair.getPublic().getEncoded());
        String privateKey = Base64.getUrlEncoder().withoutPadding().encodeToString(
                keyPair.getPrivate().getEncoded());

        System.out.println("public key: \n" + publicKey);
        System.out.println("private key: \n" + privateKey);
    }
}
