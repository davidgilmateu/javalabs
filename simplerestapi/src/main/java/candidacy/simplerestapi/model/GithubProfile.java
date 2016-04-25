package candidacy.simplerestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Information about a Github user profile, retrieved from github.com
 * @author david
 *
 */
public class GithubProfile {

	private String login;
	
	private String repos_url;
	
	private String[] repositories;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@JsonIgnore(true)
	public String getRepos_url() {
		return repos_url;
	}

	public void setRepos_url(String repos_url) {
		this.repos_url = repos_url;
	}

	public String[] getRepositories() {
		return repositories;
	}

	public void setRepositories(String[] repositories) {
		this.repositories = repositories;
	}

}