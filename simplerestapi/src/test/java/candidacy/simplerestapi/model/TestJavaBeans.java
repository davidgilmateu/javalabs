package candidacy.simplerestapi.model;

import org.junit.Assert;
import org.junit.Test;

import candidacy.simplerestapi.model.Pair;

public class TestJavaBeans  {

	private String TEST_EMAIL = "name@host.tld";
	private String TEST_REPOSITORY = "test_repository";
	
	@Test
	public void testCreate(){
		Pair pair = new Pair();
		pair.setEmail(TEST_EMAIL);
		pair.setRepository(TEST_REPOSITORY);
		Assert.assertEquals(pair.getEmail(), TEST_EMAIL);
		Assert.assertEquals(pair.getRepository(), TEST_REPOSITORY);
	}
	
}
