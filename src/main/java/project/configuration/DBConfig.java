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
@EnableJpaRepositories(basePackages = { "project/repositories" })
@PropertySource(value={"classpath:application.properties"})
public class DBConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    private static final String Driver = "db.driver";
    private static final String Password = "db.password";
    private static final String Url = "db.url";
    private static final String Username = "db.username";

    private static final String Dialect = "hibernate.dialect";
    private static final String SqlFormat = "hibernate.format_sql";
    private static final String SqlShow = "hibernate.show_sql";
    private static final String Scan = "entitymanager.packages.to.scan";
    private static final String Hbm2DDL = "hibernate.hbm2ddl.auto";

    @Autowired
    private Environment env;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public MysqlDataSource dataSource() throws IllegalStateException, PropertyVetoException {
        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource.setDatabaseName(env.getProperty(dbName));
        dataSource.setUser(env.getProperty(Username));
        dataSource.setPassword(env.getProperty(Password));
        dataSource.setURL(env.getProperty(Url));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean geEntityManagerFactoryBean() throws PropertyVetoException {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql","true");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("project.model");
        factoryBean.setJpaProperties(properties);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return factoryBean;
    }

}