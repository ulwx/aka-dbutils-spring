package com.github.ulwx.aka.dbutils.database.spring;

public class AkaMDataBaseTemplateExecuteInfoHolder {

    private static final ThreadLocal<AkaDBTemplateExecuteInfo> holder = new ThreadLocal<AkaDBTemplateExecuteInfo>();

    public static void put(AkaDBTemplateExecuteInfo akaDBTemplateExecuteInfo)
    {
        holder.set(akaDBTemplateExecuteInfo);
    }

    public static AkaDBTemplateExecuteInfo get()
    {
        return holder.get();
    }
}
