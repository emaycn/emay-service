package cn.emay.boot.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * 
 * @author Frank
 *
 */
public class FileUploadUtils {

	/**
	 * excel文件上传
	 */
	/**
	 * 文件上传
	 * 
	 * @param file
	 *            文件
	 * @param saveDirPath
	 *            保存目录
	 * @param fileMaxSizeMbLimit
	 *            文件限制大小[单位Mb](为空则不限制)
	 * @param suffixLimits
	 *            文件名后缀限制[可多种后缀](为空则不限制)
	 * @return
	 */
	public static FileUpLoadResult uploadFile(MultipartFile file, String saveDirPath, Integer fileMaxSizeMbLimit, String... suffixLimits) {
		if (file.isEmpty()) {
			return FileUpLoadResult.failResult("上传文件不能为空");
		}
		String fileName = file.getOriginalFilename();
		if (StringUtils.isEmpty(fileName)) {
			return FileUpLoadResult.failResult("文件名称不能为空");
		}
		if (suffixLimits != null) {
			long has = Arrays.stream(suffixLimits).filter(suffix -> fileName.endsWith(suffix) ? true : false).count();
			if (has <= 0) {
				return FileUpLoadResult.failResult("文件格式错误");
			}
		}
		Long size = file.getSize();
		if (fileMaxSizeMbLimit != null) {
			if (size > fileMaxSizeMbLimit.longValue() * 1024L * 1024L) {
				return FileUpLoadResult.failResult("文件过大");
			}
		}
		int pointIndex = fileName.lastIndexOf(".");
		String end = pointIndex > 0 ? fileName.substring(pointIndex) : "";
		String newFileName = UUID.randomUUID().toString().replace("-", "") + end;
		String fullPath = (saveDirPath.endsWith(File.separator) ? saveDirPath : saveDirPath + File.separator) + newFileName;
		File targetFile = new File(fullPath);
		try {
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			file.transferTo(targetFile);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return FileUpLoadResult.sucessResult(fileName, size, fullPath);
	}

	/**
	 * 文件上传结果
	 * 
	 * @author Frank
	 *
	 */
	public static class FileUpLoadResult {
		/**
		 * 是否成功
		 */
		private boolean success;
		/**
		 * 失败原因
		 */
		private String errorMessage;
		/**
		 * 原文件名
		 */
		private String fileName;
		/**
		 * 文件大小
		 */
		private Long fileSize;
		/**
		 * 保存路径
		 */
		private String saveFilePath;

		/**
		 * 失败的结果
		 * 
		 * @param errorMessage
		 *            失败原因
		 * @return
		 */
		public static FileUpLoadResult failResult(String errorMessage) {
			return new FileUpLoadResult(false, null, null, null, errorMessage);
		}

		/**
		 * 成功的结果
		 * 
		 * @param fileName
		 *            原文件名
		 * @param fileSize
		 *            文件大小
		 * @param saveFilePath
		 *            保存路径
		 * @return
		 */
		public static FileUpLoadResult sucessResult(String fileName, Long fileSize, String saveFilePath) {
			return new FileUpLoadResult(true, fileName, fileSize, saveFilePath, null);
		}

		public FileUpLoadResult() {

		}

		public FileUpLoadResult(boolean success, String fileName, Long fileSize, String saveFilePath, String errorMessage) {
			this.success = success;
			this.fileName = fileName;
			this.fileSize = fileSize;
			this.saveFilePath = saveFilePath;
			this.errorMessage = errorMessage;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getSaveFilePath() {
			return saveFilePath;
		}

		public void setSaveFilePath(String saveFilePath) {
			this.saveFilePath = saveFilePath;
		}

		public Long getFileSize() {
			return fileSize;
		}

		public void setFileSize(Long fileSize) {
			this.fileSize = fileSize;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}

}
