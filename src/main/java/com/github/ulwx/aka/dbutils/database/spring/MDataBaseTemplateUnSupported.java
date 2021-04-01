package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.DataBase;
import com.github.ulwx.aka.dbutils.database.DbException;
import java.sql.Connection;
import java.sql.Savepoint;
import java.util.Map;

public class MDataBaseTemplateUnSupported {
    /**
     * 不支持的方法
     * @param b
     * @throws DbException
     */
    public void setAutoCommit(boolean b) throws DbException {
        throw new  UnsupportedOperationException();
    }

    /**
     * 不支持的方法
     * @throws DbException
     */
    public boolean getAutoCommit() throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @throws DbException
     */
    public void rollback() throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @throws DbException
     */
    public Map<String, Savepoint> getSavepoint() {
        throw new  UnsupportedOperationException();
    }

    /**
     * 不支持的方法
     * @param savepointName
     * @throws DbException
     */
    public void setSavepoint(String savepointName) throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @param savepointName
     * @throws DbException
     */
    public void releaseSavepoint(String savepointName) throws DbException {
        throw new  UnsupportedOperationException();
    }

    /**
     * 不支持的方法
     * @param savepointName
     * @throws DbException
     */
    public void rollbackToSavepoint(String savepointName) throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @throws DbException
     */
    public boolean isColsed() throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @throws DbException
     */

    public void commit() throws DbException {
        throw new  UnsupportedOperationException();
    }
    /**
     * 不支持的方法
     * @throws DbException
     */
    public Connection getConnection(boolean force) {
        throw new  UnsupportedOperationException();
    }

    /**
     * 不支持的方法
     * @throws DbException
     */
    public DataBase getDataBase() {
        throw new  UnsupportedOperationException();
    }

}
