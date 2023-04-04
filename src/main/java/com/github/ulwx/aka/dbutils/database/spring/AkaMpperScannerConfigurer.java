package com.github.ulwx.aka.dbutils.database.spring;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class AkaMpperScannerConfigurer  implements BeanDefinitionRegistryPostProcessor,
        InitializingBean, ApplicationContextAware, BeanNameAware, EnvironmentAware {

    private String basePackages;
    private String mdDataBaseTemplateBeanName="mDataBaseTemplate";
    private ApplicationContext applicationContext;
    private String beanName;
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }


    public String getMdDataBaseTemplateBeanName() {
        return mdDataBaseTemplateBeanName;
    }

    public void setMdDataBaseTemplateBeanName(String mdDataBaseTemplateBeanName) {
        this.mdDataBaseTemplateBeanName = mdDataBaseTemplateBeanName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {


        AkaClassPathMapperScanner scanner = new AkaClassPathMapperScanner(registry);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setMdDataBaseTemplateBeanName(this.mdDataBaseTemplateBeanName);
        scanner.setBeanNameGenerator(new BeanNameGenerator(){
            @Override
            public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
                String generatedBeanName = definition.getBeanClassName();
                return generatedBeanName;
            }
        });
        scanner.registerFilters();
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackages,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

    }


}
