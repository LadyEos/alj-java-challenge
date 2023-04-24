package jp.co.axa.apidemo.controllers
import jp.co.axa.apidemo.EmployeeDbSpec
import jp.co.axa.apidemo.entities.Employee
import jp.co.axa.apidemo.exceptions.BadRequestException
import jp.co.axa.apidemo.exceptions.FailedValidationException
import jp.co.axa.apidemo.services.EmployeeServiceImpl
import jp.co.axa.apidemo.utility.TableSet
import jp.co.axa.apidemo.validators.EmployeePostValidator
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.ObjectError

@Import(EmployeePostValidator.class)
@SpringBootTest
class EmployeeControllerTest extends EmployeeDbSpec {

  @Autowired
  EmployeeController sut

  @Mock
  private BindingResult mockBindingResult

  def setup() {
    deleteTable('EMPLOYEE')
  }

  def 'Retrieve all Employees'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    def result = sut.getEmployees()

    then:
    assert(result)
    verifyAll(result.get(0)) {
      name == 'Ivan Great'
      department == 'SALES'
      salary == 100000
    }

    verifyAll(result.get(1)) {
      name == 'Barak Obama'
      department == 'HR'
      salary == 34000
    }
  }

  def 'Retrieve all Employees when there are none'() {
    when:
    def result = sut.getEmployees()

    then:
    assert(result.isEmpty())
  }

  def 'Save an Employees successfully'() {
    given:
    def emp = new Employee()
    emp.setName('Cleopatra')
    emp.setDepartment('IT')
    emp.setSalary(450000)

    Mock(BindingResult){
      hasErrors() >> false
    }

    when:
    sut.saveEmployee(emp, mockBindingResult)

    def result = sut.getEmployees()

    then:
    assert(result)
    result.size() == 1
  }

  def 'Save an Employees unsuccessfully due to failed validation'() {
    given:
    def emp = new Employee()
    emp.setName('Cleopatra')
    emp.setDepartment('')
    emp.setSalary(100000)

    ObjectError error = new ObjectError("department","Department cant be empty")
    def listOfErrors = new ArrayList<ObjectError>()
    listOfErrors.add(error)

    def bindingResult = Mock(BindingResult)
    bindingResult.hasErrors() >> true
    bindingResult.getAllErrors() >> listOfErrors

    when:
    sut.saveEmployee(emp, bindingResult)

    then:
    thrown FailedValidationException

  }

  def 'Update an Employees successfully'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    def emp = new Employee()
    emp.setId(2)
    emp.setName('Cleopatra')
    emp.setDepartment('IT')
    emp.setSalary(450000)

    Mock(BindingResult){
      hasErrors() >> false
    }

    when:
    sut.updateEmployee(emp, 2, mockBindingResult)

    def result = sut.getEmployee(2)

    then:
    assert(result)
    verifyAll(result) {
      name == 'Cleopatra'
      department == 'IT'
      salary == 450000
    }
  }

  def 'Update an Employees unsuccessfully because it doesnt exist'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    def emp = new Employee()
    emp.setId(3)
    emp.setName('Cleopatra')
    emp.setDepartment('IT')
    emp.setSalary(450000)

    Mock(BindingResult){
      hasErrors() >> false
    }

    when:
    sut.updateEmployee(emp, 3, mockBindingResult)

    then:
    thrown BadRequestException
  }

  def 'Update an Employees unsuccessfully the ids do not match'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    def emp = new Employee()
    emp.setId(2)
    emp.setName('Cleopatra')
    emp.setDepartment('IT')
    emp.setSalary(100000)

    def bindingResult = Mock(BindingResult)
    bindingResult.hasErrors() >> false

    when:
    sut.updateEmployee(emp, 3, bindingResult)

    then:
    thrown FailedValidationException
  }

  def 'Update an Employees unsuccessfully due to validation'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    def emp = new Employee()
    emp.setId(2)
    emp.setName('Cleopatra')
    emp.setDepartment('')
    emp.setSalary(100000)

    ObjectError error = new ObjectError("department","Department cant be empty")
    def listOfErrors = new ArrayList<ObjectError>()
    listOfErrors.add(error)

    def bindingResult = Mock(BindingResult)
    bindingResult.hasErrors() >> true
    bindingResult.getAllErrors() >> listOfErrors

    when:
    sut.updateEmployee(emp, 2, bindingResult)

    then:
    thrown FailedValidationException

  }

}
