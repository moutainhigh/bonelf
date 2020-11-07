package com.bonelf.support;

import com.bonelf.support.config.WsConfig;
import com.bonelf.support.config.property.WsProperty;
import com.bonelf.support.core.websocket.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.PreDestroy;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * websocket  server
 * 注册Bean {@link WsConfig#wsServer()} 主要想注入getInstance的对象
 * 不使用getInstance方式使用@Component注入默认构造器效果一样
 * 使用了getInstance那么以后getInstance也是使用同一个bean，效果和注入、SpringContextUtil获取bean一样
 * @see com.bonelf.support.event.ApplicationStartEventListener#onApplicationEvent(ContextRefreshedEvent) 启动
 * @author bonelf
 * @date 2020-10-18 20:31:32
 */
@Slf4j
public class WsServer {
	@Autowired
	private WsProperty wsProperty;
	@Autowired
	private WebSocketServerHandler webSocketServerHandler;

	// implements WebServer 不能像想象中那样
	//@Override
	//public void start() throws WebServerException {
	//	try {
	//		getInstance().run();
	//	} catch (InterruptedException e) {
	//		e.printStackTrace();
	//		throw new WebServerException(e.getMessage(), e);
	//	}
	//}
	//
	//@Override
	//public void stop() throws WebServerException {
	//	getInstance().destroy();
	//}
	//
	//@Override
	//public int getPort() {
	//	return wsProperty.getPort();
	//}

	/**
	 * 单例静态内部类
	 */
	public static class SingletionWSServer {
		static final WsServer instance = new WsServer();
	}

	public static WsServer getInstance() {
		return SingletionWSServer.instance;
	}

	public void run() throws InterruptedException {
		log.info("WsServer Starting...");
		EventLoopGroup bossGroup = new NioEventLoopGroup();

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap sb = new ServerBootstrap();
			//标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
			sb.option(ChannelOption.SO_BACKLOG, wsProperty.getSoBackLog());
			// 绑定线程池
			sb.group(group, bossGroup)
					// 指定使用的channel
					.channel(NioServerSocketChannel.class)
					// 绑定监听端口
					.localAddress(wsProperty.getPort())
					.childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							if (wsProperty.getSsl().getPath() != null) {
								SSLContext sslContext = createSSLContext(wsProperty.getSsl().getType(), wsProperty.getSsl().getPath(), wsProperty.getSsl().getPassword());
								//SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
								SSLEngine engine = sslContext.createSSLEngine();
								engine.setUseClientMode(false);
								engine.setNeedClientAuth(false);
								ch.pipeline().addLast(new SslHandler(engine));
							}

							//websocket协议本身是基于http协议的，所以这边也要使用http解编码器
							ch.pipeline().addLast(new HttpServerCodec());
							//以块的方式来写的处理器
							ch.pipeline().addLast(new ChunkedWriteHandler());
							//解析Http POST请求 所以注册后也支持Feign请求 在handler中处理
							ch.pipeline().addLast(new HttpObjectAggregator(8192));
							ch.pipeline().addLast(new WebSocketServerProtocolHandler(wsProperty.getContextPath(),
									wsProperty.getSubprotocols(),
									wsProperty.getAllowExtensions(),
									wsProperty.getMaxFrameSize()));
							ch.pipeline().addLast(webSocketServerHandler);
							//ch.pipeline().addLast(new WebSocketServerProtocolHandler(wsProperty.getContextPath()));
						}
					});
			// 服务器异步创建绑定
			ChannelFuture cf = sb.bind().sync();
			log.debug("WsServer listening：" + cf.channel().localAddress());
			// 关闭服务器通道
			cf.channel().closeFuture().sync();
		} finally {
			// 释放线程池资源
			group.shutdownGracefully().sync();
			bossGroup.shutdownGracefully().sync();
		}
	}

	@PreDestroy
	public void destroy() {
		log.info("WsServer Closing");

		log.info("WsServer Closed");
	}

	public static SSLContext createSSLContext(String type, String path, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(type);
		// 证书存放地址
		InputStream ksInputStream = new FileInputStream(path);
		ks.load(ksInputStream, password.toCharArray());
		//KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
		kmf.init(ks, password.toCharArray());
		//SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), null, null);
		return sslContext;
	}
}