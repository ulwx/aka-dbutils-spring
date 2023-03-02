package com.github.ulwx.aka.dbutils.database.spring.boot;

import com.github.ulwx.aka.dbutils.database.spring.MDataBaseFactory;
import com.github.ulwx.aka.dbutils.database.spring.MDataBaseTemplate;
import com.github.ulwx.aka.dbutils.database.utils.DbConst;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
@Configuration
@ComponentScan

public class MyConfiguration {

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?x=1&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setMaxWaitMillis(10000);
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(1);
        dataSource.setMinEvictableIdleTimeMillis(6000);
        return dataSource;

    }


    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dt = new DataSourceTransactionManager();
        dt.setDataSource(dataSource());
        return dt;
    }


    @Bean
    public MDataBaseFactory mDataBaseFactory(BasicDataSource dataSource) {
        MDataBaseFactory mDataBaseFactory = new MDataBaseFactory(dataSource);
        mDataBaseFactory.setTableColumRule(DbConst.TableNameRules.underline_to_camel);
        mDataBaseFactory.setTableNameRule(DbConst.TableColumRules.underline_to_camel);
        return mDataBaseFactory;

    }

    @Bean
    public MDataBaseTemplate mDataBaseTemplate(MDataBaseFactory mDataBaseFactory) {
        return new MDataBaseTemplate(mDataBaseFactory);
    }


}
