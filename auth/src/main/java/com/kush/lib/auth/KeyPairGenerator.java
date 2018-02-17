package com.kush.lib.auth;

public interface KeyPairGenerator {

    KeyPair generate() throws KeyGenerationFailedException;
}
