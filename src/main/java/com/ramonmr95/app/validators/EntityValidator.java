package com.ramonmr95.app.validators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EntityValidator<T> {

	public Map<String, Set<String>> getEntityValidationErrors(T entity) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> validationErrors = validator.validate(entity);
		Set<String> errorsSet = new HashSet<String>();

		for (ConstraintViolation<T> constraintViolation : validationErrors) {
			errorsSet.add(constraintViolation.getMessage());
		}

		Map<String, Set<String>> errors = new HashMap<String, Set<String>>();
		errors.put("errors", errorsSet);
		return errors;
	}

	public String getEntityValidationErrorsString(T entity) {
		ObjectMapper mapper = new ObjectMapper();
		String errs = null;
		try {
			errs = mapper.writeValueAsString(getEntityValidationErrors(entity));
		} catch (JsonProcessingException e) {
			errs = "Validation errors";
		}
		return errs;
	}

	public boolean isEntityValid(T entity) {
		return this.getEntityValidationErrors(entity).get("errors").isEmpty();
	}
}
