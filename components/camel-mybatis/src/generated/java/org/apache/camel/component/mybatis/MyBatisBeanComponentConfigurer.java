/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.mybatis;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.GeneratedPropertyConfigurer;
import org.apache.camel.support.component.PropertyConfigurerSupport;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@SuppressWarnings("unchecked")
public class MyBatisBeanComponentConfigurer extends PropertyConfigurerSupport implements GeneratedPropertyConfigurer {

    @Override
    public boolean configure(CamelContext camelContext, Object obj, String name, Object value, boolean ignoreCase) {
        MyBatisBeanComponent target = (MyBatisBeanComponent) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "configurationuri":
        case "configurationUri": target.setConfigurationUri(property(camelContext, java.lang.String.class, value)); return true;
        case "lazystartproducer":
        case "lazyStartProducer": target.setLazyStartProducer(property(camelContext, boolean.class, value)); return true;
        case "basicpropertybinding":
        case "basicPropertyBinding": target.setBasicPropertyBinding(property(camelContext, boolean.class, value)); return true;
        case "sqlsessionfactory":
        case "sqlSessionFactory": target.setSqlSessionFactory(property(camelContext, org.apache.ibatis.session.SqlSessionFactory.class, value)); return true;
        default: return false;
        }
    }

}

