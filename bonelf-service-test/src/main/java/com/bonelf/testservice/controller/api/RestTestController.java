package com.bonelf.testservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.bonelf.common.domain.Result;
import com.bonelf.common.util.BaseApiController;
import com.bonelf.testservice.client.OrderFeignClient;
import com.bonelf.testservice.domain.dto.TestConverterDTO;
import com.bonelf.testservice.domain.vo.TestConverterVO;
import com.bonelf.testservice.domain.vo.TestDictVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Api(tags = {"测试接口"})
@RestController
@RequestMapping("/test")
//@DefaultProperties( threadPoolKey="xxx" )
public class RestTestController extends BaseApiController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private OrderFeignClient orderFeignClient;

	@ApiOperation(value = "testUser")
	@GetMapping("/testUser")
	public Result<?> testUser() {
		System.out.println("User:" + JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication()));
		return Result.ok(null);
	}

	@ApiOperation(value = "testConverter")
	@GetMapping("/testConverter")
	public Result<TestConverterVO> testConverter() {
		return Result.ok(new TestConverterVO());
	}

	@ApiOperation(value = "testConverter")
	@PostMapping("/testConverter")
	public Result<TestConverterDTO> testConverterPost(@RequestBody TestConverterDTO testConverterDTO) {
		return Result.ok(testConverterDTO);
	}

	/**
	 * <p>
	 * restTemplate 方法调用服务测试
	 * </p>
	 * @author bonelf
	 * @since 2020/10/5 21:57
	 */
	@ApiOperation(value = "restTemplateTest")
	@GetMapping("/restTemplate")
	public String restTemplateTest() {
		Map<String, Object> params = new HashMap<>(1);
		params.put("orderId", "123");
		return restTemplate.getForObject("http://order/productOrder/getOrderById" + "?orderId={orderId}",
				String.class,
				params);
	}

	/**
	 * <p>
	 * feign请求测试
	 * </p>
	 * @author bonelf
	 * @since 2020/10/5 21:57
	 */
	@GetMapping("/orderFeign")
	public String orderFeign() {
		return orderFeignClient.getProductOrderById("123");
	}

	@ApiOperation(value = "testDict")
	@GetMapping("/testDict")
	public Result<TestDictVO> testDict() {
		return Result.ok(new TestDictVO());
	}

	/**
	 * <p>
	 * feign fallback方法2请求测试（测试时使用@FeignClient(ServiceNameConstant.ORDER_SERVICE)，
	 * 需要App上有@EnableCircuitBreaker注解）
	 * 使用Hystrix 的线程池配置可以设置并发量，即此接口同时调用数量。超过某个数量会进fallback
	 * hystrix.threadpool.default.coreSize = 2
	 * hystrix.threadpool.default.maximumSize = 2
	 * hystrix.threadpool.default.maxQueueSize = -1
	 * groupKey的默认值是使用@HystrixCommand标注的方法所在的类名
	 * commandKey的默认值是@HystrixCommand标注的方法名，即每个方法会被当做一个HystrixCommand
	 * threadPoolKey没有默认值（DefaultProperties）
	 * </p>
	 * @author bonelf
	 * @since 2020/10/5 21:57
	 */
	@GetMapping("/orderFeignMethodFallback")
	@HystrixCommand(fallbackMethod = "orderFeign2Back")
	public String orderFeign2() {
		return orderFeignClient.getProductOrderById("123");
	}

	public String orderFeign2Back() {
		return "orderFeign2Back";
	}

}
