package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.DataBase.MainSlaveModeConnectMode;
import com.github.ulwx.aka.dbutils.database.DbContext;
import com.github.ulwx.aka.dbutils.database.dbpool.DBPoolFactory;
import com.github.ulwx.aka.dbutils.tool.support.type.TResult;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AkaDynamicDataSource extends AbstractRoutingDataSource
{
    private String xmlPoolFileName;
    private String poolName;
    public AkaDynamicDataSource(String xmlPoolFileName, String poolName){
        this.xmlPoolFileName=xmlPoolFileName;
        this.poolName=poolName;
        DataSource masterDataSoruce= DBPoolFactory.getInstance(xmlPoolFileName).getDBPool(poolName);
        ConcurrentHashMap<String, DataSource> slaverDataSourceMap=
                DBPoolFactory.getInstance(xmlPoolFileName).getSlaveDataSources(poolName);
        this.setDefaultTargetDataSource(masterDataSoruce);
        Map<Object, Object> targetMap=new ConcurrentHashMap<>();
        targetMap.put(poolName,masterDataSoruce);
        targetMap.putAll(slaverDataSourceMap);
        this.setTargetDataSources(targetMap);

    }

    /**
     * 获取与数据源相关的key 此key是Map<String,DataSource> resolvedDataSources 中与数据源绑定的key值
     * 在通过determineTargetDataSource获取目标数据源时使用
     */
    @Override
    protected Object determineCurrentLookupKey()
    {
        if(DbContext.getMainSlaveModeConnectMode()== MainSlaveModeConnectMode.Connect_MainServer) {
            return poolName;
        }else if(DbContext.getMainSlaveModeConnectMode()== MainSlaveModeConnectMode.Connect_SlaveServer){
            TResult<String> tslaveName=new TResult<>();
            DBPoolFactory.getInstance(xmlPoolFileName).selectSlaveDbPool(poolName,tslaveName);
            return tslaveName.getValue();
        }else if(DbContext.getMainSlaveModeConnectMode()== MainSlaveModeConnectMode.Connect_Auto){
            AkaDBTemplateExecuteInfo akaDBTemplateExecuteInfo=
                    AkaMDataBaseTemplateExecuteInfoHolder.get();
            if(akaDBTemplateExecuteInfo!=null &&
                    akaDBTemplateExecuteInfo.getMethod().getName().startsWith("query")
                && !akaDBTemplateExecuteInfo.isTransactional() ){//获取从库连接
                TResult<String> tslaveName=new TResult<>();
                DBPoolFactory.getInstance(xmlPoolFileName).selectSlaveDbPool(poolName,tslaveName);
                return tslaveName.getValue();
            }else{//获取主库连接
                return poolName;
            }

        }
        else{
            return poolName;
        }
    }

}
