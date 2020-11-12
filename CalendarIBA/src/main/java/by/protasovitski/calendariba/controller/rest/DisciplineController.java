package by.protasovitski.calendariba.controller.rest;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.dto.DisciplineDto;
import by.protasovitski.calendariba.dto.TeacherDto;
import by.protasovitski.calendariba.service.DisciplineService;
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
@CrossOrigin(origins = ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_DISCIPLINE)
@Api(tags = "disciplines", description = "Discipline API")
public class DisciplineController {
    @Autowired
    private DisciplineService disciplineService;


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find discipline", notes = "Find the Discipline by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discipline found"),
            @ApiResponse(code = 404, message = "Discipline not found")
    })
    public ResponseEntity<DisciplineDto> findOneDiscipline(@PathVariable("id") Long id) {
        Discipline discipline = disciplineService.findById(id);
        return new ResponseEntity<>(Mapper.map(discipline, DisciplineDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List disciplines", notes = "List all disciplines")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Disciplines found"),
            @ApiResponse(code = 404, message = "Disciplines not found")
    })
    public ResponseEntity<List<DisciplineDto>> findAllDisciplines() {
        List<Discipline> disciplines = disciplineService.findAll();
        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(disciplines, DisciplineDto.class), HttpStatus.OK);
        }
    }

    @GetMapping("/page")
    @ApiOperation(value = "Sorted disciplines page ", notes = "Sorted disciplines page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Disciplines found"),
            @ApiResponse(code = 404, message = "Disciplines not found")
    })
    public ResponseEntity<List<DisciplineDto>> findAllDisciplinesPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Discipline> disciplines = disciplineService.findAll(pageNo,pageSize,sortBy);
        if (disciplines.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(disciplines, DisciplineDto.class), HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create discipline", notes = "It permits to create a new discipline")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Discipline created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DisciplineDto> newDiscipline(@RequestBody DisciplineDto disciplineDto) {
        return new ResponseEntity<>(
                Mapper.map(disciplineService.save(Mapper.map(disciplineDto, Discipline.class)), DisciplineDto.class),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove discipline", notes = "It permits to remove a discipline")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Discipline removed successfully"),
            @ApiResponse(code = 404, message = "Discipline not found")
    })
    public ResponseEntity<Void> removeDiscipline(@PathVariable("id") Long id) {
        if (disciplineService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            disciplineService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update discipline", notes = "It permits to update a discipline")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discipline update successfully"),
            @ApiResponse(code = 404, message = "Discipline not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DisciplineDto> updateDiscipline(@PathVariable("id") Long id, @RequestBody DisciplineDto disciplineDto) {
        if (id != disciplineDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (disciplineService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            disciplineService.save(Mapper.map(disciplineDto, Discipline.class));
            return new ResponseEntity<>(
                    Mapper.map(disciplineService.save(Mapper.map(disciplineDto, Discipline.class)), DisciplineDto.class),
                    HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}/teachers")
    @ApiOperation(value = "List teachers of discipline", notes = "List all teachers of discipline")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discipline and teachers found"),
            @ApiResponse(code = 404, message = "Discipline or teachers of discipline not found")
    })
    public ResponseEntity<List<TeacherDto>> findAllTeachersOfDiscipline(@PathVariable("id") Long id) {
        Discipline discipline = disciplineService.findById(id);
        if (discipline == null || discipline.getTeachers().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>
                    (Mapper.mapAll(discipline.getTeachers(), TeacherDto.class), HttpStatus.OK);
        }
    }
}
