package cn.emay.boot.utils;

import cn.emay.utils.encryption.Aes;
import cn.emay.utils.encryption.Md5;

/**
 * 密码工具类
 * 
 * @author Frank
 *
 */
public class PasswordUtils {

	/**
	 * 加密密码
	 * 
	 * @param password
	 *            密码
	 * @return 加密后的密码
	 */
	public static String encrypt(String password) {
		return Md5.md5(Aes.encrypt(password.getBytes(), "EMAYEUCPEMRPEBDP".getBytes(), "AES/ECB/PKCS5Padding"));
	}
	
}
