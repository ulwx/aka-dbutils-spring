package com.github.ulwx.aka.dbutils.database.spring.boot;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {
    @Test
    public   void test() throws Exception{
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(MyConfiguration.class);
        MyService myService=(MyService)ctx.getBean("myService");
        myService.init();
        myService.updateMdb();

        ctx.close();


    }


}
