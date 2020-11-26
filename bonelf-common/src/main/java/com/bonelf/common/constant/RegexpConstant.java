package com.bonelf.common.constant;

/**
 * 正则表达式常量
 * @author qingcong
 * @date 2020-03-26
 **/
public interface RegexpConstant {

	/**
	 * 6-16位数字或字母  必须既有字母又有数字
	 */
	String NUMBERS_AND_LETTERS = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

	/**
	 * 电话号码校验（不包含港澳台手机号）
	 */
	String VALIDATE_PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[0-9]|17[0135678]|18[0-9]|19[89])\\d{8}$";
	String VALIDATE_MAIL = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-z]{2,}$";

	/**
	 * 地址校验
	 */
	String VALIDATE_URL = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

	/**
	 * 金钱校验
	 */
	String VALIDATE_MONEY = "(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)";

	/**
	 * 6-18位纯数字 不能以0开头
	 */
	String VALIDATE_NUMBERS_SIX_THEN_EIGHTEEN = "^[1-9][0-9]{5,18}$";

	/**
	 * 身份证校验
	 */
	String ID_NUMBER = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

	/**
	 * 银行卡号校验
	 */
	String BANK_CRAD = "^(\\d{16}|\\d{18}|\\d{17}|\\d{15}|\\d{14}|\\d{19})$";

	/**
	 * 替换手机号中间4未
	 */
	String PHONE_REPLACE = "(\\d{3})\\d{4}(\\d{4})";

	String PHONE_LENGTH = "$1****$2";
	/**
	 * 匹配键盘上所有可见符号
	 */
	String ALL_SIGN_ON_THE_KEYBOARD = "((?=[\\x21-\\x7e]+)[^A-Za-z0-9])";
	/**
	 * 电话号码
	 */
	String VALIDATE_TEL = "0\\d{2,3}-[1-9]\\d{6,7}";
}
