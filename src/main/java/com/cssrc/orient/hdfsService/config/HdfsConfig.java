package com.cssrc.orient.hdfsService.config;

import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

@Component
public class HdfsConfig {


    private static Configuration configuration = null;

    @Bean("hdfsConf")
    public synchronized static  Configuration getHdfsConf(){
        if (configuration==null){

            Properties properties = new Properties();
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = HdfsConfig.class.getClassLoader().getResourceAsStream("hdfs.properties");
            // 使用properties对象加载输入流
            try {
                properties.load(in);
                configuration = new Configuration();
                @SuppressWarnings("rawtypes")
                Enumeration en = properties.propertyNames();
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    String value = properties.getProperty(key);
                    configuration.set(key, value);
                }

            }
            catch (Exception e){
                return null;
            }
            return configuration;
        }
        return configuration;
    }

}
