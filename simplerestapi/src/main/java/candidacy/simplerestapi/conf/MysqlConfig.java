package candidacy.simplerestapi.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Loads the configuration parameters from mysql.properties
 * @author david
 *
 */
@Configuration
@ComponentScan(basePackages = { "candidacy.simplerestapi" })
@PropertySource("classpath:mysql.properties")
public class MysqlConfig {
    
	@Value("${driver_class_name}")
	private String driverClassName;
	
	@Value("${url}")
	private String url;
	
	@Value("${username}")
	private String username;
	
	@Value("${password}")
	private String password;
	
	public String getDriverClassName() {
		return driverClassName;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
