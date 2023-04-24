package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.FailedValidationException;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.validators.EmployeePostValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeePostValidator employeePostValidator;

    private final EmployeePostValidator employeePutValidator;

    /**
     * Adds a validator class that is called when the request is received
     * for objects that use @Valid annotation
     * the Put Validator will also handle the id
     */
    @InitBinder
    private void registerValidatorForPostRequest(final HttpServletRequest httpServletRequest, final WebDataBinder binder) {

        if (httpServletRequest.getMethod().equalsIgnoreCase("POST")) {
            binder.setValidator(employeePostValidator);
        }

        if(httpServletRequest.getMethod().equalsIgnoreCase("PUT")){
            binder.setValidator(employeePutValidator);
        }
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping("/employees")
    public void saveEmployee(@Valid Employee employee, final BindingResult validationResult){
        if (validationResult.hasErrors()) {
            String errorMessages = collectAllErrors(validationResult);
            throw new FailedValidationException(errorMessages, "/employees");
        }

        employeeService.saveEmployee(employee);
        System.out.println("Employee Saved Successfully");
    }

    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        System.out.println("Employee Deleted Successfully");
    }

    /**
     * Check that the id given as a parameter (employeeId) is the same as
     * Employee.Id, in addition to the @Valid validations.
     */
    @PutMapping("/employees/{employeeId}")
    public void updateEmployee(@Valid @RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId, final BindingResult validationResult){

        if (validationResult.hasErrors()) {
            String errorMessages = collectAllErrors(validationResult);
            throw new FailedValidationException(errorMessages, "/employees/"+employeeId);
        }

        if(!Objects.equals(employee.getId(), employeeId))
            throw new FailedValidationException("Id does not match employee id", "/employees/"+employeeId);

        employeeService.updateEmployee(employee);
        System.out.println("Employee Updated Successfully");

    }

    private String collectAllErrors(BindingResult validationResult){
        return validationResult.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
    }

}
