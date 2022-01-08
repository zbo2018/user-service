package com.weaver.rpa.tender.search.util;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密解密
 *
 * @author pengyonglei
 * @version 1.0.0
 */
public class RSAEncrypt {

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	// 用于登录接口对密码加密的privateKey
	private static final String LOGIN_PRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJd2k7MrPv5klhSPN7pko8y0UnLBnFZiKqowtlNSIwH2aZCPwG5kUVaWIrvVZZOVD/1IPvHfPCB1SNqWb2kTRE4zV9TZyFdGUEZkzJUaru/PbmJKkdJIgtbpgC6p3pmgRMpGPMC6u3cp8rRDih1XSGyIgqT/QLitpw69gmz6Tp7xAgMBAAECgYBYBVsUdn9nS4FEdW6HbdEbN3fqpMV8FhSBT0nF5ifeDhuPHVtRTA9Q6HNLAVrEh44BPakG2xqQKliqX15jjATXHwXuMMRXDRyrntXOa88mOEBSIpvZSC2oMWpxXJcZ7mRoHriVvhlRSQKjT90f9olkqZvsJQQhI3icZYo0gUFPgQJBANw12qgxLpr/yS5MAD2CC9N3pfqLxhFkuxHyiUnJqG7YHLdhU4pCeriMZ6W2iqP4zAnbgCFz7ZjDohO9Nn2AdkkCQQCwFGeoUVlnmkLD0lkxIaNrl89lUV78OtWiU8EMTOBFesbIkHa6vitUsuIkfBJwyOCIHO99h+wkKV7h/92QYUNpAkEAnnmzTm92lt2VrtfLJHi+ggUNUe3dMQ9JAXWGD7AsovisUdyc/lcxcja5MKzZwX9/d2icIXcIenPv5Dy7Rj544QJAVMg+cSlaZyw74ZYzJBkSWwgp8JsRpVme7B5v4cBSbKIFfSUyRhCzRIEtqmujZXMTyfZEyqjb5z2UVjWhNZffoQJBAK7RnglOHX21ad367IVwQEfD4nZYq8aMcgOD26WSjtLio66LrIsUvMUAACRnDZ4LV4DZVcfASlHq91XgzPkrmoI=";
	// 用于登录接口对密码解密的publicKey
	public static final String LOGIN_PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXdpOzKz7+ZJYUjze6ZKPMtFJywZxWYiqqMLZTUiMB9mmQj8BuZFFWliK71WWTlQ/9SD7x3zwgdUjalm9pE0ROM1fU2chXRlBGZMyVGq7vz25iSpHSSILW6YAuqd6ZoETKRjzAurt3KfK0Q4odV0hsiIKk/0C4racOvYJs+k6e8QIDAQAB";

	public static Integer MAX_ENCRYPT_BLOCK = 117;
	public static Integer MAX_DECRYPT_BLOCK = 128;

