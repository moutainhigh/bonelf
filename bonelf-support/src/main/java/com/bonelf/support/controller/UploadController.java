package com.bonelf.support.controller;

import com.bonelf.common.domain.Result;
import com.bonelf.support.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 第三方和公用接口
 * </p>
 * @author bonelf
 * @since 2020/10/30 9:29
 */
@RestController
@RequestMapping("/upload")
@Slf4j
@Api(tags = "上传接口")
public class UploadController {
	@Autowired
	private FileService fileService;

	@ApiOperation("上传文件到服务器")
	@RequestMapping(value = "/v1/file", method = RequestMethod.POST)
	public Result<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
		//上传文件大小为1000条数据
		if (file.getSize() > 1024 * 1024 * 10) {
			log.error("upload | 上传失败: 文件大小超过10M，文件大小为：{}", file.getSize());
			return Result.error("上传失败: 文件大小不能超过10M!");
		}
		String imgUrl = fileService.uploadFile(file);
		Map<String, Object> resp = new HashMap<>(1);
		resp.put("imgUrl", imgUrl);
		return Result.ok(resp);
	}

	/**
	 * 上传文件到OSS
	 * @param file
	 * @return
	 */
	@ApiOperation("上传文件到服务器")
	@RequestMapping(value = "/v1/ossFile", method = RequestMethod.POST)
	public Result<?> uploadOssFile(@RequestParam MultipartFile file) throws IOException {
		//上传文件大小为1000条数据
		if (file.getSize() > 1024 * 1024 * 10) {
			log.error("upload | 上传失败: 文件大小超过10M，文件大小为：{}", file.getSize());
			return Result.error("上传失败: 文件大小不能超过10M!");
		}
		String imgUrl = fileService.uploadOssFile(file);
		Map<String, Object> resp = new HashMap<>(1);
		resp.put("imgUrl", imgUrl);
 		return Result.ok(resp);
	}
}
