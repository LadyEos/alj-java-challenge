package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.BadRequestException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    public List<Employee> retrieveEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.orElseThrow(()-> new BadRequestException("Employee does not exist"));
    }

    @Transactional
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long employeeId){
        Employee employee = getEmployee(employeeId);
        employeeRepository.delete(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee) {
        Optional<Employee> optEmp = employeeRepository.findById(employee.getId());
        if(optEmp.isPresent())
            employeeRepository.save(employee);
        else
            throw new BadRequestException("Employee does not exist");
    }
}
