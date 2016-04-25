package candidacy.simplerestapi.data;

import org.junit.Test;
import org.springframework.util.Assert;

import candidacy.simplerestapi.data.impl.JsonGithubRepository;
import candidacy.simplerestapi.model.GithubProfile;

public class JsonGithubRepositoryTest {

	@Test
	public void getTopProfilesByRepositories(){
		GithubRepository githubRepository = new JsonGithubRepository();
		GithubProfile[] profiles = 
				githubRepository.getTopProfilesByRepositories("");
		Assert.notNull(profiles);
	}
	
}
