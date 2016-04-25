package candidacy.simplerestapi.data.impl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import candidacy.simplerestapi.conf.GithubConfig;
import candidacy.simplerestapi.data.GithubRepository;
import candidacy.simplerestapi.model.GithubProfile;
import candidacy.simplerestapi.model.GithubProfileRepository;
import candidacy.simplerestapi.model.GithubProfiles;

@Repository("githubRepository")
public class JsonGithubRepository implements GithubRepository{

	@Autowired
	GithubConfig githubConfig;
	
	Logger log = LoggerFactory.getLogger(JsonGithubRepository.class);
	RestTemplate restTemplate = new RestTemplate();
	
	private String urlProfiles(){
		return this.githubConfig.getUrlProfiles();
	}
	
	private String urlRepositories(){
		return this.githubConfig.getUrlRepositories();
	}
	
	@Override
	public GithubProfile[] getTopProfilesByRepositories(String city) {
		if (city==null||city.trim().length()==0)
			return new GithubProfile[]{};
		final HashMap<String, String> urlVariables = new HashMap<String, String>(1);
		urlVariables.put("city", city);
		ResponseEntity<GithubProfiles> response = 
				restTemplate.getForEntity(urlProfiles(), 
				GithubProfiles.class, urlVariables);
		GithubProfile[] ret = response.getBody().getItems();
		return ret;
	}
	
	private void populateRepositories(GithubProfile profile){
		final HashMap<String, String> urlVariables = new HashMap<String, String>(1);
		urlVariables.put("login", profile.getLogin());
		ResponseEntity<GithubProfileRepository[]> response = 
				restTemplate.getForEntity(urlRepositories(), 
				GithubProfileRepository[].class, urlVariables);
		GithubProfileRepository[] repos = response.getBody();
		String[] repositories = new String[repos.length];
		for(int i=0; i<repos.length; i++)
			repositories[i] = repos[i].getName();
		profile.setRepositories(repositories);
	}

	@Override
	public void populateRepositories(GithubProfile[] profiles) {
		for(GithubProfile profile : profiles)
			populateRepositories(profile);
	}

}
