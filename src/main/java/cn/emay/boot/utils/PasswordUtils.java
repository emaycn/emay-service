package cn.emay.boot.utils;

import java.io.UnsupportedEncodingException;

import cn.emay.utils.encryption.Aes;
import cn.emay.utils.encryption.HexByte;

/**
 * 密码工具类
 * 
 * @author Frank
 *
 */
public class PasswordUtils {

	public final static byte[] AES_PASSWORD = "EMAYEUCPEMASFLOW".getBytes();

	public final static String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

	public static String encrypt(String password) {
		return HexByte.byte2Hex(Aes.encrypt(password.getBytes(), AES_PASSWORD, AES_ALGORITHM));
	}

	public static String dncrypt(String encrypassword) {
		try {
			return new String(Aes.decrypt(HexByte.hex2Byte(encrypassword), AES_PASSWORD, AES_ALGORITHM), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
