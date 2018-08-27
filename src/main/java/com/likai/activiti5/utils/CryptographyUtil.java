package com.likai.activiti5.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * md5加密
 * @author likai
 * @since 2018-04-30
 */
public class CryptographyUtil {

	
	/**
	 * Md5加密
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String md5(String str,String salt){
		//shiro提供支持
		return new Md5Hash(str,salt).toString();
	}
	
	public static void main(String[] args) {
		String password="123456";
		
		System.out.println(CryptographyUtil.md5(password, "zhangsan"));
	}
}
