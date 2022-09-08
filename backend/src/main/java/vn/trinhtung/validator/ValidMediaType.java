package vn.trinhtung.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.http.MediaType;

@Constraint(validatedBy = MediaTypeValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMediaType {

	String[] values() default { MediaType.ALL_VALUE };

	String message() default "Media type not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}