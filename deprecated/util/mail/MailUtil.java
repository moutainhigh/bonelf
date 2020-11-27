/*
 * Copyright (c) 2020. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.bonelf.common.util.mail;

import com.bonelf.common.config.property.BonelfProperty;
import com.bonelf.common.config.property.MailProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送验证码工具
 * 使用huttol代替
 * @author chuanfu
 * @version v1.0
 * @date 2019-10-10
 */
@Component
@Deprecated
public class MailUtil {
	@Autowired
	private MailProperty mailProperty;
	@Autowired
	private BonelfProperty bonelfProperty;

	public void sendMail(String email, String content) {
		MailOperation operation = new MailOperation();
		String user = mailProperty.getUsername();
		String password = mailProperty.getPassword();
		String host = mailProperty.getSmtp();
		String from = user;
		String res;

		{
			try {
				res = operation.sendMail(user, password, host, from, email,
						bonelfProperty.getAppName(), content);
				//System.out.println(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendVerifyMail(String email, String code) {
		//邮箱内容 TODO 可引入FreeMarker
		String sb = "<!DOCTYPE>" + "<div bgcolor='#f1fcfa' style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>" + "<div style='width:950px;font-family:arial;'>欢迎使用我们的" +
				bonelfProperty.getAppName() + "，您本次的验证码为：<br/><h2 style='color:green'>" +
				code + "</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>来自" + bonelfProperty.getAppName() + "</div>" + "</div>";
		sendMail(email, sb);
	}
}
