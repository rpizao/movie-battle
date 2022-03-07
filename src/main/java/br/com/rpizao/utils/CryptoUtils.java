package br.com.rpizao.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import br.com.rpizao.exceptions.BusinessException;

public final class CryptoUtils {
	
	private static final Random RANDOM = new SecureRandom(); 

	public static String generateSalt() {
		byte[] salt = new byte[16];
	    RANDOM.nextBytes(salt);
	    return salt.toString();
	}
	
	public static String hash(String passw, String salt) {
		return encrypt(passw, salt);
	}
	
	private static String encrypt(String password, String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // Add password bytes to digest
            md.update(salt.getBytes());
            
            // Get the hash's bytes
            byte[] bytes = md.digest(password.getBytes());
            
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            
            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("Erro ao encryptar senha", e);
        }
        return generatedPassword;
    }
	
	public static String randomString(int size) {
		return RandomStringUtils.randomAlphabetic(size);
	}
}
