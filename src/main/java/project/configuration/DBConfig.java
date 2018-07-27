package project.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.beans.PropertyVetoException;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@ComponentScan
@EnableJpaRepositories(basePackages = {"project/repository"})
@PropertySource(value = {"classpath:application.properties"})
public class DBConfig implements ApplicationContextAware, WebMvcConfigurer {

    private static final String PASSWORD = "db.password";
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";

    @Autowired
    private Environment env;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public MysqlDataSource dataSource() throws IllegalStateException, PropertyVetoException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(env.getProperty(USERNAME));
        dataSource.setPassword(env.getProperty(PASSWORD));
        dataSource.setURL(env.getProperty(URL));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() throws PropertyVetoException {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("project.model");
        factoryBean.setJpaProperties(properties);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        return factoryBean;
    }
}