package com.kush.lib.auth;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeyToolBasedKeyPairGenerator implements KeyPairGenerator {

    private final char[] keyStorePassword;
    private final char[] keyPassword;

    public KeyToolBasedKeyPairGenerator(char[] keyStorePassword, char[] keyPassword) {
        this.keyStorePassword = keyStorePassword;
        this.keyPassword = keyPassword;
    }

    @Override
    public KeyPair generate() throws KeyGenerationFailedException {
        KeyStore keyStore = generateKeyStore();
        loadKeyStore(keyStore);
        Key privateKey = getPrivateKey(keyStore);
        privateKey.toString();
        return null;
    }

    private Key getPrivateKey(KeyStore keyStore) throws KeyGenerationFailedException {
        try {
            return keyStore.getKey(null, keyPassword);
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new KeyGenerationFailedException(e.getMessage(), e);
        }
    }

    private void loadKeyStore(KeyStore keyStore) throws KeyGenerationFailedException {
        try {
            keyStore.load(null, keyStorePassword);
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new KeyGenerationFailedException(e.getMessage(), e);
        }
    }

    private KeyStore generateKeyStore() throws KeyGenerationFailedException {
        try {
            return KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            throw new KeyGenerationFailedException(e.getMessage(), e);
        }
    }
}
