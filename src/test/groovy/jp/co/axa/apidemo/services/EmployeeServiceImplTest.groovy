package jp.co.axa.apidemo.services

import jp.co.axa.apidemo.EmployeeDbSpec
import jp.co.axa.apidemo.entities.Employee
import jp.co.axa.apidemo.exceptions.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import jp.co.axa.apidemo.utility.TableSet


@SpringBootTest
class EmployeeServiceImplTest extends EmployeeDbSpec {

  @Autowired
  EmployeeServiceImpl sut

  def setup() {
    deleteTable('EMPLOYEE' )
  }

  def 'Retrieve all Employees'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    def result = sut.retrieveEmployees()

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
    def result = sut.retrieveEmployees()

    then:
    assert(result.isEmpty())
  }

  def 'Get one Employee'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    def result = sut.getEmployee(2)

    then:
    assert(result)
    verifyAll(result) {
      name == 'Barak Obama'
      department == 'HR'
      salary == 34000
    }
  }

  def 'Get one Employee that doesnt exists'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    sut.getEmployee(3)

    then:
    thrown BadRequestException
  }

  def 'Save an Employees successfully'() {
    given:
    def emp = new Employee()
    emp.setName('Cleopatra')
    emp.setDepartment('IT')
    emp.setSalary(450000)

    when:
    sut.saveEmployee(emp)
    def result = sut.retrieveEmployees()

    then:
    assert(result)
    verifyAll(result.get(0)) {
      name == 'Cleopatra'
      department == 'IT'
      salary == 450000
    }
  }

  def 'Delete an Employee'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when: "Get all employees currently in the table"
    def result = sut.retrieveEmployees()

    then:
    result.size() == 2

    when: "Delete an employee, then retrieve all the employees in the table"
    sut.deleteEmployee(1)
    result = sut.retrieveEmployees()

    then:
    result.size() == 1
  }

  def 'Delete Employee then try to retrieve deleted employee'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    sut.deleteEmployee(1)
    def result = sut.retrieveEmployees()

    then:
    result.size() == 1

    when:
    sut.getEmployee(1)

    then:
    thrown BadRequestException
  }

  def 'Delete Employee that doesnt exist'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    when:
    sut.deleteEmployee(3)

    then:
    thrown BadRequestException
  }

  def 'Update an Employees successfully'() {
    given:
    TableSet.insert sql, 'EMPLOYEE', {
      ID | DEPARTMENT | EMPLOYEE_NAME | EMPLOYEE_SALARY
      '1'| 'SALES'   | 'Ivan Great'  | 100000
      '2'| 'HR'      | 'Barak Obama' | 34000
    }

    def emp = new Employee()
    emp.setId(1)
    emp.setName('Ivan the Great')
    emp.setDepartment('SALES')
    emp.setSalary(550000)

    when:
    sut.updateEmployee(emp)
    def result = sut.getEmployee(1)

    then:
    assert(result)
    verifyAll(result) {
      name == 'Ivan the Great'
      department == 'SALES'
      salary == 550000
    }
  }

  def 'Update an Employees that does not exist'() {
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

    when:
    sut.updateEmployee(emp)

    then:
    thrown BadRequestException
  }

}
