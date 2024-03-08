package com.along.coash.framework.mybatis.handler;

import com.along.coash.framework.utils.JsonUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Set;

public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {
    @Override
    protected Object parse(String json) {
        return JsonUtils.parseObject(json, new TypeReference<Set<Long>>() {
        });
    }

    @Override
    protected String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }

}
