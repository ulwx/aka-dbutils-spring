package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.MDataBase;
import org.springframework.transaction.support.ResourceHolderSupport;

public class MDataBaseHolder extends ResourceHolderSupport {
    private MDataBase mDataBase;

    public MDataBaseHolder(MDataBase mDataBase) {
        this.mDataBase = mDataBase;
    }

    public MDataBase getMDataBase() {
        return mDataBase;
    }

}
