package Algo;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
 
public class AESEncrypter
{
	Cipher ecipher;
	Cipher dcipher;
	String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db",tmpLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\temp\\Cloudsafe.db";
	
	public AESEncrypter () throws Exception
	{

		String salt="a5074c9e145cac333056df8d15ff24cc7de06acf7fd9734092527d90161e163c";
		// Generate a temporary key. In practice, you would save this key.
		// See also e464 Encrypting with DES Using a Pass Phrase.
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(salt.toCharArray(), salt.getBytes(), 65536, 128);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		// Create an 8-byte initialization vector
		byte[] iv = new byte[]
		{
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
		};
		
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try
		{
			ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			// CBC requires an initialization vector
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Buffer used to transport the bytes from one stream to another
	byte[] buf = new byte[1024];
	
	public void encryption(InputStream in, OutputStream out) throws Exception
	{
		try
		{
			// Bytes written to out will be encrypted
			out = new CipherOutputStream(out, ecipher);
			
			// Read in the cleartext bytes and write to out to encrypt
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0)
			{
				out.write(buf, 0, numRead);
			}
			out.close();
			in.close();
		}
		catch (java.io.IOException e)
		{
		}

		
	}
	
	public void decryption(InputStream in, OutputStream out)
	{
		try
		{
			// Bytes read from in will be decrypted
			in = new CipherInputStream(in, dcipher);
			
			// Read in the decrypted bytes and write the cleartext to out
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0)
			{
				out.write(buf, 0, numRead);
			}
			out.close();
			in.close();
		}
		catch (java.io.IOException e)
		{
		}
	}
	


	public void encrypt() throws Exception {
		File file=new File(tmpLocation);
		if(file.exists()){
		}
		else
		{
			File dir = new File(System.getProperty("user.home") + File.separatorChar + "My Documents\\temp");
			dir.mkdir();
			new File(tmpLocation);
		}	
		AESEncrypter encrypter = new AESEncrypter();	
		encrypter.encryption(new FileInputStream(dbLocation),new FileOutputStream(tmpLocation));
		Path from = Paths.get(tmpLocation); //convert from File to Path
		Path to = Paths.get(dbLocation); //convert from String to Path
		Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

	}

	public void decrypt() throws Exception {
		
		AESEncrypter encrypter = new AESEncrypter();
		encrypter.decryption(new FileInputStream(dbLocation),new FileOutputStream(tmpLocation));
		Path from = Paths.get(tmpLocation); //convert from File to Path
		Path to = Paths.get(dbLocation); //convert from String to Path
		Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
	}
	
}