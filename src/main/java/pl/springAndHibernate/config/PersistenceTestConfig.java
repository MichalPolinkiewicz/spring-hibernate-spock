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
@Profile("test")
public class PersistenceTestConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("h2.driver"));
        dataSource.setUrl(environment.getProperty("h2.url"));
        dataSource.setUsername(environment.getProperty("h2.user"));
        dataSource.setPassword(environment.getProperty("h2.password"));

        return dataSource;
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", environment.getProperty("h2.dialect"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("h2.show_sql"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("h2.format_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("h2.auto"));

        return properties;
    }
}
