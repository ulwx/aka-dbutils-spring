package com.github.ulwx.aka.dbutils.database.spring;

import com.github.ulwx.aka.dbutils.database.AkaMapper;
import com.github.ulwx.aka.dbutils.database.DbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class AkaClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AkaClassPathMapperScanner.class);

    private  static String mdatabase_template_property_name="mDataBaseTemplate";

    private String mdDataBaseTemplateBeanName;

    private Class<?> markerType= AkaMapper.class;

    private Class< AkaMapperFactoryBean> mapperFactoryBeanClass = AkaMapperFactoryBean.class;

    public AkaClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }


    public String getMdDataBaseTemplateBeanName() {
        return mdDataBaseTemplateBeanName;
    }

    public void setMdDataBaseTemplateBeanName(String mdDataBaseTemplateBeanName) {
        this.mdDataBaseTemplateBeanName = mdDataBaseTemplateBeanName;
    }

    /**
     * Configures parent scanner to search for the right interfaces. It can search   for those
     * that extends a markerType
     */
    public void registerFilters() {

        addIncludeFilter(new AssignableTypeFilter(this.markerType) {
            @Override
            protected boolean matchClassName(String className) {
                return false;
            }
        });

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    /**
     * Calls the parent search that will search and register all the candidates. Then the registered objects are post
     * processed to set them as MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            LOGGER.warn("No aka-dbutils mapper was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            LOGGER.debug( "Creating AkaMapperFactoryBean with name '"
                    + holder.getBeanName() + "' and '" + beanClassName
                    + "' mapperType");
            // the mapper type is the original class of the bean
            // but, the actual class of the bean is AkaMapperFactoryBean
            definition.setBeanClass(this.mapperFactoryBeanClass);
            boolean inject=false;
            if (StringUtils.hasText(this.mdDataBaseTemplateBeanName)) {
                 definition.getPropertyValues().add(mdatabase_template_property_name,
                         new RuntimeBeanReference(this.mdDataBaseTemplateBeanName));
                Class mapperType=null;
                try {
                    mapperType=Class.forName(beanClassName);
                } catch (ClassNotFoundException e) {
                }
                definition.getPropertyValues().add("mapperType", mapperType);
                LOGGER.debug( "Enabling autowire by type for AkaMapperFactoryBean with name '" + holder.getBeanName() + "'.");
               // definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                inject = true;

            }

            if (!inject) {
                throw new DbException("AkaMapperFactoryBean with name '" + holder.getBeanName() +" 沒有注入MDataBaseTemplate对象！");
            }
            definition.setLazyInit(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isAbstract() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            LOGGER.warn( "Skipping AkaMapperFactoryBean with name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' mapperType" + ". Bean already defined with the same name!");
            return false;
        }
    }
}
