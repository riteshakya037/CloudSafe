package Algo;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hashing {
	protected String privateKey="paperPC";

	public String SHA256(String password)throws Exception
	{

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());

		byte byteData[] = md.digest();

		//convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<byteData.length;i++) {
			String hex=Integer.toHexString(0xff & byteData[i]);
			if(hex.length()==1) hexString.append('0');
			hexString.append(hex);
		}
		System.out.println("SHA-256 Output : " + hexString.toString());
		return hexString.toString(); 
	}

	public String HmacSHA256(String msg) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec((privateKey).getBytes("UTF-8"), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		//System.out.println("HmacSHA-256 Output : " + digest);
		return digest;
	}

	public static final int BUFFER_SIZE = 2048;
	public String getChecksum(String filePath) throws Exception {


		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		FileInputStream fileInput = new FileInputStream(filePath);
		byte[] dataBytes = new byte[1024];

		int bytesRead = 0;

		while ((bytesRead = fileInput.read(dataBytes)) != -1) {
			messageDigest.update(dataBytes, 0, bytesRead);
		}


		byte[] digestBytes = messageDigest.digest();

		StringBuffer sb = new StringBuffer("");

		for (int i = 0; i < digestBytes.length; i++) {
			sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		System.out.println("SHA256 of File: " + sb.toString());

		fileInput.close();
		return sb.toString();
	}
}
