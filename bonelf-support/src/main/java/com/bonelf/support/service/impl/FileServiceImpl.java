package com.bonelf.support.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSSClient;
import com.bonelf.common.config.property.BonelfProperty;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.support.config.property.OssProperty;
import com.bonelf.support.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private OssProperty ossProperty;
	@Autowired
	private BonelfProperty bonelfProperty;

	@Override
	public String uploadFile(MultipartFile file) {
		String staticPathName = "static";
		String typeDirName;
		if (isImage(file)) {
			typeDirName = "image";
		} else {
			typeDirName = "resource";
		}
		//创建目录
		String dateDirName = DateUtil.today();
		String dirName = staticPathName + "/" + typeDirName + "/" + dateDirName;
		File dir = new File(dirName);
		if (!dir.exists()) {
			mkdir(dir);
			//boolean f = dir.mkdir();
			//if (!f) {
			//	throw new BonelfException("创建文件目录失败");
			//}
		}
		//名称合法化
		String validFileNameTmp = StrUtil.nullToDefault(file.getOriginalFilename(), String.valueOf(System.currentTimeMillis()));
		try {
			validFileNameTmp = URLEncoder.encode(validFileNameTmp, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("文件上传编码失败name:{}", validFileNameTmp);
			e.printStackTrace();
		}
		String validFileName = validFileNameTmp.replace("%", "");
		//创建文件
		File targetFile = new File(dirName + "/" + validFileName);
		if (!targetFile.exists()) {
			createFile(targetFile);
		} else {
			validFileName = RandomUtil.randomString(5) + validFileName;
			targetFile = new File(dirName + "/" + validFileName);
			if (!targetFile.exists()) {
				createFile(targetFile);
			} else {
				throw new BonelfException("系统错误，请重新上传");
			}
		}
		//上传文件
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(targetFile);
			IOUtils.copy(file.getInputStream(), fileOutputStream);
			log.info("------>>>>>>uploaded a file successfully!<<<<<<------");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BonelfException("文件上传失败");
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					log.warn("输出流关闭失败", e);
				}
			}
		}
		return bonelfProperty.getBaseUrl() + typeDirName + "/" + dateDirName + "/" + validFileName;
	}


	@Override
	public String uploadOssFile(MultipartFile file) throws IOException {
		String fileStr;
		try {
			fileStr = DateUtil.today() + "/" + URLEncoder.encode(StrUtil.nullToEmpty(file.getOriginalFilename()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("文件上传编码失败name:{}", file.getOriginalFilename());
			e.printStackTrace();
			fileStr = DateUtil.today() + "/" + file.getOriginalFilename();
		}
		OSSClient ossClient = new OSSClient(ossProperty.getEndpoint(), ossProperty.getAccessKeyId(), ossProperty.getAccessSecret());
		ossClient.putObject(ossProperty.getBucketName(), fileStr, file.getInputStream());
		ossClient.shutdown();
		return ossProperty.getBucketUrl() + "/" + fileStr;
	}

	/**
	 * 一级一级创建目录，理论上不用这样
	 * @param dir
	 */
	private static void mkdir(File dir) {
		while (dir.getParentFile() != null && !dir.getParentFile().exists()) {
			mkdir(dir.getParentFile());
		}
		if (!dir.exists()) {
			boolean f = dir.mkdir();
			if (!f) {
				throw new BonelfException("创建文件目录失败");
			}
		}
	}

	private boolean isImage(MultipartFile file) {
		if (file.getContentType() != null) {
			String type = file.getContentType().split("/")[0];
			return "image".equals(type);
		}
		return false;
	}

	private void createFile(File targetFile) {
		boolean f;
		try {
			f = targetFile.createNewFile();
		} catch (IOException e) {
			throw new BonelfException("创建文件失败");
		}
		if (!f) {
			throw new BonelfException("创建文件失败");
		}
	}
}
