package vn.trinhtung.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MediaTypeValidator implements ConstraintValidator<ValidMediaType, Object> {
	private Set<String> values;

	@Override
	public void initialize(ValidMediaType constraintAnnotation) {
		values = Arrays.stream(constraintAnnotation.values()).collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		System.out.println("OK");
		System.out.println(values.toString());
		if (value instanceof MultipartFile) {
			System.out.println("OK 1");
			System.out.println(values.toString());
			MultipartFile file = (MultipartFile) value;
			return values.contains(MediaType.ALL_VALUE) || values.contains(file.getContentType());
		} else if (value instanceof Collection<?>) {
			System.out.println("OK 2");
			@SuppressWarnings("unchecked")
			Collection<MultipartFile> files = (Collection<MultipartFile>) value;
			for (MultipartFile file : files) {
				if (!(values.contains(MediaType.ALL_VALUE)
						|| values.contains(file.getContentType()))) {
					return false;
				}
			}
		}
		return false;
	}

//	@Override
//	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
//		System.out.println("OK");
//		System.out.println(values.toString());
//		return value == null || values.contains(MediaType.ALL_VALUE)
//				|| values.contains(value.getContentType());
//	}

//	private String allowed;
//
//	@Override
//	public void initialize(ValidMediaType constraintAnnotation) {
//		allowed = constraintAnnotation.value();
//	}
//
//	@Override
//	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
//		return value == null || allowed.equals(MediaType.ALL_VALUE)
//				|| allowed.equals(value.getContentType());
//	}
}
