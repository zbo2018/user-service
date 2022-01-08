package com.weaver.rpa.tender.search.util;

import java.util.UUID;

/**
 * 生成UUID的工具类
 *
 * @author pengyonglei
 * @version 1.0.0
 */
public class UUIDUtils {

	public static String EASST_ERROR_ID = "EASST_ERROR_";

	/**
	 * 生成error日志专用的UUID
	 */
	public static String generateErrorUUID() {
		return EASST_ERROR_ID + UUID.randomUUID().toString();
	}

	public static void main(String[] args) {
		System.out.println(generateErrorUUID());
	}

}
