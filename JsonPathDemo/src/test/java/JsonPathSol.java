import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.not;


import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.testng.annotations.Test;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class JsonPathSol {

	public InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("sample-data.json");
	public String jsonDataSourceString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();
	DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);

	/**
	 * @desc for use case - Display all users name
	 */
	@Test(enabled = true, priority=0)
	public void displayAll_UserName() {
		System.out.println("*******************Display All User*****************");
		String jsonpathCreatorLocationPath = "$.[*].name";
		List<String> jsonpathCreatorLocation = jsonContext.read(jsonpathCreatorLocationPath);
		//		  Object dataObject = JsonPath.parse(jsonDataSourceString).read("$.[*].name");

		//Print List of all User
		System.out.println("List of all User"+jsonpathCreatorLocation.size());
		for (String user : jsonpathCreatorLocation) {
			System.out.println(user);
		}
		assertThat(jsonpathCreatorLocation.toString(), containsString("Antonia Byers"));
	}
	
	/**
	 * @desc for use case - Display all female users name 
	 */
	@Test(enabled = true,priority=1)
	public void displayAll_FemaleUserName() {
		System.out.println("*******************Display All Female User*****************");
		String jsonpathCreatorLocationPath = "$.[?(@.gender=='female')].name";
		List<String> listoffemaleUSer = jsonContext.read(jsonpathCreatorLocationPath);
		
		System.out.println(listoffemaleUSer);
		assertThat(listoffemaleUSer.toString(), containsString("Phyllis Calhoun"));
		assertThat(listoffemaleUSer.toString(), containsString("Lindsay Stuart"));
	}
	
	/**
	 * @desc display first two User Only 
	 */
	@Test(enabled = true,priority=2)
	public void displayFirstTwoUser() {
		System.out.println("*******************Display First Two User*****************");
		String jsonpathCreatorLocationPath = "$[0,1].name";//$[:2].name
		List<String> first2userList = jsonContext.read(jsonpathCreatorLocationPath);
		
		assertThat(first2userList.toString(), containsString("Snow Rodriguez"));
		assertThat(first2userList.toString(), containsString("Phyllis Calhoun"));
	}
	
	/**
	 * @desc Display last two users only 
	 */
	@Test(enabled = true,priority=3)
	public void displayLastTwoUser() {
		System.out.println("*******************Display Last Two User*****************");
		String jsonpathCreatorLocationPath = "$[-2:].name";//$[-2:].name
		List<String> last2userList = jsonContext.read(jsonpathCreatorLocationPath);
		
		assertThat(last2userList.toString(), containsString("Lindsay Hawkins"));
		assertThat(last2userList.toString(), containsString("Jewel Mccoy"));
	}

	/**
	 * @desc Display total number of friends for first user
	 */
	@Test(enabled = true,priority=4)
	public void showtotalFriends() {
		System.out.println("*******************Display Total friends of First User*****************");
		int i=1;
		String jsonpathSyntax = "$[:1].friends.length()";//$[:1].friends.['name']
		net.minidev.json.JSONArray j = jsonContext.read(jsonpathSyntax);
		int tot_friend_count=(Integer) j.get(0);
		System.out.println("Total Number of Friends"+j.get(0));
		assertEquals(tot_friend_count, 3);
		
	}
	
	/**
	 * @desc Display all users whose age between 24 to 36
	 */
	@Test(enabled = true,priority=5)
	public void showMidAgeUser() {
		System.out.println("*******************Display Mid Age User*****************");
		String jsonpathSyntax = "$.[?(@.age<36 && @.age>24)].name";//$[:1].friends.['name']
		List<String> last2userList = jsonContext.read(jsonpathSyntax);
		
		System.out.println("List of all User Age in Between 24 to 36");
		for (String user : last2userList) {
			System.out.println(user);
		}
		assertThat(last2userList.toString(), containsString("Snow Rodriguez"));
		assertThat(last2userList.toString(), containsString("Ada Potter"));
		
	}
	
	/**
	 * @desc Display all users whose age between 24 to 36(Using Filter)
	 */
	@Test(enabled = true,priority=6)
	public void showMidAgeUser_UsingFilter() {
		System.out.println("*******************Display Mid Age User with Filter*****************");
//		String jsonpathSyntax = "$.[?(@.age<36 && @.age>24)].name";//$[:1].friends.['name']
		Filter expensiveFilter = Filter.filter(Criteria.where("age").gt(24).lt(36));
		 List<Map<String, Object>> expensive = JsonPath.parse(jsonDataSourceString).read("$.[?]",expensiveFilter);
		
		 assertThat(expensive.toString(), containsString("Snow Rodriguez"));
		 assertThat(expensive.toString(), containsString("Leah Downs"));
	     assertThat(expensive.toString(), not(containsString("Lindsay Stuart")));
	}
	
	/**
	 * @desc Display balance of male users
	 */
	@Test(enabled = false,priority=7)
	public void showMaleUserBal() {
		System.out.println("*******************Display Balance of Male User*****************");
//		List<String> last2userList = jsonContext.read("$.[?(@.gender=='male')].balance");
		 List<Map<String, Object>> dataList = jsonContext.read("$.[?(@.gender=='male')]");
		
		 for(int i=0;i<dataList.size();i++) {
				String name = (String) dataList.get(i).get("name");
				String bal = (String) dataList.get(i).get("balance");
				System.out.println(name+" : "+bal);
			}
	}
	
//	$[?(@.address =~/^.*California.*$/)]name//need to use
	/**
	 * @desc Display all users who lives in California
	 */
	@Test(enabled = true,priority=8)
	public void showUserLivesinCalfornia () {
		System.out.println("*******************Display User Lives in California*****************");
		Filter cityfilter = Filter.filter(Criteria.where("address").contains("California"));
		 List<Map<String, Object>> expensive = JsonPath.parse(jsonDataSourceString).read("$.[?]",cityfilter);
			
		 for (Map<String, Object> map : expensive) {
			 String name = (String) map.get("name");
			String bal = (String) map.get("address");
					System.out.println(name+" : "+bal);
		}
		 
//		 for(int i=0;i<expensive.size();i++) {
//				String name = (String) expensive.get(i).get("name");
//				String bal = (String) expensive.get(i).get("address");
//				System.out.println(name+" : "+bal);
//			}
		 assertThat(expensive.toString(), containsString("Lindsay Stuart"));
	}
	
	
	
	
	
	
	
}
