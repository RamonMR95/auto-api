package com.ramonmr95.app.validators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ramonmr95.app.parsers.JsonParser;

public class EntityValidator<T> {
	
	private JsonParser jsonParser = new JsonParser();
	

	public Map<String, Set<String>> getEntityValidationErrors(T entity) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> validationErrors = validator.validate(entity);
		Set<String> errorsSet = new HashSet<>();

		for (ConstraintViolation<T> constraintViolation : validationErrors) {
			errorsSet.add(constraintViolation.getMessage());
		}

		Map<String, Set<String>> errors = new HashMap<>();
		errors.put("errors", errorsSet);
		return errors;
	}

	public String getEntityValidationErrorsString(T entity) {
		return jsonParser.getMapAsJsonFormat(getEntityValidationErrors(entity));
	}

	public boolean isEntityValid(T entity) {
		return this.getEntityValidationErrors(entity).get("errors").isEmpty();
	}

}
