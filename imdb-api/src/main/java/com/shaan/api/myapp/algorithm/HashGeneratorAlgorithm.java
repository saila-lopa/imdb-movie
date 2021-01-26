package com.shaan.api.myapp.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

public class HashGeneratorAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(HashGeneratorAlgorithm.class);

    public static boolean validateHash(String inputValue, String compareValue)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        logger.debug("HashGeneratorAlgorithm.validateHash({}, {}).",
                inputValue, compareValue);
        if (compareValue == null || inputValue == null) {
            logger.error("An error occurred while attempting to " +
                            "call HashGeneratorAlgorithm.validateHash({}, {})",
                    inputValue, compareValue);
            return false;
        }

        String[] parts = compareValue.split(":");
        int iterations = toInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(inputValue.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }

        return diff == 0;
    }

    public static String generateHash(String inputValue) {
        logger.debug("HashGeneratorAlgorithm.generateHash({}).", inputValue);
        int iterations = 1000;
        char[] chars = inputValue.toCharArray();
        byte[] salt = new byte[0];
        try {
            salt = getSalt().getBytes();
        } catch (NoSuchAlgorithmException e) {
            logger.info("Error while generated hash" + e);
            return null;
        }

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 512);
        SecretKeyFactory skf = null;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            logger.info("Error while generated hash" + e);
            return null;
        }
        byte[] hash = new byte[0];
        try {
            hash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            logger.info("Error while generated hash" + e);
            return null;
        }

        try {
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.info("Error while generated hash" + e);
            return null;
        }
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Arrays.toString(salt);
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
