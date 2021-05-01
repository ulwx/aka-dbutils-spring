package com.github.ulwx.aka.dbutils.database.spring;

import java.lang.reflect.Method;

public class AkaDBTemplateExecuteInfo {
    private Method method;
    private boolean isTransactional;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isTransactional() {
        return isTransactional;
    }

    public void setTransactional(boolean transactional) {
        isTransactional = transactional;
    }
}
