package br.com.rsdconsultoria.model;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Participant {
    private String email;
    private String publicKey;
    private PrivateKey privateKey;

    public Participant(String email) throws Exception {
        this.email = email;

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        this.privateKey = pair.getPrivate();
    }

    public Participant() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        this.privateKey = pair.getPrivate();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public byte[] signContract(String contractData) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(contractData.getBytes("UTF-8"));
        return privateSignature.sign();
    }

    public boolean verifySignature(String contractData, byte[] signature) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(this.publicKey)));
        publicSignature.initVerify(publicKey);
        publicSignature.update(contractData.getBytes("UTF-8"));
        return publicSignature.verify(signature);
    }
}
