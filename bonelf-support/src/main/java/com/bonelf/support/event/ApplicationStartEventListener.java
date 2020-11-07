package com.bonelf.support.event;

import cn.hutool.core.thread.ThreadUtil;
import com.bonelf.support.WsServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * <p>
 * 也可以使用implements ApplicationRunner
 * </p>
 * @author bonelf
 * @since 2020/10/18 16:16
 */
@Slf4j
//@Component
public class ApplicationStartEventListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private WsServer wsServer;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//ws会堵塞导致后面代码不执行
		ThreadUtil.execAsync(() -> {
			try {
				wsServer.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

}


