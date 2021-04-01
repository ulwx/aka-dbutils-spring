package com.github.ulwx.aka.dbutils.database.spring;

import org.springframework.beans.factory.FactoryBean;

public class AkaMapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> mapperType;
    private MDataBaseTemplate mDataBaseTemplate;

    public Class<T> getMapperType() {
        return mapperType;
    }

    public void setMapperType(Class<T> mapperType) {
        this.mapperType = mapperType;
    }

    public MDataBaseTemplate getmDataBaseTemplate() {
        return mDataBaseTemplate;
    }

    public void setmDataBaseTemplate(MDataBaseTemplate mDataBaseTemplate) {
        this.mDataBaseTemplate = mDataBaseTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return mDataBaseTemplate.getMapper(this.mapperType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
