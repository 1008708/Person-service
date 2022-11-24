package telran.java2022.person.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class PersonNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8647573866454320680L;

}
