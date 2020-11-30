package com.bonelf.support.controller;

import com.bonelf.cicada.enums.EnumFactory;
import com.bonelf.cicada.util.CipherCryptUtil;
import com.bonelf.common.config.property.BonelfProperty;
import com.bonelf.common.constant.AuthConstant;
import com.bonelf.common.constant.enums.QrCodeTypeEnum;
import com.bonelf.common.core.exception.BonelfException;
import com.bonelf.common.core.exception.enums.CommonBizExceptionEnum;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.redis.RedisUtil;
import com.bonelf.support.constant.CacheConstant;
import com.bonelf.support.constant.QrCodeConstant;
import com.bonelf.support.core.exception.SupportExceptionEnum;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码接口（有期限）
 * @author bonelf
 * @date 2019-10-27
 **/
@Slf4j
@Controller
@Api(value = "二维码API", tags = {"二维码"})
@ApiIgnore
@RequestMapping("/qrcode")
public class QrCodeController {
	@Autowired
	private BonelfProperty bonelfProperty;
	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 获取验证码
	 */
	@GetMapping("/getImage")
	@ApiOperation(value = "获取二维码")
	public void getImage(HttpServletResponse response,
						 @ApiParam(value = "业务类型", required = true) @RequestParam("b") String bizType,
						 @ApiParam(value = "某个主体编号", required = true) @RequestParam("u") String uniqueId,
						 @ApiParam(value = "过期时间/s") @RequestParam(value = "t", defaultValue = CacheConstant.QR_CODE_EXPIRE_TIME + "", required = false) Long expireTime,
						 @ApiParam(value = "宽") @RequestParam(value = "w", defaultValue = QrCodeConstant.QR_CODE_DEFAULT_WIDTH + "", required = false) Integer width,
						 @ApiParam(value = "高") @RequestParam(value = "h", defaultValue = QrCodeConstant.QR_CODE_DEFAULT_HEIGHT + "", required = false) Integer height) throws Exception {
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		String key = String.format(CacheConstant.QR_CODE_PREFIX, bizType, uniqueId);
		redisUtil.set(key, this.getData(uniqueId, EnumFactory.getByCode(bizType, QrCodeTypeEnum.class)), Math.min(expireTime, CacheConstant.QR_CODE_MAX_EXPIRE_TIME));
		String cipherEncryptedCode = CipherCryptUtil.encrypt(bizType + ";" + uniqueId, AuthConstant.FRONTEND_PASSWORD_CRYPTO, AuthConstant.FRONTEND_SALT_CRYPTO);
		BitMatrix bitMatrix = qrCodeWriter.encode(bonelfProperty.getBaseUrl() + "/bonelf/support/qrcode/data/" + cipherEncryptedCode,
				BarcodeFormat.QR_CODE, Math.min(width, QrCodeConstant.QR_CODE_MAX_WIDTH), Math.min(height, QrCodeConstant.QR_CODE_MAX_HEIGHT));
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
	}

	/**
	 * 通过Feign请求服务获取二维码数据
	 * @param uniqueId
	 * @param qrCodeType
	 * @return
	 */
	private String getData(String uniqueId, QrCodeTypeEnum qrCodeType) {
		switch (qrCodeType) {
			case EXAMPLE:
				return uniqueId;
			default:
				throw new BonelfException(CommonBizExceptionEnum.REQUEST_INVALIDATE);
		}
	}

	/**
	 * 获取图片Base64验证码
	 */
	@GetMapping("/getBase64Image")
	@ResponseBody
	@ApiOperation(value = "获取Base64二维码")
	public Result<Map<String, Object>> getCode(@ApiParam(value = "业务类型", required = true) @RequestParam("b") String bizType,
											   @ApiParam(value = "某个主体编号", required = true) @RequestParam("u") String uniqueId,
											   @ApiParam(value = "过期时间/s") @RequestParam(value = "t", defaultValue = CacheConstant.QR_CODE_EXPIRE_TIME + "", required = false) Long expireTime,
											   @ApiParam(value = "宽") @RequestParam(value = "w", defaultValue = QrCodeConstant.QR_CODE_DEFAULT_WIDTH + "", required = false) Integer width,
											   @ApiParam(value = "高") @RequestParam(value = "h", defaultValue = QrCodeConstant.QR_CODE_DEFAULT_HEIGHT + "", required = false) Integer height) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		String key = String.format(CacheConstant.QR_CODE_PREFIX, bizType, uniqueId);
		//每次刷新
		redisUtil.set(key, this.getData(uniqueId, EnumFactory.getByCode(bizType, QrCodeTypeEnum.class)), Math.min(CacheConstant.QR_CODE_MAX_EXPIRE_TIME, expireTime));
		String cipherEncryptedCode = CipherCryptUtil.encrypt(bizType + ";" + uniqueId, AuthConstant.FRONTEND_PASSWORD_CRYPTO, AuthConstant.FRONTEND_SALT_CRYPTO);
		BitMatrix bitMatrix = qrCodeWriter.encode(bonelfProperty.getBaseUrl() + "/bonelf/support/qrcode/data/" + cipherEncryptedCode,
				BarcodeFormat.QR_CODE, Math.min(width, QrCodeConstant.QR_CODE_MAX_WIDTH), Math.min(height, QrCodeConstant.QR_CODE_MAX_HEIGHT));
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		// 将图片转换成base64字符串
		String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
		Map<String, Object> map = new HashMap<>(2);
		map.put("image", "data:image/png;base64," + base64);
		// 缓存到Redis
		return Result.ok(map);
	}

	/**
	 * 获取验证码
	 * FIXME 不加 @ResponseBody 重定向到/error
	 */
	//@ResponseBody
	@GetMapping("/data/{code}")
	@ApiOperation(value = "获取二维码数据")
	public Object getData(@RequestHeader(value = "qrFlag", required = false) String qrFlag, HttpServletResponse response,
						  @ApiParam(value = "编码", required = true) @PathVariable("code") String cipherEncryptedCode) throws Exception {
		if (qrFlag == null) {
			//response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
			// 重定向到项目网页首页或下载页面
			return "redirect:/download.html";
		}
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE+";charset=utf-8");
		String deEntry = CipherCryptUtil.decrypt(cipherEncryptedCode, AuthConstant.FRONTEND_PASSWORD_CRYPTO, AuthConstant.FRONTEND_SALT_CRYPTO);
		String[] strings = deEntry.split(";");
		if (strings.length != 2) {
			return Result.error(SupportExceptionEnum.QRCODE_EXPIRE);
		}
		String key = String.format(CacheConstant.QR_CODE_PREFIX, strings[0], strings[1]);
		Object obj = redisUtil.get(key);
		if (obj == null) {
			return Result.error(SupportExceptionEnum.QRCODE_EXPIRE);
		}
		QrCodeTypeEnum enums = EnumFactory.getByCode(strings[0], QrCodeTypeEnum.class);
		switch (enums) {
			case EXAMPLE:
				//取过值二维码过期
				redisUtil.del(key);
				break;
			default:
				// pass
		}
		return Result.ok(obj);
	}

}
