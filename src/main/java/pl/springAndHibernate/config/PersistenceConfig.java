package pl.springAndHibernate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pl.springAndHibernate")
@PropertySource("classpath:hibernate.properties")
public class PersistenceConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Profile("prod")
    public DataSource getDataSourceProd() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("mysql.driver"));
        dataSource.setUrl(environment.getProperty("mysql.url"));
        dataSource.setUsername(environment.getProperty("mysql.user"));
        dataSource.setPassword(environment.getProperty("mysql.password"));

        return dataSource;
    }

    @Bean
    @Profile("test")
    public DataSource getDataSourceTest() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("h2.driver"));
        dataSource.setUrl(environment.getProperty("h2.url"));
        dataSource.setUsername(environment.getProperty("h2.user"));
        dataSource.setPassword(environment.getProperty("h2.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(getDataSourceTest());
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setPackagesToScan("pl.springAndHibernate.dao");

        String [] activeProfiles = environment.getActiveProfiles();
        String activeProfile = "";

        for (int i = 0; i < activeProfiles.length; i++){
            if (activeProfiles[i].equals("prod")){
                activeProfile = activeProfiles[i];
            }
        }

        if (activeProfile.equals("prod")){
            entityManager.setJpaProperties(propertiesProd());
        } else {
            entityManager.setJpaProperties(propertiesTest());
        }


        return entityManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }

    @Bean
    @Profile("prod")
    public Properties propertiesProd() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", environment.getProperty("mysql.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("mysql.show_sql"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("mysql.format_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("mysql.auto"));

        return properties;
    }

    @Bean
    @Profile("test")
    public Properties propertiesTest() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", environment.getProperty("h2.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("h2.show_sql"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("h2.format_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("h2.auto"));

        return properties;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
