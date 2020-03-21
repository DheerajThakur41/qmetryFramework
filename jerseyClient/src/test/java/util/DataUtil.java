package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataUtil {

	public static void updateData(String Keydata,String value) {
		String filepath="./src/test/resources/DataEnv.properties";
		FileInputStream in = null;
		try {
			in = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		props.setProperty(Keydata, value);
		try {
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readData(String Key)  {
		Properties props = new Properties();
		String result = null;
		String filepath="./src/test/resources/DataEnv.properties";
		FileInputStream resource = null;
		try {
			resource = new FileInputStream(filepath);
		} catch (FileNotFoundException e1) {
			System.out.println("Exception during file Read"+e1);
		}
		//		InputStream resource = ClassLoader.class.getResourceAsStream("/DataEnv.properties");
		try {
			if(resource!=null) {
				props.load(resource);
				result = (String) props.get(Key);
			}
			resource.close();
		} catch (IOException e) {
			System.out.println("Error during file loading: "+e.getMessage());
		}
		return result;
	}

	/**
	 * Convert a JSON string to pretty print version
	 * @param jsonString
	 * @return
	 */
	public static String toPrettyFormat(String jsonString) 
	{
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	public static Object readJsonFile(String fileName) throws Exception {
		FileReader reader = new FileReader(System.getProperty("user.dir")+"/src/test/resources/"+fileName);
		JSONParser jsonparser=new JSONParser();
		Object obj = jsonparser.parse(reader);
		return obj;
	}

	public static FileWriter writeJsonFile(String fileName) throws Exception {
		FileWriter writer = new FileWriter(System.getProperty("user.dir")+"/src/test/resources/"+fileName);
		return writer;
	}

	public static String getNextDate(int num) {
		DateTimeZone timeZone = DateTimeZone.forID( "Asia/Kolkata" );
		DateTime now = new DateTime( timeZone ).plusDays(num);
		return now.toString();
	}

	public static String getRandomEvent() {
		FakeValuesService fakeValuesService = new FakeValuesService(
				new Locale("en-GB"), new RandomService());
		return  fakeValuesService.letterify("eventOrganizer_??");
	}
	
	public static String getRandomMail() {
		FakeValuesService fakeValuesService = new FakeValuesService(
				new Locale("en-GB"), new RandomService());
		return  fakeValuesService.letterify("ravi_????@gmail.com");
	}

	public static void main(String[] args) {
		Faker faker1 = new Faker(new Random(24));
		System.out.println(faker1.name().firstName());
	}

}
