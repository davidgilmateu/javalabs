package candidacy.simplerestapi.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Contains the data to configure activity on Github, taken from github.properties
 * @author david
 *
 */
@Configuration
@ComponentScan(basePackages = { "candidacy.simplerestapi" })
@PropertySource("classpath:github.properties")
public class GithubConfig {
	
	@Value("${max_repos}")
	private Integer maxRepos;
	
	@Value("${url_profiles}")
	private String urlProfiles;
	
	@Value("${url_repositories}")
	private String urlRepositories;
	
	public Integer getMaxRepos() {
		return maxRepos;
	}

	public void setMaxRepos(Integer maxRepos) {
		this.maxRepos = maxRepos;
	}

	public String getUrlProfiles() {
		return urlProfiles;
	}

	public void setUrlProfiles(String urlProfiles) {
		this.urlProfiles = urlProfiles;
	}

	public String getUrlRepositories() {
		return urlRepositories;
	}

	public void setUrlRepositories(String urlRepositories) {
		this.urlRepositories = urlRepositories;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
