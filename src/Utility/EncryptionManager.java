
package Utility;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

import ServerMainBody.Settings;

public class EncryptionManager {
	public static String encrypt16bits(String message) throws Exception {

		// use key coss2
		SecretKeySpec skeySpec = new SecretKeySpec(Settings.sEncryptKey.getBytes(), "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(message.getBytes());

		return Hex.encodeHexString(encrypted);

	}

	public static String decrypt16bits(String encrypted) throws Exception {

		// use key coss2
		SecretKeySpec skeySpec = new SecretKeySpec(Settings.sEncryptKey.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] original = cipher.doFinal(Hex.decodeHex(encrypted.toCharArray()));

		String originalString = new String(original);

		return originalString;
	}

	private static Key generateKey64bits() {
		byte[] keyValue = null;
		MessageDigest sha = null;
		try {
			keyValue = Settings.keyStr.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			keyValue = sha.digest(keyValue);
			keyValue = Arrays.copyOf(keyValue, 16); // use only first 128 bit
			Key key = new SecretKeySpec(keyValue, Settings.ALGO);
			return key;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

		return null;
	}

	public static String encrypt64bits(String Data) {
		Key key;
		Cipher c;
		byte[] encVal = null;
		try {
			key = generateKey64bits();
			c = Cipher.getInstance(Settings.ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			encVal = c.doFinal(Data.getBytes());
			String encryptedValue = DatatypeConverter.printBase64Binary(encVal);
			return encryptedValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return null;
	}

	public static String decrypt64bits(String encryptedData) {
		Key key;
		Cipher c;
		byte[] decValue = null;
		try {
			key = generateKey64bits();
			c = Cipher.getInstance(Settings.ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedData);
			decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

		return null;

	}
}
