# giants-web
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.vencent-lu/giants-web/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.vencent-lu/giants-web)

WEB 公共工具类封装

#### com.giants.web.utils.WebUtils
简化 HttpServletRequest 调用工具类，需求初始化 com.giants.web.filter.WebFilter 后才能拿到 HttpServletRequest 上下文，目前提供以下方法：
* getSessionAttribute
* setSessionAttribute
* getHeader
* getIpAddress
* getRequest
* getIpAddress