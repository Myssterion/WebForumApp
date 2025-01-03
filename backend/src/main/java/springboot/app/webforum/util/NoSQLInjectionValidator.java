package springboot.app.webforum.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoSQLInjectionValidator implements ConstraintValidator<NoSQLInjection, String> {

	private static final String[] FORBIDDEN_SEQUENCES = { "SELECT", "INSERT", "UPDATE", "DELETE", "OR", "--", "/*",
			"*/", "#", "=", ";", "\"", "'" };

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; 
		}
		String[] words = value.split("\\s+");
		for (String seq : FORBIDDEN_SEQUENCES) {
			for (String word : words)
				if (word.toUpperCase().equals(seq)) {
					return false; 
				}
		}
		return true; 
	}
}