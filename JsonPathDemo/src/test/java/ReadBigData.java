import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.testng.annotations.Test;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

public class ReadBigData {

	public InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("DataFile.json");
	public String jsonDataSourceString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();
	DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);
	
	
	@Test(enabled = true)
	public void showUserLivesinCalfornia () {
//		System.out.println(jsonDataSourceString);	
		
		System.out.println("*******************Display US user*****************");
		Filter cityfilter = Filter.filter(Criteria.where("contact").contains("example"));
//		 List<Map<String, Object>> expensive = JsonPath.parse(jsonDataSourceString).read("$.[data][?]",cityfilter);
			
		 
		 List<Map<String, Object>> expensive = JsonPath.parse(jsonDataSourceString)
			      .read("$[?(@.contact == 'example')]");
		 
//		 $[?('example' in @['contact'])]
		 
		 for (Map<String, Object> map : expensive) {
//			 String name = (String) map.get("name");
//			String bal = (String) map.get("address");
			System.out.println(" : "+map);
		}
	} 
	
	
	public static void main(String[] args) {

	}

}
