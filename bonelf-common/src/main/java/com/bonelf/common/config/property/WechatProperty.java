package com.bonelf.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 微信小程序配置
 * </p>
 * @author bonelf
 * @since 2020/8/31 16:40
 */
@Component
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatProperty {
    /**
     * secret
     */
    private String secret;
    /**
     * appid
     */
    private String mchId;
    /**
     * 商户秘钥
     */
    private String mchKey;
    /**
     * notifyUrl
     */
    private String notifyUrl;

    /**
     * 商户API证书 退款用
     */
    private String keyPath;
    /**
     * 小程序
     */
    private MiniProperty mini = new MiniProperty();
    /**
     * 公众号
     */
    private MpProperty mp = new MpProperty();
    /**
     * 移动应用
     */
    private AppProperty app = new AppProperty();

    @Data
    public static class MiniProperty {
        /**
         * 小程序appId
         * 获取地址 https://mp.weixin.qq.com
         */
        private String appid;
    }

    @Data
    public static class AppProperty {
        /**
         * app应用appid 移动应用支付用
         * 获取地址 https://open.weixin.qq.com
         */
        private String appid;
    }

    @Data
    public static class MpProperty {
        /**
         * 公众账号appid h5支付、推送用
         * 获取地址 https://mp.weixin.qq.com
         */
        private String appid;
    }
}
