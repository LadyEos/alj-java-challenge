package jp.co.axa.apidemo.validators;

import jp.co.axa.apidemo.entities.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class EmployeePostValidator implements Validator {


  @Override
  public boolean supports(Class<?> clazz) {
    return Employee.class.isAssignableFrom(clazz);
  }

  /**
   *
   * @param target the object that is to be validated
   * @param errors contextual state about the validation process
   *
   *               Will check if the field is empty or only whitespace and will add an entry to the Errors object.
   */
  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name is required.");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "salary", "error.salary", "Salary is required.");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "department", "error.department", "Department is required.");
  }
}
