package candidacy.simplerestapi.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Creates a new DataSource base in MysqlConfig data
 * @author david
 *
 */
@Configuration
@ComponentScan(basePackages = { "candidacy.simplerestapi" })
public class DataSourceConfig {

	@Autowired
	MysqlConfig mysqlConfig;
	
	@Bean
	public DataSource mysqlDataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(mysqlConfig.getDriverClassName());
        dataSource.setUrl(mysqlConfig.getUrl());
        dataSource.setUsername(mysqlConfig.getUsername());
        dataSource.setPassword(mysqlConfig.getPassword());
        return dataSource;
	}
}
