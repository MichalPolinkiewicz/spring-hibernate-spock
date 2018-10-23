package pl.springAndHibernate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:hibernate.properties")
@Profile("prod")
public class PersistenceProdConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("mysql.driver"));
        dataSource.setUrl(environment.getProperty("mysql.url"));
        dataSource.setUsername(environment.getProperty("mysql.user"));
        dataSource.setPassword(environment.getProperty("mysql.password"));

        return dataSource;
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", environment.getProperty("mysql.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("mysql.show_sql"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("mysql.format_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("mysql.auto"));

        return properties;
    }
}
