package candidacy.simplerestapi.web.exceptions;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * This controller intercepts the exceptions and configures the final 
 * answer to the user
 * @author david
 *
 */
@ControllerAdvice
public class ExceptionController {
	
	@Autowired
	ApplicationContext context;
	
	@ExceptionHandler(PairNotFoundException.class)
	public ResponseEntity<String> handleContactNotFoundException(PairNotFoundException e) {
		String error = e.getMessage();
		return new ResponseEntity<String>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<String> handleGenericException(GenericException e) {
		String error = e.getMessage();
		return new ResponseEntity<String>(context.getMessage(error, null, Locale.ENGLISH), HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<String> handleContactNotFoundException(InvalidDataException e) {
		String error = context.getMessage(e.getMessage(), null, Locale.ENGLISH);;
		return new ResponseEntity<String>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<String> handleHttpException(HttpClientErrorException e){
		String message = context.getMessage("HTTP_CLIENT_ERROR_EXCEPTION", null, Locale.ENGLISH);
		return new ResponseEntity<String>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(MySQLSyntaxErrorException.class)
	public ResponseEntity<String> handleHttpMysqlSyntaxException(MySQLSyntaxErrorException e){
		String message = context.getMessage("MYSQL_SYNTAX_EXCEPTION", null, Locale.ENGLISH);
		return new ResponseEntity<String>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
}
