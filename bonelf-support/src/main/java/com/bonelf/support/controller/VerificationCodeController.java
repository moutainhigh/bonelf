package com.bonelf.support.controller;

import com.bonelf.common.domain.Result;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.support.constant.CacheConstant;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码接口
 * @author bonelf
 * @date 2019-10-27
 **/
@Slf4j
@Controller
@Api(value = "验证码API", tags = {"验证码"})
@ApiIgnore
@RequestMapping("/verificationCode")
public class VerificationCodeController {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private Producer producer;

	/**
	 * 获取验证码
	 */
	@GetMapping("/getImage")
	@ApiOperation(value = "获取验证码")
	public void getImage(HttpServletResponse response,
						 @ApiParam(value = "业务类型", required = true) @RequestParam String bizType,
						 @ApiParam(value = "业务唯一编号，如账号", required = true) @RequestParam String bizCode) throws Exception {
		// create the text for the image
		String capText = producer.createText();
		// store the text in the session
		//session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		// create the image with the text
		BufferedImage bi = producer.createImage(capText);
		//String verifyToken = UUID.randomUUID().toString().replaceAll("-", "");
		// 缓存到Redis
		redisUtil.set(String.format(CacheConstant.VERIFY_CODE, bizType, bizCode), capText, 5 * 60);
		//response.setHeader("verifyKey", verifyToken);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		ServletOutputStream outputStream = response.getOutputStream();
		ImageIO.write(bi, "jpeg", outputStream);
	}

	/**
	 * 获取图片Base64验证码
	 */
	@GetMapping("/getBase64Image")
	@ResponseBody
	@ApiOperation(value = "获取图片Base64验证码")
	public Result<Map<String, Object>> getCode(HttpServletResponse response,
											   @ApiParam(value = "业务类型", required = true) @RequestParam String bizType,
											   @ApiParam(value = "业务唯一编号，如账号", required = true) @RequestParam String bizCode) throws Exception {
		// create the text for the image
		String capText = producer.createText();
		// store the text in the session
		//session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
		// create the image with the text
		BufferedImage bi = producer.createImage(capText);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "jpeg", outputStream);
		// 将图片转换成base64字符串
		String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		// 生成当前验证码会话token
		//String verifyToken = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String, Object> map = new HashMap<>(2);
		map.put("image", "data:image/png;base64," + base64);
		//map.put("key", bizCode);
		// 缓存到Redis
		redisUtil.set(String.format(CacheConstant.VERIFY_CODE, bizType, bizCode), capText, 5 * 60);
		return Result.ok(map);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(value = HttpMessageNotWritableException.class)
	public void errorHandler(HttpMessageNotWritableException e) {
		//便于调试
		log.warn("HttpMessageNotWritableException", e);
	}

}
