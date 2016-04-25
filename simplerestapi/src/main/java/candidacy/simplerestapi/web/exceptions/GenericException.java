package candidacy.simplerestapi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used for default exceptions
 * @author david
 *
 */
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = -4957462273321169441L;
	
	public GenericException(String message) {
		super(message);
	}

	@ExceptionHandler({java.sql.SQLException.class})
	@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE)
	public String dataBaseError(){
		return "databaseError";
	}

	
}
