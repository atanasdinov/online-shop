package project.configuration;

import com.mysql.cj.jdbc.MysqlDataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.Resource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "project")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = { "com.scalefocus.repository" })

public class WebConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    private static final String Driver = "db.driver";
    private static final String Password = "db.password";
    private static final String Url = "db.url";
    private static final String Username = "db.username";
//    private static final String dbName = "scaleshop";

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
    //Connection and driver settings
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

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("project.model");
        factoryBean.setJpaProperties(properties);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return factoryBean;
    }

    @Bean
    public JpaTransactionManager geJpaTransactionManager() throws PropertyVetoException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(geEntityManagerFactoryBean().getObject());
        return transactionManager;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/classes/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
}