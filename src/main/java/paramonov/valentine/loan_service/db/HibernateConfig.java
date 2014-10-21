package paramonov.valentine.loan_service.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:res/jdbc.properties")
@ComponentScan("paramonov.valentine.loan_service")
class HibernateConfig {
    @Autowired
    private Environment environment;

    @Value("${hikari.dataSource.driverClassName}")
    private String driverClassName;

    @Value("${hikari.dataSource.url}")
    private String dataSourceUrl;

    @Value("${hikari.dataSource.userName}")
    private String dataSourceUserName;

    @Value("${hikari.dataSource.password}")
    private String dataSourcePassword;

    private Logger log;
    private final String[] packagesToScan = {
        "paramonov.valentine.loan_service.db.entities"
    };

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        final String[] hibernatePropertyNames = {
            "hibernate.hbm2ddl.auto",
            "hibernate.dialect",
            "hibernate.globally_quoted_identifiers"
        };
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        final Properties hibernateProperties = getProperties(hibernatePropertyNames);

        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(packagesToScan);
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        final HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUserName);
        dataSource.setPassword(dataSourcePassword);

        return dataSource;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    private Properties getProperties(final String[] propertyNames) {
        final Properties properties = new Properties();

        for(String propertyName : propertyNames) {
            final String property = environment.getProperty(propertyName);
            if(property == null) {
                log.error("Property not found: {}", propertyName);
                continue;
            }
            properties.setProperty(propertyName, property);
        }

        return properties;
    }
}
