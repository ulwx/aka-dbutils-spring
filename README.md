### Spring集成

与spring继承首先必须引入maven依赖如下：

```xml
<dependency>
   <artifactId>aka-dbutils-spring</artifactId>
   <groupId>com.github.ulwx</groupId>
   <version>1.0.3</version>
</dependency>

```

下面以一个完整的例子（aka-dbutils-spring-test）来介绍如何与Spring集成。

例子工程(aka-dbutils-spring-test)的maven配置如下：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <artifactId>aka-dbutils-spring-test</artifactId>
   <groupId>com.github.ulwx</groupId>
   <version>1.0.0-SNAPSHOT</version>
   <parent>
      <groupId>com.github.ulwx</groupId>
      <artifactId>aka-dbutils-parent</artifactId>
      <version>1.0.3.1</version>
      <relativePath>../aka-dbutils-parent/pom.xml</relativePath>
   </parent>
   <properties>
      <spring.version>5.3.1</spring.version>
   </properties>
   <dependencies>
      <dependency>
         <artifactId>aka-dbutils-spring</artifactId>
         <groupId>com.github.ulwx</groupId>
      </dependency>
      <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-context</artifactId>
         <version>${spring.version}</version>
      </dependency>
      <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-aspects</artifactId>
         <version>${spring.version}</version>
      </dependency>
      <dependency>
         <groupId>org.apache.commons</groupId>
         <artifactId>commons-dbcp2</artifactId>
         <version>2.8.0</version>
      </dependency>
      <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
         <version>8.0.23</version>
      </dependency>
   </dependencies>
</project>
```


下面为MyConfiguration配置类，用于声明数据源及事务相关的配置。

```java
package com.github.ulwx.aka.dbutils.database.spring.test;
......
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)   //暴露代理对象，这样可以通过 AopContext.currentProxy()获取代理对象
@Configuration
@ComponentScan
public class MyConfiguration {

    @Bean(destroyMethod = "close")
    public BasicDataSource dataSource() {       
        BasicDataSource dataSource = new BasicDataSource();  //使用的是commons-dbcp2数据源
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?x=1&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("abcd");
        dataSource.setMaxWaitMillis(10000);
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(10);
        dataSource.setMinEvictableIdleTimeMillis(6000);
        return dataSource;

    }
    @Bean  //定义事务管理器，Bean名称为transactionManager，这样业务方法上的@Transactional的注解就不必指定transactionManager属性
    public DataSourceTransactionManager transactionManager() {    
        DataSourceTransactionManager dt = new DataSourceTransactionManager();
        dt.setDataSource(dataSource()); //④
        return dt;
    }
    @Bean   //定义MDataBaseFactory类型工厂Bean用于在DataBaseTemplate Bean里获取MDataBase实例
    public MDataBaseFactory mDataBaseFactory() {
        MDataBaseFactory mDataBaseFactory = 
                 new MDataBaseFactory(dataSource()); //③ 一定要和上面④处使用同一个数据源，不然不会应用上事务
        mDataBaseFactory.setTableColumRule(DbConst.TableNameRules.underline_to_camel);  //①
        mDataBaseFactory.setTableNameRule(DbConst.TableColumRules.underline_to_camel);  //②
        return mDataBaseFactory;

    }
    @Bean  //定义MDataBaseTemplate类型Bean，此Bean会被注入到业务实体（Service或Dao）中，从而可以进行数据库操作
    public MDataBaseTemplate mDataBaseTemplate() {
        return new MDataBaseTemplate(mDataBaseFactory());
    }

}


```

上面的MyConfiguration配置类里，可以看到必须要定义MDataBaseFactory类型的Bean，在Bean里可以配置表名和表列到Java类和属性名称的转换规则，同时需要传入一个数据源，表明在此数据源上进行操作。还必须定义一个MDataBaseTemplate类型的Bean，通过这个Bean注入到你的业务实体从而进行数据库操作。需要注意的是③处对MDataBaseFactory构造函数传入的数据源一定和DataSourceTransactionManager里传入的数据源一致，这样才能应用到Spring的事务机制。

**Address类**

```java
package com.github.ulwx.aka.dbutils.database.spring.test;
import com.github.ulwx.aka.dbutils.tool.support.ObjectUtils;
import com.github.ulwx.aka.dbutils.database.MdbOptions;
public class Address extends MdbOptions implements java.io.Serializable {

	private String name;/*;len:20*/
	private Integer addressId;/*;len:10*/
	private Long custormerId;/*;len:19*/
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setAddressId(Integer addressId){
		this.addressId = addressId;
	}
	public Integer getAddressId(){
		return addressId;
	}
	public void setCustormerId(Long custormerId){
		this.custormerId = custormerId;
	}
	public Long getCustormerId(){
		return custormerId;
	}
	public String toString(){
		return  ObjectUtils.toString(this);
	}
	private static final long serialVersionUID =556120941L;

}
```

**AddressDao类**

```java
package com.github.ulwx.aka.dbutils.database.spring.test;
......
@Component
public class AddressDao {
    private MDataBaseTemplate mDataBaseTemplate; //通过构造方法被注入，后面可以 通过MDataBaseTemplate对象操作数据库
    public AddressDao(MDataBaseTemplate mDataBaseTemplate){
        this.mDataBaseTemplate=mDataBaseTemplate;
    }
    public List<Address> getListMd(){
        Map<String, Object> mp=new HashMap<>();
        List<Address> list=mDataBaseTemplate.queryList(Address.class,
                MD.md(),mp);
        return list;
    }
    @Transactional(propagation = Propagation.NESTED)
    public void updateMd(){
        Map<String, Object> mp=new HashMap<>();
        mDataBaseTemplate.update(MD.md(),mp);
    }
}
///AddressDao.md
getListMd
===
select * from address

updateMd
===
UPDATE
`address`
SET
`name` = '111111'
WHERE  `address_id` = 1

```

**MyService类**

```java
package com.github.ulwx.aka.dbutils.database.spring.test;
......
@Service
public class MyService {
    private AddressDao addressDao;
    public MyService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMdb(){
        List<Address> list2= addressDao.getListMd();
        //声明了Propagation.NESTED事务
        addressDao.updateMd();
       //MyService方法的内部调用会使用被调用方法上声明的事务失效，所以需要用下面方式调用
        ((MyService) AopContext.currentProxy()).updateMdbOther();
        //MyService方法的内部调用会使用被调用方法上声明的事务失效，下面的内部调用还是在updateMdb()方法的事务里。
        //updateMdbOther();

    }
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateMdbOther(){
        System.out.println("call updateMdbOther");
    }
}
```

**ApplicationTest类**

```java
package com.github.ulwx.aka.dbutils.database.spring.test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
public class ApplicationTest {
    public static void main(String[] args) throws Exception{
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(MyConfiguration.class);
        MyService myService=(MyService)ctx.getBean("myService");
        myService.updateMdb();

    }
}

```

至此与Spring集成的例子介绍完毕，读者可以发现与MyBatis有很多相似之处。
