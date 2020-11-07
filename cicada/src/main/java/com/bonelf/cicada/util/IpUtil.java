package com.bonelf.cicada.util;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtil {

	/**
	 * 获得Wlan局域网IP
	 * 并不保证一定是WlanIP，因为是自己根据特征判断而不是系统方法
	 * @return
	 */
	public static String getWlanV4Ip() {
		Enumeration<NetworkInterface> networkinterface;
		try {
			networkinterface = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}
		boolean wlanFlag = false;
		while (networkinterface.hasMoreElements()) {
			NetworkInterface networkInterface = networkinterface.nextElement();
			Enumeration<InetAddress> address = networkInterface.getInetAddresses();
			while (address.hasMoreElements()) {
				InetAddress ip = address.nextElement();
				//wlanFlag 下一个就是wlan ipv4地址、或者不是.1结尾并且是192.168的是wlan地址
				if (wlanFlag || (ip.isSiteLocalAddress() && !ip.getHostAddress().endsWith(".1"))) {
					return ip.getHostAddress();
				}
				wlanFlag = ip.getHostAddress().contains("wlan1"); // || ip.getHostAddress().contains("wifi")
			}
		}
		return null;
	}

	/**
	 * 获取本机的内网ip地址
	 * @return
	 * @throws SocketException
	 */
	public static String getInnetIp() throws SocketException {
		// 本地IP，如果没有配置外网IP则返回它
		String localip = null;
		// 外网IP
		String netip = null;
		Enumeration<NetworkInterface> netInterfaces;
		netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip;
		// 是否找到外网IP
		boolean finded = false;
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress()
						&& !ip.isLoopbackAddress()
						&& !ip.getHostAddress().contains(":")) {
					// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress()
						&& !ip.isLoopbackAddress()
						&& !ip.getHostAddress().contains(":")) {
					// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * 获取本机的外网ip地址
	 * @return
	 */
	public static String getV4IP() {
		String ip = "";
		String chinaz = "http://ip.chinaz.com";

		StringBuilder inputLine = new StringBuilder();
		String read;
		URL url;
		HttpURLConnection urlConnection;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection)url.openConnection();
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
			while ((read = in.readLine()) != null) {
				inputLine.append(read).append("\r\n");
			}
			//System.out.println(inputLine.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Pattern p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>");
		Matcher m = p.matcher(inputLine.toString());
		if (m.find()) {
			ip = m.group(1);
			//System.out.println(ipstr);
		}
		return ip;
	}


	/**
	 * 解析ip地址
	 *
	 * 设置访问地址为http://ip.taobao.com/service/getIpInfo.php
	 * 设置请求参数为ip=[已经获得的ip地址]
	 * 设置解码方式为UTF-8
	 * @param content 请求的参数 格式为：ip=192.168.1.101
	 * @param encoding 服务器端请求编码。如GBK,UTF-8等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getAddresses(String content, String encoding) throws UnsupportedEncodingException {
		//设置访问地址
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = getResult(urlStr, content, encoding);
		if (returnStr != null) {
			// 处理返回的省市区信息
			// System.out.println(returnStr);
			String[] temp = returnStr.split(",");
			if (temp.length < 3) {
				return "0";// 无效IP，局域网测试
			}

			String country = ""; //国家
			String area = ""; //地区
			String region = ""; //省份
			String city = ""; //市区
			String county = ""; //地区
			String isp = ""; //ISP公司
			for (int i = 0; i < temp.length; i++) {
				switch (i) {
					case 2:
						country = (temp[i].split(":"))[1].replaceAll("\"", "");
						country = URLDecoder.decode(country, encoding);// 国家
						break;
					case 3:
						area = (temp[i].split(":"))[1].replaceAll("\"", "");
						area = URLDecoder.decode(area, encoding);// 地区
						break;
					case 4:
						region = (temp[i].split(":"))[1].replaceAll("\"", "");
						region = URLDecoder.decode(region, encoding);// 省份
						break;
					case 5:
						city = (temp[i].split(":"))[1].replaceAll("\"", "");
						city = URLDecoder.decode(city, encoding);// 市区
						if ("内网IP".equals(city)) {
							return "地址为：内网IP";
						}
						break;
					case 6:
						county = (temp[i].split(":"))[1].replaceAll("\"", "");
						county = URLDecoder.decode(county, encoding);// 地区
						break;
					case 7:
						isp = (temp[i].split(":"))[1].replaceAll("\"", "");
						isp = URLDecoder.decode(isp, encoding); // ISP公司
						break;
				}
			}
			return "地址为：" + country + "," + region + "省," + city + "市," + county + "地区," + area + "地址," + "ISP公司：" + isp;
		}
		return null;
	}


	/**
	 * 访问目标地址并获取返回值
	 * @param urlStr 请求的地址
	 * @param content 请求的参数 格式为：ip=192.168.1.101
	 * @param encoding 服务器端请求编码。如GBK,UTF-8等
	 * @return
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		URL url;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection)url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(33000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}




	/**
	 * 测试方法
	 * 获取本机的内网ip，外网ip和指定ip的地址
	 * @param args
	 */
	public static void main(String[] args) throws SocketException {
		System.out.println(getWlanV4Ip());

		////step1.获得内网ip和外网ip，并输出到控制台
		//String ip1 = "";
		//try {
		//	ip1 = getInnetIp(); //局域网的ip地址，比如：192.168.1.101
		//} catch (SocketException e1) {
		//	e1.printStackTrace();
		//}
		//System.out.println("内网ip:" + ip1);
		//String ip2 = getV4IP(); //用于实际判断地址的ip
		//System.out.println("外网ip:" + ip2);
		////step2.根据外网ip地址，得到市级地理位置
		//String address = "";
		//try {
		//	address = getAddresses("ip=" + ip2, "utf-8");
		//} catch (UnsupportedEncodingException e) {
		//	e.printStackTrace();
		//}
		//// 输出地址，比如：中国，山东省，济南市，联通
		//System.out.println("您的" + address);
		//System.out.println("******************************");
		//System.out.println("请输入想要查询的ip地址(输入exit退出)：");
		//Scanner scan = new Scanner(System.in);
		//String ip;
		//while (!"exit".equals(ip = scan.next())) {
		//	try {
		//		address = getAddresses("ip=" + ip, "utf-8");
		//	} catch (UnsupportedEncodingException e) {
		//		e.printStackTrace();
		//	}
		//	// 输出地址，比如：中国，山东省，济南市，联通
		//	System.out.println(ip + "的" + address);
		//	System.out.println("******************************");
		//	System.out.println("请输入想要查询的ip地址(输入exit退出)：");
		//}
		//scan.close();
		//System.out.println("再见");
	}

}
