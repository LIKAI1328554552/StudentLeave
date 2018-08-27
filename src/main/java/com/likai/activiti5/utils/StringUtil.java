package com.likai.activiti5.utils;

/**
 * 字符串工具类
 * @author likai
 * @since 2018-04-30
 */
public class StringUtil {

	public static String nullBlack(String str) {
		return (str == null || "".equals(str))? "" : str ;
	}
	
	public static boolean isEmpty(String str) {
		if(str == null ||"".equals(str)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	public static boolean isNotEmpty(String str) {
		if(str == null || "".equals(str)) {
			return false ;
		}
		return true ;
	}
}
