//package qaf.example.tests;
//
//import java.util.Date;
//
//import com.qmetry.qaf.automation.data.BaseDataBean;
//import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;
//import com.qmetry.qaf.automation.util.Randomizer;
//
//public class SampleBean extends BaseDataBean {
//
//	@Randomizer(type=RandomizerTypes.LETTERS_ONLY)
//	private String name;
//	
//	@Randomizer(type=RandomizerTypes.DIGITS_ONLY,minval=18,maxval=100)
//	private String age;
//	
//	@Randomizer(minval=7,maxval=15)
//	private Date dateOfTravel;
//	
//	@Randomizer(suffix="@mailinator.com", length=6)
//	private String email;
//	
//	private String pwd;
//	@Randomizer(format="999-99-9999")
//	private String ssn;
//	
//	@Randomizer(skip=true)
//	private String dontRandomizeMe;
//	
//	public static void main(String[] args) {
//		SampleBean sb=new SampleBean();
////		System.out.println(SampleBean.getRandomValue(sb.name));
//		System.out.println("Hello");
//	}
//}