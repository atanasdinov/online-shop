package com.scalefocus.shop.configuration;

import com.scalefocus.shop.utility.ScriptRunner;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * <b>Database configuration class.</b>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.scalefocus.shop.repository"})
@PropertySource(value = {"classpath:application.properties"})
public class DatabaseConfig {

    private static final String DB_DRIVER = "db.driver";
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";

    @Autowired
    private Environment env;

    /**
     * DataSource configuration bean. Sets Username, Password and DB_URL.
     *
     * @return configured {@link HikariDataSource} object.
     */
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(env.getProperty(DB_URL));
        dataSource.setUsername(env.getProperty(DB_USERNAME));
        dataSource.setPassword(env.getProperty(DB_PASSWORD));
        dataSource.setDriverClassName(env.getProperty(DB_DRIVER));

        return dataSource;
    }

    /**
     * {@link LocalContainerEntityManagerFactoryBean} configuration bean.
     *
     * @return configured factory bean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        Properties properties = new Properties();

        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQL9Dialect");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.scalefocus.shop.model");
        factoryBean.setJpaProperties(properties);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        return factoryBean;
    }

    @Bean(name = "tableCreation")
    public Void tableCreation() throws SQLException, IOException {
        Connection connection = dataSource().getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
        scriptRunner.runScript(new BufferedReader(new FileReader("src/main/resources/schema.sql")));
        return null;
    }
}