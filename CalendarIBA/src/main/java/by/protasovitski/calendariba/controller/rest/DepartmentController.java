package by.protasovitski.calendariba.controller.rest;

import by.protasovitski.calendariba.domain.entities.Department;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.dto.DepartmentDto;
import by.protasovitski.calendariba.dto.TeacherDto;
import by.protasovitski.calendariba.service.DepartmentService;
import by.protasovitski.calendariba.util.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_DEPARTMENT)
@Api(tags = "departments", description = "Departments API")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find department", notes = "Find the department by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Department found"),
            @ApiResponse(code = 404, message = "Department not found")
    })
    public ResponseEntity<DepartmentDto> findOneDepartment(@PathVariable("id") Long id) {
        Department department = departmentService.findById(id);
        return new ResponseEntity<>(Mapper.map(department, DepartmentDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List departments", notes = "List all departments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Departments found"),
            @ApiResponse(code = 404, message = "Departments not found")
    })
    public ResponseEntity<List<DepartmentDto>> findAllDepartments() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(departments, DepartmentDto.class), HttpStatus.OK);
        }
    }

    @GetMapping("/page" )
    @ApiOperation(value = "Sorted departments page", notes = "Sorted departments page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Departments found"),
            @ApiResponse(code = 404, message = "Departments not found")
    })
    public ResponseEntity<List<DepartmentDto>> findAllDepartmentsPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Department> departments = departmentService.findAll(pageNo,pageSize,sortBy);
        if (departments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(departments, DepartmentDto.class), HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create department", notes = "It permits to create a new department")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Department created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DepartmentDto> newDepartment(@RequestBody DepartmentDto departmentDto) {
        return new ResponseEntity<>(
                Mapper.map(departmentService.save(Mapper.map(departmentDto, Department.class)), DepartmentDto.class),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove department", notes = "It permits to remove a department")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Department removed successfully"),
            @ApiResponse(code = 404, message = "Department not found")
    })
    public ResponseEntity<Void> removeDepartment(@PathVariable("id") Long id) {
        if (departmentService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            departmentService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update department", notes = "It permits to update a department")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Department update successfully"),
            @ApiResponse(code = 404, message = "Department not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentDto departmentDto) {
        if (id != departmentDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (departmentService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            departmentService.save(Mapper.map(departmentDto, Department.class));
            return new ResponseEntity<>(
                    Mapper.map(departmentService.save(Mapper.map(departmentDto, Department.class)), DepartmentDto.class),
                    HttpStatus.OK);
        }
    }
}
