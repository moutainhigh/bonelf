# bonelf 小骨 个人项目经验总结

## 介绍

## 项目结构

| 模块 | 名称 | 端口 | |
| --- | --- | --- | --- |
| `cicada` | 工具包 |  | |
| `bonelf-common` | 公用模块 |  | |
| `bonelf-gateway` | 网关 | HTTP 9999 | |
| `bonelf-support` | Netty Websocket服务、定时器等 | HTTP 8800 & WS 8802 | |
| `bonelf-service-order` | 订单服务 | HTTP 8801 | [接口文档]() |
| `bonelf-service-pay` | 支付服务 | HTTP 8806 | [接口文档]() |
| `bonelf-service-product` | 商品服务 | HTTP 8803 | [接口文档]() |
| `bonelf-service-promotion` | 营销服务 | HTTP 8805 | [接口文档]() |
| `bonelf-service-search` | 搜索服务 | HTTP 8807| [接口文档]() |
| `bonelf-service-system` | 管理系统服务 | HTTP 8808 | [接口文档]() |
| `bonelf-service-user` | 用户服务 | HTTP 8804 | [接口文档]() |
| `bonelf-service-test` | 测试服务 | HTTP 8080 | [接口文档]() |

## Q&A
Q：关于为什么不把实体类和VO拆分出单独的模块从而避免模块间Feign请求需要再建实体接受的问题：

A：抽离出来确实不用再担心解耦在调用时出现因为实体类不一致导致的无法解析，导致接口调用失败。但我个人认为既然使用了微服务，那么各模块之间应尽可能避免耦合，各模块用各模块需要的实体，不应同意实体。

## Nacos
下载地址：
[点击下载ZIP（镜像）](https://github-production-release-asset-2e65be.s3.amazonaws.com/137451403/90b68b00-d688-11ea-8e5b-0126ff25179c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20201004%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20201004T151437Z&X-Amz-Expires=300&X-Amz-Signature=4f9425db68308988d24ba947031ddbac59ceda8a8494a306bff5132fcd1c55c5&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=137451403&response-content-disposition=attachment%3B%20filename%3Dnacos-server-1.3.2.zip&response-content-type=application%2Foctet-stream)

#### 启动用法
先将/conf 中的nacos-mysql.sql添加到自己项目的数据库中并修改application.properties 31行(Config Module Related Configurations)的配置

Windows启动：切换到/bin目录 -> .\startup.cmd -m standalone

## 开源项目参考
Jeecg-boot、ruoyi-cloud、Guns、litemall、best-pay

## 开发计划
继承Webserver是否可以替换springboot的web服务？

搜索服务elasticsearch

微信支付宝 支付、退款、支付对账、分账

库存问题

规格问题（下单）

经纬度排序问题

三方登录

二维码登录

OAuth自动刷新token

maven一键部署、使用docker部署（depoly）

zipkin

grafana

kibana

seata

sentired

## 已完成
nacos基本微服务框架功能配置（swagger、多数据源、druid、hystrix、feign、redis、mybatisplus、消息转化器、异常处理advice、spring cache使用redis实现等）

SpringBoot、Netty、redis发布订阅三者的websocket

quartz数据库定时器

数据库字典、枚举字典注解

文件上传至服务器、OSS（七牛云一般前端传，后端维护refreshToken）

swagger-ui 微服务下整合

小程序、账号密码登录

文件上传

规格问题（增删改查）

空字符串转null 注解（并没有实现注解，而是全局设置）

OAuth2

OAuth权限

string 替换文本注解

支持表情注解（valid）

过滤表情 过滤HTML表情（XSS）注解方式实现

加密传输注解

枚举校验注解（valid clazz or enum value）

验证码 图形验证码

二维码

rocketmq（应用订单服务通知商品修改销售数目、定时器发送消息，其他服务处理）

RocketMQ 实现 redis websocket发布订阅

点击量排序问题（需rocketmq）

销售量排序问题（需rocketmq）

## 放弃
CAS单点登录（使用OAuth2.0解决）

shiro （不支持分布式，替换成OAuth2)