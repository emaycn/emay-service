package cn.emay.boot.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.emay.boot.config.PropertiesConfig;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.result.Result;

public class CommonUtil {

	private static PropertiesConfig propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfig.class);

	/**
	 * excel文件上传
	 */
	public static Result uploadExcel(MultipartFile file, String typePath, int maxSize) {
		if (file.isEmpty()) {
			return Result.badResult("上传文件不能为空.");
		}
		String fileName = file.getOriginalFilename();
		if (StringUtils.isEmpty(fileName)) {
			return Result.badResult("文件名称不能为空.");
		}
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			return Result.badResult("请上传excle格式文件");
		}
		Long size = file.getSize();
		if (size > maxSize) {
			return Result.badResult("文件过大");
		}
		String dateDay = DateUtils.toString(new Date(), "yyyyMMdd");
		String date = DateUtils.toString(new Date(), "yyyyMMddHHmmss");
		String finalPath = propertiesConfig.getUploadFilePath() + dateDay + "/" + typePath + "/";
		String newFileName = fileName.substring(0, fileName.indexOf(".")) + date + fileName.substring(fileName.indexOf("."));
		File targetFile = new File(finalPath + newFileName);
		try {
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			file.transferTo(targetFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String accessUrl = finalPath + newFileName;
		return Result.rightResult(accessUrl);
	}

}
