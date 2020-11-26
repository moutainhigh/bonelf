package com.bonelf.common.constant;

/**
 * <p>
 * 项目常量
 * </p>
 * @author bonelf
 * @since 2020/10/11 17:27
 */
public interface BonelfConstant {
	/**
	 * 项目名
	 */
	String PROJECT_NAME = "bonelf";

	/**
	 * 默认头像
	 */
	String DEFAULT_AVATAR_PATH = "/image/defaultAvatar.jpg";

	String VERIFY_HTML = "<!DOCTYPE>" + "<div bgcolor='#f1fcfa' style='border:1px solid #d9f4ee; font-size:14px; line-height:22px; color:#005aa0;padding-left:1px;padding-top:5px;   padding-bottom:5px;'><span style='font-weight:bold;'>温馨提示：</span>" + "<div style='width:950px;font-family:arial;'>欢迎使用我们的"+
			"{APPNAME}，您本次的验证码为：<br/><h2 style='color:green'>{CODE}</h2><br/>本邮件由系统自动发出，请勿回复。<br/>感谢您的使用。<br/>来自{APPNAME}</div>" + "</div>";
}
