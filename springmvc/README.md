# giants-web-springmvc
[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-web-springmvc.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.github.vencent-lu/giants-web-springmvc)

Spring MVC 工具封装组件，适用于 Spring MVC 4.2+ 与 5.x。核心是统一 JSON 响应封装、统一异常解析、
FastJson 转换器与一组参数解析器。统一响应/异常提示依赖 Spring 的 `ResourceBundleMessageSource`。

## com.giants.web.springmvc.json.JsonResult

统一响应对象（内置 Swagger 注解），字段如下：

* code
    * 类型：`byte`，默认 0（成功）。错误编码，前台根据不同编码显示相应提示。
* message
    * 类型：`java.lang.String`。提示信息。
* businessErrorKey
    * 类型：`java.lang.String`。业务异常 KEY，出现业务异常时返回，前端可基于 KEY 做分支处理。
* errMsg
    * 类型：`java.util.Map<String, String>`。数据合法性验证错误信息，仅校验失败时返回。
* data
    * 类型：`java.lang.Object`。Response Body 返回值。

## com.giants.web.springmvc.advice.JsonResultResponseAdvice

统一用 `com.giants.web.springmvc.json.JsonResult` 封装 response 返回结果，并内置 JSONP 支持。
HTTP 状态 200 时 `code=0`、`message=operation.success`；否则 `code=127`、`message=operation.systemerror`。

配置参数：
* uriExcludeList
    * 类型：`java.util.List<String>`
    * 说明：排除 URI 列表，此列表中的请求不会进行统一封装处理，直接返回原始 body。
* jsonpQueryParamNames / jsonpQueryParamName
    * 类型：`java.lang.String[]` / `java.lang.String`
    * 说明：JSONP 回调参数名称。命中且值合法（`[0-9A-Za-z_.]*`）时以 `application/javascript` 输出。

## com.giants.web.springmvc.aop.ControllerValidationAop

Controller 输入数据验证切面，配合 spring-modules-validation 框架一起使用，当数据合法性检查不通过时抛出
`com.giants.common.exception.DataValidationException`。切面约定 Controller 方法参数为 `(model, BindingResult)`。

配置参数：
* validator
    * 类型：`org.springmodules.validation.commons.DefaultBeanValidator`
    * 说明：数据验证器。
* errorMessageKey
    * 类型：`java.lang.String`
    * 说明：数据验证失败提示信息 resource key。
* dontThrowExceptionsReturnTypes
    * 类型：`java.util.List<String>`
    * 说明：不需要抛异常的返回值类型列表，如 `org.springframework.web.servlet.ModelAndView`。

## com.giants.web.springmvc.json.FastJsonHttpMessageConverter

FastJson MVC 转换器，适用于 Spring MVC 4.2 以上版本。如果 FastJson 版本较低（如 1.2.22）使用官方转换器会出现兼容性问题，
可使用此转换器替代。内置对 `JsonpResult` 的序列化支持（输出 `callback(...)` 形式）。

配置参数：
* charset
    * 类型：`java.nio.charset.Charset`
    * 默认值：UTF-8
    * 说明：字符集。
* features
    * 类型：`com.alibaba.fastjson.serializer.SerializerFeature[]`
    * 默认值：`new SerializerFeature[0]`
    * 说明：序列化特性设置。
* jsonProcessInterceptors
    * 类型：`com.giants.web.springmvc.json.JsonProcessInterceptor[]`
    * 说明：JSON 读写前后拦截器。
* filters
    * 类型：`com.alibaba.fastjson.serializer.SerializeFilter[]`
    * 默认值：`new SerializeFilter[0]`
    * 说明：序列化过滤器组。
* dateFormat
    * 类型：`java.lang.String`
    * 说明：日期时间格式化。

## com.giants.web.springmvc.json.JsonProcessInterceptor

