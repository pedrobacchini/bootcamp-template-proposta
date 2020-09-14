package br.com.zup.bootcamp.proposta.constraint;

import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Constraint(validatedBy = CPFouCNPJ.Validator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CPFouCNPJ {

    String message() default "{br.com.zup.bootcamp.proposta.constraint.CPFouCNPJ.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<CPFouCNPJ, String> {

        public boolean isValid(String documento, ConstraintValidatorContext context) {

            if (documento == null) return true;

            CNPJValidator cnpjValidator = new CNPJValidator();
            CPFValidator cpfValidator = new CPFValidator();

            cnpjValidator.initialize(null);
            cpfValidator.initialize(null);

            return cnpjValidator.isValid(documento, context) || cpfValidator.isValid(documento, context);
        }
    }
}
