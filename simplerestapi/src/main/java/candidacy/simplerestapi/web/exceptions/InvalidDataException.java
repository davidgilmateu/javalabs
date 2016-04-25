package candidacy.simplerestapi.web.exceptions;

/**
 * Used to react after any incorrect data has been found
 * @author david
 *
 */
public class InvalidDataException extends RuntimeException {

	private static final long serialVersionUID = 5401449380562918703L;
	private String myMessage;
	
	public InvalidDataException(String myMessage) {
		super();
		this.myMessage = myMessage;
	}
	
	@Override
	public String getMessage(){
		return this.myMessage;
	}
}
