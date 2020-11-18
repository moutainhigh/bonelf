package com.bonelf.gateway.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "bonelf")
public class GatewayBonelfProperty {
	private List<String> noAuthUrl;
}
