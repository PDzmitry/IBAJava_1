package by.protasovitski.calendariba.controller.rest;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import by.protasovitski.calendariba.domain.repository.TeacherRepository;
import by.protasovitski.calendariba.dto.DisciplineDto;
import by.protasovitski.calendariba.dto.TeacherDto;
import by.protasovitski.calendariba.service.TeacherService;
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
@CrossOrigin(origins = ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_TEACHER)
@Api(tags = "teachers", description = "Teacher API")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find teacher", notes = "Find the Teacher by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher found"),
            @ApiResponse(code = 404, message = "Teacher not found")
    })
    public ResponseEntity<TeacherDto> findOneTeacher(@PathVariable("id") Long id) {
            Teacher teacher = teacherService.findById(id);
            return new ResponseEntity<>(Mapper.map(teacher, TeacherDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List teachers", notes = "List all teachers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teachers found"),
            @ApiResponse(code = 404, message = "Teachers not found")
    })
    public ResponseEntity<List<TeacherDto>> findAllTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(teachers, TeacherDto.class), HttpStatus.OK);
        }
    }
    @GetMapping("/page" )
    @ApiOperation(value = "Sorted teachers page", notes = "Sorted teachers page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teachers found"),
            @ApiResponse(code = 404, message = "Teachers not found")
    })
    public ResponseEntity<List<TeacherDto>> findAllTeachersPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Teacher> teachers = teacherService.findAll(pageNo,pageSize,sortBy);
        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(teachers, TeacherDto.class), HttpStatus.OK);
        }
    }
    @GetMapping("/types")
    @ApiOperation(value = "List types_of_employment", notes = "List all types_of_employment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_employment found"),
            @ApiResponse(code = 404, message = "Types_of_employment not found")
    })
    public ResponseEntity<List<TypeOfEmployment>> findAllTypesOfEmployment() {
        if (TypeOfEmployment.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfEmployment.orderedValues, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create teacher", notes = "It permits to create a new teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Teacher created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<TeacherDto> newTeacher(@RequestBody TeacherDto teacherDto) {
        return new ResponseEntity<>(
                Mapper.map(this.teacherService.save(Mapper.map(teacherDto, Teacher.class)), TeacherDto.class),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove teacher", notes = "It permits to remove a teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Teacher removed successfully"),
            @ApiResponse(code = 404, message = "Teacher not found")
    })
    public ResponseEntity<Void> removeTeacher(@PathVariable("id") Long id) {
        if (teacherService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            teacherService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update teacher", notes = "It permits to update a teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher update successfully"),
            @ApiResponse(code = 404, message = "Teacher not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable("id") Long id, @RequestBody TeacherDto teacherDto) {
        Teacher oldTeacher;
        if (id != teacherDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if ((oldTeacher=teacherService.findById(id)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Teacher teacher = Mapper.map(teacherDto, Teacher.class);
            teacher.setDisciplines(oldTeacher.getDisciplines());
            return new ResponseEntity<>(
                    Mapper.map(teacherService.save(teacher), TeacherDto.class),
                    HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}/disciplines")
    @ApiOperation(value = "List disciplines of teacher", notes = "List all disciplines of teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher and disciplines found"),
            @ApiResponse(code = 404, message = "Teacher or disciplines of teacher not found")
    })
    public ResponseEntity<List<DisciplineDto>> findAllDisciplinesOfTeacher(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.findById(id);
        if (teacher==null || teacher.getDisciplines().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            List<Discipline> disciplines = new ArrayList<>(teacher.getDisciplines());
            return new ResponseEntity<>
                    (Mapper.mapAll(disciplines,DisciplineDto.class),HttpStatus.OK);
        }
    }

    @PutMapping(value = "/{id}/disciplines/{code}")
    @ApiOperation(value = "Add  the discipline to the teacher", notes = "It permits to add  the discipline to the teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Discipline added"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DisciplineDto> addDisciplineToTeacher(@PathVariable("id") Long id, @PathVariable("code") Long code) {
        teacherService.addTeacherDiscipline(id,code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/disciplines/{code}")
    @ApiOperation(value = "Remove discipline from the teacher", notes = "It permits to remove the discipline from the teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Discipline removed successfully"),
            @ApiResponse(code = 404, message = "Discipline not found")
    })
    public ResponseEntity<Void> removeDisciplineOfTeacher(@PathVariable("id") Long id, @PathVariable("code") Long code) {
     /*   if (teacherService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Teacher teacher = teacherService.findById(id);
            teacher.getDisciplines()
                    .stream()
                    .filter(discipline -> discipline.getId().equals(code))
                    .findFirst()
                    .ifPresent(discipline -> teacher.getDisciplines().remove(discipline));
            return new ResponseEntity<>(HttpStatus.GONE);
        }*/
     teacherService.delTeacherDiscipline(id,code);
     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
