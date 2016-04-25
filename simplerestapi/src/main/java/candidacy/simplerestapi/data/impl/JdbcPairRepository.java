package candidacy.simplerestapi.data.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import candidacy.simplerestapi.data.PairRepository;
import candidacy.simplerestapi.model.Pair;
import candidacy.simplerestapi.web.exceptions.GenericException;
import candidacy.simplerestapi.web.exceptions.InvalidDataException;
import candidacy.simplerestapi.web.exceptions.PairNotFoundException;

/**
 * Class to handle the persistence of a Pair in MySQL using JDBC
 * @author david
 *
 */
@Repository("pairRepository")
public class JdbcPairRepository implements PairRepository {

	private Logger log = LoggerFactory.getLogger(JdbcPairRepository.class);
	private JdbcOperations jdbc;
	
	@Autowired
	public JdbcPairRepository(JdbcOperations jdbc) {
    	this.jdbc = jdbc;
	}
	
	public void update(Pair pair) throws PairNotFoundException{
		String sql = "UPDATE pair SET repository=? WHERE email=?";
		int rows = jdbc.update(sql, pair.getRepository(), pair.getEmail());
		if (rows==0)
			throw new PairNotFoundException(pair.getEmail());
	}
	
	public void insert(Pair pair) {
		Pair.checkPair(pair);
		try{
			Pair existingPair = this.findByEmail(pair.getEmail());
			if (!existingPair.getRepository().equals(pair.getRepository()))
				update(pair);
			return;
		}catch (PairNotFoundException e){}
		String sql = "INSERT INTO pair (email, repository) VALUES (?, ?)";
		try{
			jdbc.update(sql, pair.getEmail(), pair.getRepository());
		}catch (DataAccessException e){
			log.error("inserting into database : {}", e.getMessage());
			log.debug("{}", e);
			switch (e.getMessage()){
			case "Column 'email' cannot be null" : throw new InvalidDataException("NULL_EMAIL");
			default: throw new GenericException("MYSQL_SYNTAX_EXCEPTION");
			}
		}
	}
	
	public Pair findByEmail(String email) throws PairNotFoundException {
		String sql = "SELECT email, repository FROM pair WHERE email = ?";
		try{
			return jdbc.queryForObject(sql, new PairRowMapper(), email);
		} catch (EmptyResultDataAccessException e) {
			throw new PairNotFoundException(email);
		} catch (Exception e){
			log.error("Could not perform query");
			log.debug("{}",e);
			throw new GenericException("MYSQL_SYNTAX_EXCEPTION");
		}
	}
	
	private static class PairRowMapper implements RowMapper<Pair>{
		@Override
		public Pair mapRow(ResultSet rs, int rowNum) throws SQLException {
			Pair pair = new Pair();
			pair.setEmail(rs.getString("email"));
			pair.setRepository(rs.getString("repository"));
			return pair;
		}
	}

	@Override
	public void delete(String email) throws PairNotFoundException {
		this.findByEmail(email);
		String sql = "DELETE FROM pair WHERE email = ?";
		jdbc.update(sql, email);
	}
}
