package candidacy.simplerestapi.web.exceptions;

/**
 * Used to react after a Pair has not been found
 * @author david
 *
 */
public class PairNotFoundException extends Exception {

	private static final long serialVersionUID = 5401449380562918703L;
	private String email;
	
	public PairNotFoundException(String email) {
		super();
		this.email = email;
	}
	
	@Override
	public String getMessage(){
		return this.email + " not found";
	}
}