JSON 读写拦截器接口，配合 `FastJsonHttpMessageConverter` 使用，可用于日志、加解密、埋点等。
提供 `readBefore` / `readAfter`（反序列化前后）与 `writeBefore` / `writeAfter`（序列化前后）四个钩子。

## com.giants.web.springmvc.json.JsonSerializePropertyFilter

FastJson 序列化属性过滤器（实现 `PropertyFilter`），通过 `ignorePropertyNames` 指定序列化时需要忽略的属性名（如敏感字段 `password`）。

配置参数：
* ignorePropertyNames
    * 类型：`java.util.List<String>`
    * 说明：需要忽略的属性名列表。

## com.giants.web.springmvc.resolver.JsonResultExceptionResolver

将异常解析为 `JsonResult` 对象并序列化输出。仅对返回 JSON 的方法生效（`@RestController` 或标注 `@ResponseBody` 的方法；
`includeModelAndView=true` 时对所有方法生效）。通过 `BuildExceptionJsonResult` 把 `GiantsException` 体系映射为对应错误码与国际化提示。

配置参数：
* messageConverters
    * 类型：`java.util.List<org.springframework.http.converter.HttpMessageConverter<Object>>`
    * 说明：http 输出信息转换器。
* includeModelAndView
    * 类型：`boolean`，默认 `false`
    * 说明：是否对 ModelAndView 返回值的 Controller 方法进行解析处理。
* jsonpQueryParamNames / jsonpQueryParamName
    * 类型：`java.lang.String[]` / `java.lang.String`
    * 说明：JSONP 回调参数名称。

## com.giants.web.springmvc.resolver.JsonExceptionResolver

Exception 解析器，将异常解析后输出。相比 `JsonResultExceptionResolver` 额外提供响应状态码配置。

配置参数：
* jsonpQueryParamNames
    * 类型：`java.lang.String[]`
    * 说明：JSONP 回调参数名称。
* includeModelAndView
    * 类型：`boolean`，默认 `false`
    * 说明：是否对 ModelAndView 返回值的 Controller 方法进行解析处理。
* responseExceptionStatus
    * 类型：`int`，默认 `600`
    * 说明：http response 响应状态码。
* messageConverters
    * 类型：`java.util.List<org.springframework.http.converter.HttpMessageConverter<Object>>`
    * 说明：http 输出信息转换器。

## com.giants.web.springmvc.resolver.CookieHandlerMethodArgumentResolver

Cookie 参数解析器，获取 Cookie 值注入到 Controller 方法参数，使 Controller 代码与 Cookie API 解耦。
匹配规则为「参数类型为 `String` 且参数名与 `cookieName` 相等」。

配置参数：
* cookieName
    * 类型：`java.lang.String`
    * 说明：cookie 名称，Controller 参数名与 cookie 名称一致时注入。

## com.giants.web.springmvc.resolver.SessionHandlerMethodArgumentResolver

Session 参数解析器，适用于 Spring MVC 4.X。获取 Session 值注入到 Controller 方法参数，使 Controller 代码与 Session API 解耦。
匹配规则为「参数类型 + 参数名同时与 `SessionAttribute` 相等」。

配置参数：
* sessionAttribute
    * 类型：`com.giants.web.springmvc.resolver.SessionAttribute`
    * 说明：session 属性配置，指定属性名称及 session 对象类型。

## com.giants.web.springmvc.resolver.SessionArgumentResolver

Session 参数解析器，适用于 Spring MVC 3.X。获取 Session 值注入到 Controller 方法参数，使 Controller 代码与 Session API 解耦。

配置参数：
* sessionAttributes / sessionAttribute
    * 类型：`com.giants.web.springmvc.resolver.SessionAttribute[]` / `com.giants.web.springmvc.resolver.SessionAttribute`
    * 说明：session 属性配置，可配置多个，指定属性名称及 session 对象类型。

---

更多完整的配置与示例，请查看 [使用手册](../docs/usage-manual.md#module-springmvc)。
