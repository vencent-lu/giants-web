# giants-web-parent
[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-web-parent.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.github.vencent-lu/giants-web-parent)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

`giants-web` 是一套面向 Java WEB 层的通用组件封装，目标是把 Controller / Action 代码从繁琐的
Servlet API、JSON 序列化、异常处理和数据校验中解放出来，统一 HTTP 响应格式，让业务代码更聚焦于逻辑本身。

框架分为三个相互独立的模块，可按需引入：

| 模块 | artifactId | 说明 |
| --- | --- | --- |
| [giants-web](web) | `giants-web` | WEB 公共工具类封装（Filter、请求上下文、IP 获取等），无框架依赖 |
| [giants-web-springmvc](springmvc) | `giants-web-springmvc` | Spring MVC 组件封装：统一 JSON 响应、异常解析、FastJson 转换器、参数解析器 |
| [giants-web-struts2](struts2) | `giants-web-struts2` | Struts2 组件封装：JSON Action 基类、CRUD Action、校验与异常拦截器 |

> 完整的功能说明与配置示例请查看 **[使用手册](docs/usage-manual.md)**。

## 特性一览

- **统一响应格式**：所有接口返回统一的 `JsonResult` 结构（`code` / `message` / `data` / `errMsg` / `businessErrorKey`），前端处理更简单。
- **统一异常处理**：基于 `GiantsException` 异常体系，自动将业务异常、数据校验异常映射为对应的错误码与国际化提示信息。
- **数据校验集成**：Spring MVC 侧支持 AOP 校验，Struts2 侧提供校验拦截器，校验失败信息统一输出。
- **参数解析解耦**：通过参数解析器把 Cookie、Session 值直接注入 Controller 方法参数，业务代码不再直接调用 Servlet API。
- **请求上下文**：通过 `ThreadLocal` 在任意位置获取当前 `HttpServletRequest`，简化 Session、Header、客户端 IP 的读取。
- **JSONP 支持**：Spring MVC 侧内置 JSONP 回调封装。
- **Swagger 友好**：`JsonResult` 内置 Swagger 注解，接口文档展示统一响应结构。

## 环境要求

- JDK 1.8+
- 依赖版本（由父 POM 统一管理）：
  - Spring `5.2.25.RELEASE`
  - Struts2 `2.5.33`
  - FastJson `1.2.83`
  - Servlet API `3.0.1`（`provided`）
  - giants-common `1.3.0`

## 快速开始

### 1. 引入依赖

Spring MVC 项目：

```xml
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-web-springmvc</artifactId>
    <version>1.1.11</version>
</dependency>
```

Struts2 项目：

```xml
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-web-struts2</artifactId>
    <version>1.1.11</version>
</dependency>
```

仅需要公共工具类：

```xml
<dependency>
    <groupId>com.github.vencent-lu</groupId>
    <artifactId>giants-web</artifactId>
    <version>1.1.11</version>
</dependency>
```

### 2. 配置 WebFilter（使用请求上下文工具类时）

在 `web.xml` 中注册 `WebFilter`，即可通过 `WebUtils` 在任意位置获取当前请求上下文：

```xml
<filter>
    <filter-name>webFilter</filter-name>
    <filter-class>com.giants.web.filter.WebFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>webFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

```java
HttpServletRequest request = com.giants.web.utils.WebUtils.getRequest();
String ip = com.giants.web.utils.WebUtils.getIpAddress();
```

更多用法（Spring MVC 统一响应配置、Struts2 拦截器栈配置等）见 **[使用手册](docs/使用手册.md)**。

## 统一响应结构（JsonResult）

Spring MVC 与 Struts2 模块最终都会把接口结果封装成统一结构，前端根据 `code` 判断处理分支：

```json
{
  "code": 0,
  "message": "操作成功!",
  "businessErrorKey": null,
  "errMsg": null,
  "data": { }
}
```

错误编码 `code` 取值（来自 `com.giants.common.GiantsConstants`）：

| code | 常量 | 含义 |
| --- | --- | --- |
| 0 | `ERROR_CODE_SUCCESS` | 操作成功 |
| 1 | `ERROR_CODE_NOT_LOGGED_IN` | 没有登录，或没有进行认证 |
| 2 | `ERROR_CODE_DATA_CHECK_FAILURE` | 数据验证失败 |
| 3 | `ERROR_CODE_BUSINESS_EXCEPTION` | 业务操作异常 |
| 4 | `ERROR_CODE_NOT_AUTHORITY` | 没有操作权限 |
| 5 | `ERROR_CODE_DATA_OPERATION_EXCEPTION` | 数据操作异常 |
| 6 | `ERROR_CODE_VIEW_EXCEPTION` | 显示层异常 |
| 7 | `ERROR_CODE_TOKEN_EXCEPTION` | 授权码非法 |
| 127 | `ERROR_CODE_SYSTEM_ERROR` | 系统错误 |

## 从源码构建

```bash
mvn clean install
```

## 模块导航

- [giants-web 模块文档](web/README.md)
- [giants-web-springmvc 模块文档](springmvc/README.md)
- [giants-web-struts2 模块文档](struts2/README.md)
- [完整使用手册](docs/usage-manual.md)

## 许可证

本项目基于 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) 开源。

## 作者

vencent.lu (scsedux@163.com)