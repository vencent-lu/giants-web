package com.giants.web.springmvc.json;

import com.alibaba.fastjson.serializer.PropertyFilter;
import com.giants.common.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * FastJson 序列化属性过滤 *
 * <p>{@link PropertyFilter} 接口实现请查看 {@link PropertyFilter}.
 *
 * @author vencent-lu
 * @since 1.0
 */
public class JsonSerializePropertyFilter implements PropertyFilter {

    private List<String> ignorePropertyNames;

    @Override
    public boolean apply(Object object, String name, Object value) {
        if (CollectionUtils.isNotEmpty(this.ignorePropertyNames)
                && this.ignorePropertyNames.contains(name)) {
            return false;
        }
        return true;
    }

    public void setIgnorePropertyNames(List<String> ignorePropertyNames) {
        this.ignorePropertyNames = ignorePropertyNames;
    }
}
