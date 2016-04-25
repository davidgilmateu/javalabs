package candidacy.simplerestapi.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import candidacy.simplerestapi.web.exceptions.InvalidDataException;

/**
 * Stores all the information of a pair email/repository.
 * Its attributes must follow the patterns stated below.
 * @author david
 *
 */
public class Pair {

	@NotEmpty @Email
	private String email;
	
	@NotEmpty
	private String repository;
	
	public static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static final String REPOSITORY_PATTERN = ".*[A-Za-z0-9].*";
	
	public Pair() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	/**
	 * Checks a pair is correct. Otherwise throws an
	 * InvalidDataException
	 * @param pair to be checked
	 * email must satisfy the pattern declared at the top of this class.
	 * repository must satisfy the pattern declared at the top of this class.
	 */
	public static void checkPair(Pair pair){
		if (pair==null)
			throw new InvalidDataException("PAIR_NULL");
		if (pair.getEmail()==null)
			throw new InvalidDataException("EMAIL_NULL");
		if (!pair.getEmail().matches(EMAIL_PATTERN))
			throw new InvalidDataException("EMAIL_NOT_VALID");
		if (pair.getRepository()==null)
			throw new InvalidDataException("REPOSITORY_NULL");
		if (!pair.getRepository().matches(REPOSITORY_PATTERN))
			throw new InvalidDataException("REPOSITORY_NOT_VALID");
	}
	
	@Override
	public String toString(){
		return email + " : " + repository;
	}
	
}
