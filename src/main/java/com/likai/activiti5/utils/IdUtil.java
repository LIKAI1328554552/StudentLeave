package com.likai.activiti5.utils;

import java.util.UUID;

/**
 * 生成随机id工具类
 * @author likai
 * @since 2018-04-30
 */
public class IdUtil {

	/**
	 * 使用java自带的UUID生成随机id，不带"-"
	 * @return 随机id
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-","") ;
	}
	
	/**
	 * 使用java自带的UUID生成随机id，带"-"
	 * @return 随机id
	 */
	public static String uuid() {
		return UUID.randomUUID().toString() ;
	}
	
}
