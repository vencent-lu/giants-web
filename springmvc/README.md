# giants-web-springmvc
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.vencent-lu/giants-web-springmvc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.vencent-lu/giants-web-springmvc)

springmvc 工具封装组件

#### com.giants.web.springmvc.advice.JsonResultResponseAdvice

统一用 com.giants.web.springmvc.json.JsonResult 封装response返回结果。

配置参数:
* uriExcludeList
    * 类型：java.util.List\<String\>
    * 说明：排除URI列表，些列表中的请求，不会进行统一封装处理。

#### com.giants.web.springmvc.aop.ControllerValidationAop

Controller 输入数据验证拦截器，配合 spring-modules-validation 框架一起使用, 当数据合法性检查不通过时 throw com.giants.common.exception
.DataValidationException。

配置参数：
* validator
    * 类型：org.springmodules.validation.commons.DefaultBeanValidator
    * 说明：数据验证器。
* errorMessageKey
    * 类型：java.lang.String
    * 说明：数据验证失败提示信息 resource key。
* dontThrowExceptionsReturnTypes
    * 类型：java.util.List\<String\>
    * 说明：不需要抛异常的返回值类型列表，如 org.springframework.web.servlet.ModelAndView。

#### com.giants.web.springmvc.json.FastJsonHttpMessageConverter

FastJson mvc 转换器，如果FastJson版本较底(如:1.2.22) 使用 FastJson 自带的转换器，会出现一些兼容性问题，可以使用些转换器.

配置参数：
* charset
    * 类型：java.nio.charset.Charset
    * 默认值：UTF8
    * 说明：字符集
* features
    * 类型：com.alibaba.fastjson.serializer.SerializerFeature[]
    * 默认值：new SerializerFeature[0]
    * 说明：序列化特性设置
* jsonProcessInterceptors
    * 类型：com.giants.web.springmvc.json.JsonProcessInterceptor[]
    * 说明：json解析拦截器
* filters
    * 类型：com.alibaba.fastjson.serializer.SerializeFilter[]
    * 默认值：new SerializeFilter[0]
    * 说明：序列化过滤器组
* dateFormat
    * 类型：java.lang.String
    * 说明：日期时间格式化

#### com.giants.web.springmvc.resolver.JsonExceptionResolver
Exception 解析成JSON数据输出(实际上这里没有任何与JSON相关的处理，叫异常解析器 比较合适，后续改进)

配置参数：
* jsonpQueryParamNames
    * 类型：java.lang.String[]
    * 说明：jsonp回调参数名称
* includeModelAndView
    * 类型：boolean
    * 默认值：false
    * 说明：是否对ModelAndView返回值的 Controller 方法进行解析处理
* responseExceptionStatus
    * 类型：int
    * 默认值：600
    * 说明：http response 响应状态码
* messageConverters
    * 类型：java.util.List\<org.springframework.http.converter.HttpMessageConverter\<Object\>\>
    * 说明：http输出信息转换器

#### com.giants.web.springmvc.resolver.JsonResultExceptionResolver
Exception 解析成JsonResult对象

配置参数：
* jsonpQueryParamNames
    * 类型：java.lang.String[]
    * 说明：jsonp回调参数名称
* includeModelAndView
    * 类型：boolean
    * 默认值：false
    * 说明：是否对ModelAndView返回值的 Controller 方法进行解析处理
* messageConverters
    * 类型：java.util.List\<org.springframework.http.converter.HttpMessageConverter\<Object\>\>
    * 说明：http输出信息转换器

#### com.giants.web.springmvc.resolver.CookieHandlerMethodArgumentResolver

Cookie 参数解析器，获取Cookie值，注入到 Controller 方法参数值。使用 Controller 代码与Cookie API 解耦。

配置参数：
* cookieName
    * 类型：java.lang.String
    * 说明：cookie 名称 Controller 参数签名 与 cookie中参数签名一至

#### com.giants.web.springmvc.resolver.SessionHandlerMethodArgumentResolver

Session 参数解析器，获取Session值，注入到 Controller 方法参数值。使用 Controller 代码与Session API 解耦。

配置参数：
* sessionAttribute
    * 类型：com.giants.web.springmvc.resolver.SessionAttribute
    * 说明：session属性配置，指定属性名称 及 session 对象类型

#### com.giants.web.springmvc.resolver.SessionArgumentResolver

Session 参数解析器，适用于spring mvc 3.X 获取Session值，注入到 Controller 方法参数值。使用 Controller 代码与Session API 解耦。

配置参数：
* sessionAttribute
    * 类型：com.giants.web.springmvc.resolver.SessionAttribute
    * 说明：session属性配置，指定属性名称 及 session 对象类型

#### com.giants.web.springmvc.v3.PackingJsonResultAnnotationMethodHandlerAdapter

统一用 com.giants.web.springmvc.json.JsonResult 封装response返回结果,适用于spring mvc 3.X .

配置参数：
* jsonResultExcludeMethodList
    * 类型：java.util.List\<String\>
    * 说明：排除不需要解析处理的 Controller方法
* jsonResultExcludeReturnTypeList
    * 类型：java.util.List\<String\>
    * 说明：排除不需要解析处理的 返回值类型
* invokeInterceptors
    * 类型：java.util.List\<com.giants.web.springmvc.v3.MethodHandlerInterceptor\>
    * 默认值：new ArrayList\<~\>()
    * 说明：方法执行拦截器