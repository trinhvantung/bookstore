package vn.trinhtung.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Map<String, String> handleBindException(BindException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	public String handleValidationExceptions(ValidationException e) {
		return e.getMessage();
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e) {

		return new ErrorResponse(e.getCode(), e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(OrderException.class)
	public ErrorResponse handleOrderException(OrderException e) {

		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(value = DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleDuplicateResourceException(DuplicateResourceException e) {
		return new ErrorResponse(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(PasswordNotMatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlePasswordNotMatchException(PasswordNotMatchException e) {
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(InvalidParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleInvalidParameterException(InvalidParameterException e) {
		return new ErrorResponse(e.getCode(), e.getMessage());
	}
}
