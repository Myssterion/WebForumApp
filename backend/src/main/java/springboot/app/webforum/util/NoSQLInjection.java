package springboot.app.webforum.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoSQLInjectionValidator.class) 
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSQLInjection {
    String message() default "Invalid input; SQL keywords are not allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}