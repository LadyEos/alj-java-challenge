package jp.co.axa.apidemo.validators

import jp.co.axa.apidemo.entities.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import spock.lang.Specification

class EmployeePutValidatorTest extends Specification {

  @Autowired
  EmployeePutValidator vut

  Employee employee

  def 'Validate an Employee successfully'() {
    given:
    employee = new Employee()
    employee.setId(1)
    employee.setName('Cleopatra')
    employee.setDepartment('IT')
    employee.setSalary(450000)

    vut = new EmployeePutValidator()
    Errors errors = new BeanPropertyBindingResult(employee, "employee")
    vut.validate(employee, errors)

    expect:
    !errors.hasErrors()
  }

  def 'Validate an Employee unsuccessfully'() {
    given:
    employee = new Employee()
    employee.setName(' ')
    employee.setDepartment('')
    employee.setSalary()

    vut = new EmployeePutValidator()
    Errors errors = new BeanPropertyBindingResult(employee, "employee")
    vut.validate(employee, errors)

    expect:
    verifyAll(errors) {
      hasErrors()
      hasFieldErrors('id')
      getFieldError('id').getCode() == 'error.id'
      hasFieldErrors('name')
      getFieldError('name').getCode() == 'error.name'
      hasFieldErrors('department')
      getFieldError('department').getCode() == 'error.department'
      hasFieldErrors('salary')
      getFieldError('salary').getCode() == 'error.salary'
    }
  }

}
