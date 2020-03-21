import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class JsonReadFile {

	public static void main(String[] args) {
		String filepath="D:\\MyPractice\\EclipseFolder\\JsonPathDemo\\src\\test\\resources\\sample-data.json";
		
	//FirstWay
		//JSON parser object to parse read file
     /*   JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("D:\\MyPractice\\EclipseFolder\\JsonPathDemo\\src\\test\\resources\\sample-data.json"))
        {
            //Read JSON file
            try {
            	 
				Object obj = jsonParser.parse(reader);
				  System.out.println(obj.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
      */
        
		try {
			String jsonString = new Scanner(new File(filepath)).useDelimiter("\\Z").next();
			DocumentContext jsonContext = JsonPath.parse(jsonString);
			 String jsonpath = "$.[?(@.address =~/.*california.*/i)]";
			   System.out.println(jsonContext.read(jsonString).toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        
	}
}
