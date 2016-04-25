package candidacy.simplerestapi.data;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import candidacy.simplerestapi.conf.DataSourceConfig;
import candidacy.simplerestapi.data.impl.JdbcPairRepository;
import candidacy.simplerestapi.model.Pair;
import candidacy.simplerestapi.web.exceptions.InvalidDataException;
import candidacy.simplerestapi.web.exceptions.PairNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=DataSourceConfig.class)
@WebAppConfiguration
public class PairRepositoryTest {

	@Autowired
	private DataSource dataSource;

	private String email;
	private PairRepository pairRepository;
	boolean pendingDelete = false;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	public void initialize(){
		long now = System.currentTimeMillis();
		this.email = now + "@test.forbiddentld";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		pairRepository = new JdbcPairRepository(jdbcTemplate);
	}
	
	@Test
	public void updateNotFound() throws PairNotFoundException{
		Pair pair = new Pair();
		pair.setEmail(email);
		pair.setRepository(email + "Repository");
		exception.expect(PairNotFoundException.class);
		pairRepository.update(pair);
	}
	
	@Test
	public void insertOk(){
		Pair pair = new Pair();
		pair.setEmail(email);
		pair.setRepository(email + "Repository");
		pairRepository.insert(pair);
		pendingDelete = true;
	}
	
	@Test
	public void insertNull(){
		exception.expect(InvalidDataException.class);
		pairRepository.insert(null);
	}
	
	@Test
	public void insertNullEmail(){
		Pair pair = new Pair();
		pair.setEmail(null);
		pair.setRepository(email + "Repository");
		exception.expect(InvalidDataException.class);
		pairRepository.insert(pair);
		pendingDelete = true;
	}
	
	@Test
	public void insertNullRepository(){
		Pair pair = new Pair();
		pair.setEmail(email);
		pair.setRepository(null);
		exception.expect(InvalidDataException.class);
		pairRepository.insert(pair);
		pendingDelete = true;
	}
	
	@Test
	public void insertIncorrectEmail(){
		Pair pair = new Pair();
		pair.setEmail("mycleanmail");
		pair.setRepository(email + "Repository");
		exception.expect(InvalidDataException.class);
		pairRepository.insert(pair);
		pendingDelete = true;
	}
	
	@Test
	public void insertIncorrectRepository(){
		Pair pair = new Pair();
		pair.setEmail(email);
		pair.setRepository("    !");
		exception.expect(InvalidDataException.class);
		pairRepository.insert(pair);
		pendingDelete = true;
	}
	
	@After
	public void deleteTestAddress() throws PairNotFoundException{
		if (this.pendingDelete)
			pairRepository.delete(this.email);
	}
}
