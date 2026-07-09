# giants-web
[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-web.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.github.vencent-lu/giants-web)

WEB 公共工具类封装，不绑定任何 MVC 框架，可单独使用。提供请求上下文（基于 `ThreadLocal`）、
Session/Header/客户端 IP 的简化读取，以及一个通用的 Filter 基类。

## com.giants.web.filter.WebFilter

请求上下文过滤器，借助 `ThreadLocal` 保存当前线程的 `HttpServletRequest`，使业务代码在任意位置都能获取当前请求，
而无需层层传递 request 参数。请求进入时把 request 存入 `ThreadLocal`，请求结束后（`finally`）自动清空，避免线程复用导致的内存泄漏与脏数据。

使用 `com.giants.web.utils.WebUtils` 前，必须先在 `web.xml` 中注册此 Filter：

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

静态方法：
* getRequest
    * 说明：获取当前线程的 `HttpServletRequest`。

## com.giants.web.utils.WebUtils

简化 `HttpServletRequest` 调用的工具类，需要先初始化 `com.giants.web.filter.WebFilter` 才能拿到 `HttpServletRequest` 上下文，
目前提供以下方法：
* getSessionAttribute
    * 说明：获取 Session 属性。
* setSessionAttribute
    * 说明：设置 Session 属性。
* getHeader
    * 说明：获取请求头。
* getRequest
    * 说明：获取当前线程的 `HttpServletRequest`。
* getIpAddress
    * 说明：获取客户端真实 IP。依次尝试 `X-Forwarded-For`、`Proxy-Client-IP`、`WL-Proxy-Client-IP`、`HTTP_CLIENT_IP`、
      `HTTP_X_FORWARDED_FOR`，最后回退到 `request.getRemoteAddr()`；对多级代理的 `X-Forwarded-For`（逗号分隔）取第一个有效 IP。
* getIpAddress(HttpServletRequest request)
    * 说明：从指定 request 获取客户端真实 IP。

## com.giants.web.filter.AbstractFilter

所有 Filter 的基类，自定义 Filter 时继承它并实现 `doFilter(HttpServletRequest, HttpServletResponse, FilterChain)` 即可。
提供以下能力：

* 防重入：同一个 Filter 在一次请求中被重复调用（如 forward 到 JSP）时不重复执行业务逻辑。
* 参数查找：`findInitParameter(name, default)` 先查 Filter 的 `init-param`，再查全局 `context-param`，最后用默认值。
* 异常吞噬开关：`eatException` 初始化参数，默认 `true`。
* 请求转储：`dumpRequest(request)` 返回 `方法 + URI + 查询串`，便于日志输出。

---

更多完整的配置与示例，请查看 [使用手册](../docs/使用手册.md#二giants-web-模块)。