	/**
	 * 随机生成密钥对
	 */
	public static Map<String, String> genKeyPair() {
		Map<String, String> resultMap = new HashMap<>();
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return resultMap;
		}
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024,new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		try {
			// 得到公钥字符串
			String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
			// 得到私钥字符串
			String privateKeyString = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
			// 将密钥对返回
			resultMap.put("publicKey", publicKeyString);
			resultMap.put("privateKey", privateKeyString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64.getDecoder().decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从字符串中加载私钥
	 *
	 * @param privateKeyStr 私钥数据字符串
	 * @throws Exception 加载私钥时产生的异常
	 */
	public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
			throws Exception {
		try {
			if (StringUtils.isEmpty(privateKeyStr)) {
				privateKeyStr = LOGIN_PRIVATEKEY;
			}
			byte[] buffer = Base64.getDecoder().decode(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 公钥加密过程 (建议使用encryptBySegment)
	 *
	 * @param publicKey 公钥
	 * @param plainTextData 明文数据
	 * @throws Exception 加密过程中的异常信息
	 */
	@Deprecated
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(plainTextData);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	private static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}

	/**
	 * 公钥的分段加密
	 *
	 * @param publicKey			公钥
	 * @param plainTextData		明文
	 */
	public static String encryptBySegment(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int inputLen = plainTextData.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			byte[] bytes = new byte[]{};
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(plainTextData, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(plainTextData, offSet, inputLen - offSet);
				}
				bytes = addBytes(bytes, cache);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			return new String(Base64.getEncoder().encode(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥加密过程 (建议使用encryptBySegment)
	 *
	 * @param privateKey 私钥
	 * @param plainTextData 明文数据
	 * @throws Exception 加密过程中的异常信息
	 */
	@Deprecated
	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return cipher.doFinal(plainTextData);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥分段加密过程
	 *
	 * @param privateKey 私钥
	 * @param plainTextData 明文数据
	 * @throws Exception 加密过程中的异常信息
	 */
	public static String encryptBySegment(RSAPrivateKey privateKey, byte[] plainTextData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			int inputLen = plainTextData.length;
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			byte[] bytes = new byte[]{};
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(plainTextData, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(plainTextData, offSet, inputLen - offSet);
				}
				bytes = addBytes(bytes, cache);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			return new String(Base64.getEncoder().encode(bytes));
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 私钥解密过程 (建议使用decryptBySegment)
	 *
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	@Deprecated
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(cipherData);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 私钥的分段解密
	 *
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decryptBySegment(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int inputLen = cipherData.length;
			byte[] bytes = new byte[]{};
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
				}
				bytes = addBytes(bytes, cache);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			return new String(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 公钥解密过程 (建议使用decryptBySegment)
	 *
	 * @param publicKey 公钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception  解密过程中的异常信息
	 */
	@Deprecated
	public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(cipherData);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 公钥分段解密过程
	 *
	 * @param publicKey 公钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception  解密过程中的异常信息
	 */
	public static String decryptBySegment(RSAPublicKey publicKey, byte[] cipherData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		Cipher cipher;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			int inputLen = cipherData.length;
			byte[] bytes = new byte[]{};
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
				}
				bytes = addBytes(bytes, cache);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			return new String(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	/**
	 * 获取解密后的密码
	 *
	 * @param password	前端经过RSA私钥加密的密码串
	 */
	public static String getDecryptPwd(String password) throws Exception {
		if (StringUtils.isBlank(password)) {
			return null;
		}
		byte[] bytes = RSAEncrypt.decrypt(loadPublicKeyByStr(LOGIN_PUBLICKEY), Base64.getDecoder().decode(password.getBytes()));
		if (bytes == null) {
			return null;
		} else {
			return new String(bytes);
		}
	}

	/**
	 *  签名
	 *
	 *  @param data 待签名数据
	 *  @param privateKey 私钥
	 *  @return 签名
	 */
	public static String sign(String data, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(key);
		signature.update(data.getBytes());
		return new String(Base64.getEncoder().encode(signature.sign()));
	}

	/**
	 * 验签
	 *
	 * @param srcData   原始字符串
	 * @param publicKey 公钥
	 * @param sign      签名
	 * @return 是否验签通过
	 */
	public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(key);
		signature.update(srcData.getBytes());
		return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
	}

	/**
	 * 字节数据转十六进制字符串
	 *
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}


	public static void main(String[] args) throws Exception {

//		Map<String, String> rsaKey = genKeyPair();
//		String publicKey = rsaKey.get("publicKey");
//		String privateKey = rsaKey.get("privateKey");
//		System.out.println("publicKey = " + publicKey);
//		System.out.println("privateKey = " + privateKey);

//		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJd2k7MrPv5klhSPN7pko8y0UnLBnFZiKqowtlNSIwH2aZCPwG5kUVaWIrvVZZOVD/1IPvHfPCB1SNqWb2kTRE4zV9TZyFdGUEZkzJUaru/PbmJKkdJIgtbpgC6p3pmgRMpGPMC6u3cp8rRDih1XSGyIgqT/QLitpw69gmz6Tp7xAgMBAAECgYBYBVsUdn9nS4FEdW6HbdEbN3fqpMV8FhSBT0nF5ifeDhuPHVtRTA9Q6HNLAVrEh44BPakG2xqQKliqX15jjATXHwXuMMRXDRyrntXOa88mOEBSIpvZSC2oMWpxXJcZ7mRoHriVvhlRSQKjT90f9olkqZvsJQQhI3icZYo0gUFPgQJBANw12qgxLpr/yS5MAD2CC9N3pfqLxhFkuxHyiUnJqG7YHLdhU4pCeriMZ6W2iqP4zAnbgCFz7ZjDohO9Nn2AdkkCQQCwFGeoUVlnmkLD0lkxIaNrl89lUV78OtWiU8EMTOBFesbIkHa6vitUsuIkfBJwyOCIHO99h+wkKV7h/92QYUNpAkEAnnmzTm92lt2VrtfLJHi+ggUNUe3dMQ9JAXWGD7AsovisUdyc/lcxcja5MKzZwX9/d2icIXcIenPv5Dy7Rj544QJAVMg+cSlaZyw74ZYzJBkSWwgp8JsRpVme7B5v4cBSbKIFfSUyRhCzRIEtqmujZXMTyfZEyqjb5z2UVjWhNZffoQJBAK7RnglOHX21ad367IVwQEfD4nZYq8aMcgOD26WSjtLio66LrIsUvMUAACRnDZ4LV4DZVcfASlHq91XgzPkrmoI=";
//		String text = "Weaver@3434~*&20EE290$";
//		byte[] secretTextBytes = RSAEncrypt.encrypt(loadPrivateKeyByStr(privateKey), text.getBytes());
//		String secretText = "";
//		if (secretTextBytes!= null) {
//			secretText = new String(Base64.getEncoder().encode(secretTextBytes));
//			System.out.println("密文 = " + secretText);
//		}
//
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXdpOzKz7+ZJYUjze6ZKPMtFJywZxWYiqqMLZTUiMB9mmQj8BuZFFWliK71WWTlQ/9SD7x3zwgdUjalm9pE0ROM1fU2chXRlBGZMyVGq7vz25iSpHSSILW6YAuqd6ZoETKRjzAurt3KfK0Q4odV0hsiIKk/0C4racOvYJs+k6e8QIDAQAB";
//		String mingwen = RSAEncrypt.getDecryptPwd(secretText);
//		System.out.println("明文 = " + mingwen);

		//----------------------- 分段加解密 start -----------------------------------------
		Map<String, String> params = new HashMap<>();
		params.put("key1", "下班了下班了下班了下班66666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了下班了");
		params.put("key2", "签到签到签到签到签到签到签到签到签到签到签77777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到签到");
		params.put("key3", "出差出差出差出差出差出差出差出8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差出差");
		String text = new Gson().toJson(params);
		String secretText = RSAEncrypt.encryptBySegment(loadPublicKeyByStr(LOGIN_PUBLICKEY), text.getBytes());
		System.out.println("分段加密的密文=" + secretText);
		byte[] bb = Base64.getDecoder().decode(secretText.getBytes());
		System.out.println("分段解密的解密后= " + RSAEncrypt.decryptBySegment(loadPrivateKeyByStr(LOGIN_PRIVATEKEY), bb));
		//----------------------- 分段加解密 END -----------------------------------------

//		System.out.println(RSAEncrypt.getDecryptPwd("C3E+nztMQeGLiicIVCznzge6oBucx7TxTpgMiS0xiDrcbwVvA4kYBxnqkdHTsbaXuzn7K9ibCLx/YDIFgB6MdGqsEVGA2yFc0FFivMN5D+7IoBZB5N2/KbULweU+OiKDAd95SwA18FRYawzM1RRj0uJADZr9iU2wCqNwFjFdPgU="));

		//----------------------- 签名 和 验签 start -----------------------------------------
		String sign = sign("appId", loadPrivateKeyByStr(LOGIN_PRIVATEKEY));
		System.out.println("sign = " + sign);
		boolean isValid = verify("appId", loadPublicKeyByStr(LOGIN_PUBLICKEY), sign);
		System.out.println("是否合法: " + isValid);
		//----------------------- 签名 和 验签 end -----------------------------------------
	}

}
