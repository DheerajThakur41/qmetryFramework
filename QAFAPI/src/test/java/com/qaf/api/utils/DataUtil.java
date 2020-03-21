package com.qaf.api.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.qmetry.qaf.automation.util.RandomStringGenerator;

import net.bytebuddy.utility.RandomString;

public class DataUtil {

	public static void updateData(String Keydata,String value) {
		String filepath="./resources/repository/DataEnv.properties";
		Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(filepath);
			props.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filepath);
			props.setProperty(Keydata, value);
			props.store(out, null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getData(String Key)  {
		 Properties props = new Properties();
		 String result = null;
		 String filepath="./resources/repository/DataEnv.properties";
		  FileInputStream resource = null;
		try {
			resource = new FileInputStream(filepath);
		} catch (FileNotFoundException e1) {
			System.out.println("Exception during file Read"+e1);
		}
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
	
	
	public static String randomString(String w) {
		RandomString r=new RandomString();
		return w+r.make(5);
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(randomString("hello"));
	}
}
