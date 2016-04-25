package candidacy.simplerestapi.test;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import candidacy.simplerestapi.data.impl.JdbcPairRepository;
import candidacy.simplerestapi.model.Pair;
import candidacy.simplerestapi.web.PairRestController;

public class PairRestControllerTest  {

	private String URL 	= "/rest";
	private String EMAIL = "user@domain.tld";
	JdbcPairRepository pairRepositoryMock = null;
	MockMvc mockMvc;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup(){
		pairRepositoryMock = mock(JdbcPairRepository.class);
		PairRestController pairRestController = new PairRestController(pairRepositoryMock);
		mockMvc = standaloneSetup(pairRestController).build();
	}
	
	private String jsonPair(){
		return "{\"email\": \"some@email.com\", \"repository\": \"repository_name\"}";
	}
	
	@Test
	public void validatePost() throws Exception {
		Pair pair = new Pair();
		pair.setEmail(EMAIL);
		pair.setRepository("userDomainRepository");
		pairRepositoryMock.insert(pair);
		PairRestController restController = new PairRestController(pairRepositoryMock);
		MockMvc mockMvc = standaloneSetup(restController).build();
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPair())
			).andExpect(status().isOk());
	}

}
