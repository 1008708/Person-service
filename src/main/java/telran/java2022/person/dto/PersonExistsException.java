package telran.java2022.person.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PersonExistsException extends RuntimeException {

	private static final long serialVersionUID = -8289332537513832567L;

}
