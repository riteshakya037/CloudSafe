package Algo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Prefs{

	public String getProp(String key) throws IOException
	{
		String configFilePath = "settings.properties";
		Properties properties = new Properties();
		FileInputStream  fis = new FileInputStream(configFilePath);
		properties.load(fis);
		if (fis != null) {
		    fis.close();
		}
		String value = properties.getProperty(key);
		return value;
	}
	public void setProp(String key,String value) throws Exception{
		String configFilePath = "settings.properties";
		Properties properties = new Properties();
		FileInputStream  fis = new FileInputStream(configFilePath);
		properties.load(fis);
		if (fis != null) {
		    fis.close();
		}
		properties.setProperty(key, value);
		
		FileOutputStream fos = new FileOutputStream(configFilePath);
		properties.store(fos, "My Application Settings");
	}
}