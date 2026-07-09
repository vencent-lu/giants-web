# giants-web-struts2
[![Maven Central](https://img.shields.io/maven-central/v/com.github.vencent-lu/giants-web-struts2.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.github.vencent-lu/giants-web-struts2)

Struts2 工具封装组件，提供 JSON 请求处理基类、CRUD Action 骨架，以及配套的校验、异常拦截器。
统一使用 `com.giants.common` 的异常体系与错误编码。

## com.giants.web.struts2.JsonActionSupport

处理 JSON 请求的抽象基类，封装统一的返回字段：`success`、`errorCode`、`message`、`fieldValidationError`。

关键方法：
* `validator()`
    * 说明：动态读取当前 Action 上配置的所有 Struts2 校验规则，封装成 `List<GiantsValidator>` 输出，供前端做客户端校验。
    * 返回结果码：`validator`。若没有配置任何校验器，`success=false`，`message` 取国际化 key `not.find.validator`。

属性字段：
* `success`
    * 类型：`boolean`，默认 `true`。是否处理成功。
* `errorCode`
    * 类型：`byte`，默认 `GiantsConstants.ERROR_CODE_SUCCESS`（0）。错误编码。
* `message`
    * 类型：`java.lang.String`。提示信息。
* `validators`
    * 类型：`java.util.List<GiantsValidator>`。`validator()` 方法输出的校验规则列表。
* `fieldValidationError`
    * 类型：`java.util.Map<String, List<String>>`。字段校验错误信息。

## com.giants.web.struts2.JsonAction

继承自 `JsonActionSupport`，进一步简化 JSON 请求处理。业务逻辑只需实现 `executeAction()` 抽象方法。

* `executeAction()`
    * 说明：编写业务逻辑的抽象方法。执行成功后 `success=true`、`errorCode=0`；抛出异常时由 `BusinessExceptionHandler.handleExceptionToJson` 捕获，转为对应错误码并返回 `error` 结果码。

## com.giants.web.struts2.CRUDAction\<T\>

标准增删改查（CRUD）Action 骨架，实现了 `Preparable`，内置分页（`com.giants.common.tools.Page`）与查询过滤条件（`queryFilters`）。
子类只需实现数据访问相关的抽象方法：

* `listCount(Map<String,Object> queryFilters)`：查询总记录数。
* `listResultSet(Map<String,Object> queryFilters)`：查询结果集。
* `createEntity()`：新增实体。
* `modifyEntity()`：修改实体。
* `loadEntity(Serializable id)`：加载实体。
* `deleteEntity()`：删除实体。

内置动作方法：
* `list()` / `execute()`：分页查询列表，返回 `success`。
* `input()`：进入表单页，返回 `input`。
* `save()`：`id` 为空时新增，否则修改；业务异常时返回 `input`，成功返回 `reload`。
* `delete()`：删除，返回 `reload`。

## com.giants.web.struts2.GiantsValidator

校验规则的传输对象，描述单个字段的校验类型、提示信息与校验参数。由 `JsonActionSupport.validator()` 生成。

## com.giants.web.struts2.interceptor.JsonActionInterceptor

JSON Action 异常拦截器。拦截 `JsonActionSupport` 及其子类的调用，捕获执行过程中抛出的异常，
统一交由 `BusinessExceptionHandler.handleExceptionToJson` 处理并返回 `error` 结果码。

## com.giants.web.struts2.interceptor.JsonValidationInterceptor

JSON 校验拦截器，继承 Struts2 的 `AnnotationValidationInterceptor`。当校验产生字段错误时，
自动设置 `success=false`、`errorCode=ERROR_CODE_DATA_CHECK_FAILURE`（2），`message` 取国际化 key `request.data.validation.fail`，
并将字段错误写入 `fieldValidationError`。

## com.giants.web.struts2.exception.BusinessExceptionHandler

异常处理工具类：
* `handleException(ActionSupport action, BusinessException e)`：将业务异常转为 Action Error（用于 JSP 页面场景）。
* `handleExceptionToJson(JsonActionSupport action, Exception e)`：将异常转为 JSON Action 的错误字段（用于 JSON 场景）。

## com.giants.web.struts2.WebUtils

Struts2 环境下的请求上下文工具类，基于 `ActionContext` / `ServletActionContext`：
* `getSession()` / `getSessionAttribute(name)` / `setSessionAttribute(name, value)`
* `getRequest()`：获取当前 `HttpServletRequest`。
* `getRemoteAddr()`：获取客户端真实 IP（依次尝试 `X-Real-IP`、`X-Forwarded-For`、各类代理头，最后回退到 `getRemoteAddr()`）。

---

更多完整的拦截器栈配置与示例，请查看 [使用手册](../docs/usage-manual.md#module-struts2)。