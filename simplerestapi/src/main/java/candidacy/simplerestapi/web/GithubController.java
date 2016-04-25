package candidacy.simplerestapi.web;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import candidacy.simplerestapi.conf.GithubConfig;
import candidacy.simplerestapi.data.GithubRepository;
import candidacy.simplerestapi.model.GithubProfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This controller gives the tools to retrieve information from
 * github.com using its API
 * @author david
 *
 */
@RestController
@ContextConfiguration(classes=GithubConfig.class)
@RequestMapping("/github")
public class GithubController {

	@Autowired
	private GithubRepository githubRepository;
	
	@Autowired
	private GithubConfig githubConfig;
	
	public GithubController(){
		super();
	}
	
	public GithubController(GithubRepository githubRepository, GithubConfig githubConfig) {
		super();
		this.githubRepository = githubRepository;
		this.githubConfig = githubConfig;
	}

	private GithubProfile[] relevantProfiles(GithubProfile[] firstPage){
		if (firstPage==null||firstPage.length==0)
			return new GithubProfile[]{};
		int numProfiles = Math.min(githubConfig.getMaxRepos(), firstPage.length);
		return (GithubProfile[]) Arrays.asList(firstPage)
				.subList(0, numProfiles).toArray(new GithubProfile[numProfiles]);
	}
	
	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GithubProfile[] getProfileRepositories(@RequestParam String city) {
		ObjectMapper jacksonMapper = new ObjectMapper();
		jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		GithubProfile[] firstPage = githubRepository.getTopProfilesByRepositories(city);
		GithubProfile[] profiles = relevantProfiles(firstPage);
		githubRepository.populateRepositories(profiles);
		return profiles;
	}
	
}
