	package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.restassured.response.Response;

public class DataUtil {
	
	public static void main(String[] args) {
//		System.out.println(DataUtil.getRandomData().letterify("??Jhon????@gmail.com"));
		System.out.println(getRandomName()+"  "+getRandomMail());
//		getClass().getClassLoader().getResourceAsStream("config.properties");
//		new Properties().load()
	}
	
	/*
	 * public static String getData(String Key) { ResourceBundle rb =
	 * ResourceBundle.getBundle("TestData"); return rb.getString(Key); }
	 */
	
	public static String getData(String Key)  {
		 Properties props = new Properties();
		 String result = null;
		 String filepath="./src/test/resources/TestData.properties";
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
	
	public static void updateData(String Keydata,String value) {
		String filepath=System.getProperty("user.dir")+"/src/test/resources/TestData.properties";
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
	 
	 @SuppressWarnings("unchecked")
	public static String getCreateUserBody() throws Exception {
		 String newFName=DataUtil.getRandomName();
		 String newemail=DataUtil.getRandomMail();
		 updateData("User.email", newemail);
		 updateData("User.Fname", newFName);
		 
		 Object obj = DataUtil.readJsonFile("UserPayload.json");
		 JSONObject jsonObject = (JSONObject) obj;
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject attributes = (JSONObject) data.get("attributes");
			data.remove("id");
			attributes.put("password", DataUtil.getData("User.password"));
			attributes.put("email", newemail);
			attributes.put("details",DataUtil.getData("User.details"));
			attributes.put("first-name",newFName);
			String userData=null;
			FileWriter file;
			
				file = DataUtil.writeJsonFile("UserPayload.json");
				file.write(jsonObject.toString());
				file.flush();
				userData = DataUtil.readJsonFile("UserPayload.json").toString();
				return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String getupdatedUserBody() throws Exception {
		 Object obj = readJsonFile("UserPayload.json");
		 String newFName=DataUtil.getRandomName();
		 String newemail=DataUtil.getRandomMail();
		 updateData("newUser_email", newemail);
		 updateData("newUSer_firstName", newFName);
				 
		 JSONObject jsonObject = (JSONObject) obj;
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject attributes = (JSONObject) data.get("attributes");
			data.put("id",getData("new.UserId"));
			attributes.put("email",newemail);
			attributes.put("first-name",newFName);
			String userData=null;
			FileWriter file;
			
				file = DataUtil.writeJsonFile("UserPayload.json");
				file.write(jsonObject.toString());
				file.flush();
				userData = DataUtil.readJsonFile("UserPayload.json").toString();
				return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String getEventTypePatch() throws Exception {
		 Object obj = readJsonFile("eventType.json");
				 
		 JSONObject jsonObject = (JSONObject) obj;
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject attributes = (JSONObject) data.get("attributes");
			data.put("id",getData("Event.TypeId"));
			attributes.put("name","Updated Camp, Treat and Retreat");
			String userData=null;
			FileWriter file;
			
				file = DataUtil.writeJsonFile("eventType.json");
				file.write(jsonObject.toString());
				file.flush();
				userData = DataUtil.readJsonFile("eventType.json").toString();
				return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String createEventType() throws Exception {
		 Object obj = readJsonFile("eventType.json");

		 JSONObject jsonObject = (JSONObject) obj;
		 JSONObject data = (JSONObject) jsonObject.get("data");
		 JSONObject attributes = (JSONObject) data.get("attributes");
		 data.remove("id");
		 attributes.put("name","Camp, Treat and Retreat");
		 String userData=null;
		 FileWriter file;

		 file = DataUtil.writeJsonFile("eventType.json");
		 file.write(jsonObject.toString());
		 file.flush();
		 userData = DataUtil.readJsonFile("eventType.json").toString();
		 return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String createEventTopicBody() throws Exception {
		 Object obj = readJsonFile("eventTopic.json");

		 JSONObject jsonObject = (JSONObject) obj;
		 JSONObject data = (JSONObject) jsonObject.get("data");
		 JSONObject attributes = (JSONObject) data.get("attributes");
		 data.remove("id");
		 attributes.put("name","Travel & Outdoor");
		 String userData=null;
		 FileWriter file;

		 file = DataUtil.writeJsonFile("eventTopic.json");
		 file.write(jsonObject.toString());
		 file.flush();
		 userData = DataUtil.readJsonFile("eventTopic.json").toString();
		 return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
	public static String patchEventTopicBody() throws Exception {
		 Object obj = readJsonFile("eventTopic.json");

		 JSONObject jsonObject = (JSONObject) obj;
		 JSONObject data = (JSONObject) jsonObject.get("data");
		 JSONObject attributes = (JSONObject) data.get("attributes");
		 data.put("id",getData("Event.TopicId"));
		 attributes.put("name","Updated Travel & Outdoor");
		 String userData=null;
		 FileWriter file;

		 file = DataUtil.writeJsonFile("eventTopic.json");
		 file.write(jsonObject.toString());
		 file.flush();
		 userData = DataUtil.readJsonFile("eventTopic.json").toString();
		 return userData;
	 }
	 
	 @SuppressWarnings("unchecked")
		public static String createEventBody() throws Exception {
			 Object obj = readJsonFile("createEvent.json");

			 JSONObject jsonObject = (JSONObject) obj;
				JSONObject data = (JSONObject) jsonObject.get("data");
				JSONObject attributes = (JSONObject) data.get("attributes");
//				String name = (String) attributes.get("name");//Take Name Value
				attributes.put("starts-at", DataUtil.getNextDate(2));
				attributes.put("ends-at", DataUtil.getNextDate(4));
				attributes.put("schedule-published-on", DataUtil.getNextDate(-2));
				attributes.put("name",getData("Event.Name"));
				data.remove("id");

			 FileWriter file = DataUtil.writeJsonFile("createEvent.json");
			 file.write(jsonObject.toString());
			 file.flush();
			 String userData = DataUtil.readJsonFile("createEvent.json").toString();
			 return userData;
		 }
	 
	 @SuppressWarnings("unchecked")
		public static String patchEventBody() throws Exception {
			 Object obj = readJsonFile("createEvent.json");

			 JSONObject jsonObject = (JSONObject) obj;
				JSONObject data = (JSONObject) jsonObject.get("data");
				JSONObject attributes = (JSONObject) data.get("attributes");
//				String name = (String) attributes.get("name");//Take Name Value
				attributes.put("starts-at", DataUtil.getNextDate(2));
				attributes.put("ends-at", DataUtil.getNextDate(4));
				attributes.put("schedule-published-on", DataUtil.getNextDate(-2));
				attributes.put("name", getData("Updated.Event.Name"));
				data.put("id",DataUtil.getData("Event.Id"));
				
			 FileWriter file = DataUtil.writeJsonFile("createEvent.json");
			 file.write(jsonObject.toString());
			 file.flush();
			 String userData = DataUtil.readJsonFile("createEvent.json").toString();
			 return userData;
		 }
	 
	 public static void showResponseBody(Response resp) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(resp.getBody().asString());
			String prettyJsonString = gson.toJson(je);
			System.out.println("===========Response Body==================\n"+prettyJsonString);
		}
	 
	 public static String getRandomEvent() {
		  FakeValuesService fakeValuesService = new FakeValuesService(
			      new Locale("en-GB"), new RandomService());
		  return  fakeValuesService.letterify("eventOrganizer_??");
	  }
	 
	 public static  String getRandomName() {
		 FakeValuesService fakeValuesService = new FakeValuesService(
			      new Locale("en-GB"), new RandomService());
		  return  fakeValuesService.letterify("rahul????");
		  
	  }
	 public static  String getRandomMail() {
		 FakeValuesService fakeValuesService = new FakeValuesService(
			      new Locale("en-GB"), new RandomService());
		  return  fakeValuesService.letterify("rahul????@??gmail.com");
		  
	  }
	 public static String getNextDate(int num) {
		  DateTimeZone timeZone = DateTimeZone.forID( "Asia/Kolkata" );
			DateTime now = new DateTime( timeZone ).plusDays(num);
			return now.toString();
	  }
}
