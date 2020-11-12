package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.Department;
import by.protasovitski.calendariba.domain.entities.Employee;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import by.protasovitski.calendariba.dto.DepartmentDto;
import by.protasovitski.calendariba.dto.EmployeeDto;
import by.protasovitski.calendariba.service.EmployeeService;
import by.protasovitski.calendariba.util.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_EMPLOYEE)
@Api(tags = "employees", description = "Employees API")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find employee", notes = "Find the employee by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee found"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    public ResponseEntity<EmployeeDto> findOneEmployee(@PathVariable("id") Long id) {
        Employee employee = employeeService.findById(id);
        EmployeeDto employeeDto = Mapper.map(employee, EmployeeDto.class);
        if (employee.getDepartment()!=null){
            employeeDto.setDepartmentDto(Mapper.map(employee.getDepartment(), DepartmentDto.class));
        }
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List employees", notes = "List all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employees found"),
            @ApiResponse(code = 404, message = "Employees not found")
    })
    public ResponseEntity<List<EmployeeDto>> findAllEmployee() {
        List<Employee> employees = employeeService.findAll();
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<EmployeeDto> employeeDtoList = new ArrayList<>();
            for (Employee employee:employees){
                EmployeeDto employeeDto = Mapper.map(employee, EmployeeDto.class);
                if (employee.getDepartment()!=null){
                    employeeDto.setDepartmentDto(Mapper.map(employee.getDepartment(), DepartmentDto.class));
                }
                employeeDtoList.add(employeeDto);
            }
            return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);
        }
    }
    @GetMapping("/types")
    @ApiOperation(value = "List types_of_position", notes = "List all types_of_position")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_position found"),
            @ApiResponse(code = 404, message = "Types_of_position not found")
    })
    public ResponseEntity<List<TypeOfPosition>> findAllTypeOfPosition() {
        if (TypeOfPosition.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfPosition.orderedValues, HttpStatus.OK);
        }
    }

    @GetMapping("/page" )
    @ApiOperation(value = "Sorted employees page", notes = "Sorted employees page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employees found"),
            @ApiResponse(code = 404, message = "Employees not found")
    })
    public ResponseEntity<List<EmployeeDto>> findAllEmployeesPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Employee> employees = employeeService.findAll(pageNo,pageSize,sortBy);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<EmployeeDto> employeeDtoList = new ArrayList<>();
            for (Employee employee:employees){
                EmployeeDto employeeDto = Mapper.map(employee, EmployeeDto.class);
                if (employee.getDepartment()!=null){
                    employeeDto.setDepartmentDto(Mapper.map(employee.getDepartment(), DepartmentDto.class));
                }
                employeeDtoList.add(employeeDto);
            }
            return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create employee", notes = "It permits to create a new employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<EmployeeDto> newEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee newEmployee = Mapper.map(employeeDto, Employee.class);
        if(employeeDto.getDepartmentDto()!=null){
            Department department = Mapper.map(employeeDto.getDepartmentDto(),Department.class);
            newEmployee.setDepartment(department);
        }
        Employee employee = employeeService.save(newEmployee);
        employeeDto = Mapper.map( employee, EmployeeDto.class);
        if (employee.getDepartment()!=null){
            employeeDto.setDepartmentDto(Mapper.map(employee.getDepartment(), DepartmentDto.class));
        }
        return new ResponseEntity<>(employeeDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove employee", notes = "It permits to remove a employee")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Employee removed successfully"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    public ResponseEntity<Void> removeEmployee(@PathVariable("id") Long id) {
        if (employeeService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            employeeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update employee", notes = "It permits to update a employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee update successfully"),
            @ApiResponse(code = 404, message = "Employee not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDto employeeDto) {
        if (id != employeeDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (employeeService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Employee newEmployee = Mapper.map(employeeDto, Employee.class);
            if(employeeDto.getDepartmentDto()!=null){
                Department department = Mapper.map(employeeDto.getDepartmentDto(),Department.class);
                newEmployee.setDepartment(department);
            }
            Employee employee = employeeService.save(newEmployee);
            employeeDto = Mapper.map( employee, EmployeeDto.class);
            if (employee.getDepartment()!=null){
                employeeDto.setDepartmentDto(Mapper.map(employee.getDepartment(), DepartmentDto.class));
            }
            return new ResponseEntity<>(employeeDto,HttpStatus.OK);
        }
    }
}
