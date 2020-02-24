package cn.emay.utils;

import java.io.File;
import java.util.Date;

import cn.emay.utils.date.DateUtils;

/**
 * 文件存储路径定义
 * 
 * @author frank
 *
 */
public class TempFileUtils {

	/**
	 * 获取文件存储路径<br/>
	 * 以日期为动态根目录
	 * 
	 * @param basePath 根路径
	 * @param dirName  文件夹名[可自定义多级]
	 * @param fileName 文件名
	 * @return
	 */
	public static String genDateFilePath(String basePath, String dirName, String fileName) {
		basePath = (basePath.endsWith(File.separator) ? basePath : basePath + File.separator);
		String date = DateUtils.toString(new Date(), "yyyyMMdd");
		String fullPath = basePath + date + File.separator + dirName + File.separator + fileName;
		return fullPath;
	}
	
	/**
	 * 获取文件存储路径<br/>
	 * 以 static 为静态根目录
	 * 
	 * @param basePath 根路径
	 * @param dirName  文件夹名[可自定义多级]
	 * @param fileName 文件名
	 * @return
	 */
	public static String genStaticFilePath(String basePath, String dirName, String fileName) {
		basePath = (basePath.endsWith(File.separator) ? basePath : basePath + File.separator);
		String fullPath = basePath + "static" + File.separator + dirName + File.separator + fileName;
		return fullPath;
	}

}
