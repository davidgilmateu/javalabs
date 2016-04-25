package candidacy.simplerestapi.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import candidacy.simplerestapi.conf.AppConfig;
import candidacy.simplerestapi.conf.GithubConfig;
import candidacy.simplerestapi.data.impl.JsonGithubRepository;
import candidacy.simplerestapi.model.GithubProfile;
import candidacy.simplerestapi.web.GithubController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class, GithubConfig.class})
@WebAppConfiguration
public class GithubControllerTest  {

	private String URL = "/github";
	private String CITY = "London";
	
	@Autowired
	private GithubConfig githubConfig;
	
	@Test
	public void validateGet() throws Exception {
		GithubProfile ghProfile = new GithubProfile();
		ghProfile.setLogin("alogin");
		ghProfile.setRepositories(new String[]{"first, second, third"});
		GithubProfile[] profiles = new GithubProfile[]{ghProfile};
		JsonGithubRepository ghRepositoryMock = mock(JsonGithubRepository.class);
		when(ghRepositoryMock.getTopProfilesByRepositories(CITY)).thenReturn(profiles);
		MockMvc mockMvc = standaloneSetup(new GithubController(ghRepositoryMock, githubConfig)).build();
		mockMvc.perform(
				get(URL + "?city=" + CITY).accept(MediaType.APPLICATION_JSON)
			).andExpect(status().isOk()
			).andExpect(jsonPath("$", hasSize(1))
			).andExpect(jsonPath("$[0].login", notNullValue())
			).andExpect(jsonPath("$[0].repositories", notNullValue())
		);
    }
	
}
