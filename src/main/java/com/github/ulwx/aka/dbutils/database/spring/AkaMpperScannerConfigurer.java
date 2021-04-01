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
import org.springframework.util.StringUtils;

public class AkaMpperScannerConfigurer  implements BeanDefinitionRegistryPostProcessor,
        InitializingBean, ApplicationContextAware, BeanNameAware {

    private String basePackage;
    private String mdDataBaseTemplateBeanName;
    private ApplicationContext applicationContext;
    private String beanName;

    /**
     * This property lets you set the base package for your mapper interface files.
     * <p>
     * You can set more than one package by using a semicolon or comma as a separator.
     * <p>
     * Mappers will be searched for recursively starting in the specified package(s).
     *
     * @param basePackage
     *          base package name
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
       // notNull(this.basePackage, "Property 'basePackage' is required");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.2
     */
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
                StringUtils.tokenizeToStringArray(this.basePackage,
                        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }


}
