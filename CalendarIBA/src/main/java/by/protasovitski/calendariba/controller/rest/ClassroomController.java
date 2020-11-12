package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.Classroom;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfClassroom;
import by.protasovitski.calendariba.dto.ClassroomDto;
import by.protasovitski.calendariba.service.ClassroomService;
import by.protasovitski.calendariba.util.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_CLASSROOM)
@Api(tags = "classrooms", description = "Classrooms API")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find classroom", notes = "Find the Classroom by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Classroom found"),
            @ApiResponse(code = 404, message = "Classroom not found")
    })
    public ResponseEntity<ClassroomDto> findOneClassroom(@PathVariable("id") Long id) {
        Classroom classroom = classroomService.findById(id);
        return new ResponseEntity<>(Mapper.map(classroom, ClassroomDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List classrooms", notes = "List all classrooms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Classrooms found"),
            @ApiResponse(code = 404, message = "Classrooms not found")
    })
    public ResponseEntity<List<ClassroomDto>> findAllClassrooms() {
        List<Classroom> classrooms = classroomService.findAll();
        if (classrooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(classrooms, ClassroomDto.class), HttpStatus.OK);
        }
    }

    @GetMapping("/page")
    @ApiOperation(value = "Sorted classrooms page", notes = "Sorted classrooms page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Classrooms found"),
            @ApiResponse(code = 404, message = "Classrooms not found")
    })
    public ResponseEntity<List<ClassroomDto>> findAllClassroomsPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Classroom> classrooms = classroomService.findAll(pageNo, pageSize, sortBy);
        if (classrooms.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(classrooms, ClassroomDto.class), HttpStatus.OK);
        }
    }
    @GetMapping("/types")
    @ApiOperation(value = "List types_of_classroom", notes = "List all types_of_classroom")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_classroom found"),
            @ApiResponse(code = 404, message = "Types_of_classroom not found")
    })
    public ResponseEntity<List<TypeOfClassroom>> findAllTypesOfClassroom() {
        if (TypeOfClassroom.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfClassroom.orderedValues, HttpStatus.OK);
        }
    }


    @PostMapping
    @ApiOperation(value = "Create classroom", notes = "It permits to create a new classroom")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Classroom created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClassroomDto> newClassroom(@RequestBody ClassroomDto classroomDto) {

        return new ResponseEntity<>(
                Mapper.map(classroomService.save(
                        Mapper.map(classroomDto, Classroom.class))
                        , ClassroomDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove classroom", notes = "It permits to remove a classroom")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Classroom removed successfully"),
            @ApiResponse(code = 404, message = "Classroom not found")
    })
    public ResponseEntity<Void> removeClassroom(@PathVariable("id") Long id) {
        if (classroomService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            classroomService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update classroom", notes = "It permits to update a classroom")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Classroom update successfully"),
            @ApiResponse(code = 404, message = "Classroom not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<ClassroomDto> updateClassroom(@PathVariable("id") Long id, @RequestBody ClassroomDto classroomDto) {
        if (!(id.equals(classroomDto.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (classroomService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(
                    Mapper.map(classroomService.save(
                            Mapper.map(classroomDto, Classroom.class)),
                            ClassroomDto.class), HttpStatus.OK);
        }
    }
}
