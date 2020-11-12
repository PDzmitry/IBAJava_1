package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.DescriptionOfPlan;
import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.entities.LessonPlan;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import by.protasovitski.calendariba.dto.DescriptionOfPlanDto;
import by.protasovitski.calendariba.dto.DisciplineDto;
import by.protasovitski.calendariba.dto.LessonPlanDto;
import by.protasovitski.calendariba.dto.TeacherDto;
import by.protasovitski.calendariba.service.DescriptionOfPlanService;
import by.protasovitski.calendariba.service.DisciplineService;
import by.protasovitski.calendariba.service.LessonPlanService;
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
@RequestMapping(ConstantURL.URL_DESCRIPTION_OF_PLAN)
@Api(tags = "description_of_plans", description = "Description_of_plans API")
public class DescriptionOfPlanController {

    @Autowired
    private DescriptionOfPlanService descriptionOfPlanService;

    @Autowired
    private LessonPlanService lessonPlanService;

    @Autowired
    private DisciplineService disciplineService;


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find descriptionOfPlan", notes = "Find the descriptionOfPlan by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DescriptionOfPlan found"),
            @ApiResponse(code = 404, message = "DescriptionOfPlan not found")
    })
    public ResponseEntity<DescriptionOfPlanDto> findOneDescriptionOfPlan(@PathVariable("id") Long id) {
        DescriptionOfPlan descriptionOfPlan = descriptionOfPlanService.findById(id);
        return new ResponseEntity<>(Mapper.map(descriptionOfPlan, DescriptionOfPlanDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List descriptionOfPlans", notes = "List all descriptionOfPlans")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DescriptionOfPlans found"),
            @ApiResponse(code = 404, message = "DescriptionOfPlans not found")
    })
    public ResponseEntity<List<DescriptionOfPlanDto>> findAllDescriptionOfPlans() {
        List<DescriptionOfPlan> descriptionOfPlans = descriptionOfPlanService.findAll();
        if (descriptionOfPlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(descriptionOfPlans, DescriptionOfPlanDto.class), HttpStatus.OK);
        }
    }

    @GetMapping("/page")
    @ApiOperation(value = "Sorted descriptionOfPlans page", notes = "Sorted descriptionOfPlans page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DescriptionOfPlans found"),
            @ApiResponse(code = 404, message = "DescriptionOfPlans not found")
    })
    public ResponseEntity<List<DescriptionOfPlanDto>> findAllDescriptionOfPlansPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<DescriptionOfPlan> descriptionOfPlans = descriptionOfPlanService.findAll(pageNo, pageSize, sortBy);
        if (descriptionOfPlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(descriptionOfPlans, DescriptionOfPlanDto.class), HttpStatus.OK);
        }
    }
    @GetMapping("/types")
    @ApiOperation(value = "List types_of_course", notes = "List all types_of_course")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_course found"),
            @ApiResponse(code = 404, message = "Types_of_course not found")
    })
    public ResponseEntity<List<TypeOfCourse>> findAllTypesOfCourse() {
        if (TypeOfCourse.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfCourse.orderedValues, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create descriptionOfPlan", notes = "It permits to create a new descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "DescriptionOfPlan created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DescriptionOfPlanDto> newDescriptionOfPlan(@RequestBody DescriptionOfPlanDto descriptionOfPlanDto) {
        return new ResponseEntity<>(
                Mapper.map(this.descriptionOfPlanService.save(Mapper.map(descriptionOfPlanDto, DescriptionOfPlan.class)), DescriptionOfPlanDto.class),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove descriptionOfPlan", notes = "It permits to remove a descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "DescriptionOfPlan removed successfully"),
            @ApiResponse(code = 404, message = "DescriptionOfPlan not found")
    })
    public ResponseEntity<Void> removeDescriptionOfPlan(@PathVariable("id") Long id) {
        if (descriptionOfPlanService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            descriptionOfPlanService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update descriptionOfPlan", notes = "It permits to update a descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DescriptionOfPlan update successfully"),
            @ApiResponse(code = 404, message = "DescriptionOfPlan not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<DescriptionOfPlanDto> updateDescriptionOfPlan(@PathVariable("id") Long id, @RequestBody DescriptionOfPlanDto descriptionOfPlanDto) {
        if (id != descriptionOfPlanDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (descriptionOfPlanService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            DescriptionOfPlan descriptionOfPlan = Mapper.map(descriptionOfPlanDto,DescriptionOfPlan.class);
              return new ResponseEntity<>(
                    Mapper.map(descriptionOfPlanService.save(descriptionOfPlan), DescriptionOfPlanDto.class),
                    HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}/lesson_plans")
    @ApiOperation(value = "List lesson_plans for descriptionOfPlan", notes = "List all lesson_plans for descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "DescriptionOfPlan and lesson_plans found"),
            @ApiResponse(code = 404, message = "DescriptionOfPlan and lesson_plans not found")
    })
    public ResponseEntity<List<LessonPlanDto>> findAllLessonPlan(@PathVariable("id") Long id) {
        DescriptionOfPlan descriptionOfPlan = descriptionOfPlanService.findById(id);
        if (descriptionOfPlan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<LessonPlan> lessonPlans = lessonPlanService.findAllByDescriptionOfPlan_Id(id);
            List<LessonPlanDto> lessonPlanDtoList = new ArrayList<>();
            for (LessonPlan lessonPlan:lessonPlans){
                LessonPlanDto lessonPlanDto = Mapper.map(lessonPlan, LessonPlanDto.class);
                lessonPlanDto.setDescriptionOfPlanDto(Mapper.map(lessonPlan.getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                lessonPlanDto.setDisciplineDto(Mapper.map(lessonPlan.getDiscipline(), DisciplineDto.class));
                lessonPlanDto.setTeacherDto(Mapper.map(lessonPlan.getTeacher(), TeacherDto.class));
                lessonPlanDtoList.add(lessonPlanDto);
            }
            return new ResponseEntity<>
                    (lessonPlanDtoList, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/{id}/lesson_plans")
    @ApiOperation(value = "Add  the lesson_plan to the descriptionOfPlan", notes = "It permits to add  the lesson_plan to the descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lesson_plan added"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<LessonPlanDto> addLesson_planToDescriptionOfPlan(@PathVariable("id") Long id, @RequestBody LessonPlanDto lessonPlanDto) {
        DescriptionOfPlan descriptionOfPlan = descriptionOfPlanService.findById(lessonPlanDto.getDescriptionOfPlanDto().getId());
        Discipline discipline = disciplineService.findById(lessonPlanDto.getDisciplineDto().getId());
        System.out.println(descriptionOfPlan.toString());
        System.out.println(discipline.toString());

        if (descriptionOfPlan == null || discipline == null || descriptionOfPlan.getId() != id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            LessonPlan newLessonPlan = Mapper.map(lessonPlanDto, LessonPlan.class);
            newLessonPlan.setDescriptionOfPlan(descriptionOfPlan);
            newLessonPlan.setDiscipline(discipline);
            LessonPlan lessonPlan = lessonPlanService.save(newLessonPlan);
            lessonPlanDto = Mapper.map(lessonPlan, LessonPlanDto.class);
            lessonPlanDto.setDescriptionOfPlanDto(Mapper.map(lessonPlan.getDescriptionOfPlan(),DescriptionOfPlanDto.class));
            lessonPlanDto.setDisciplineDto(Mapper.map(lessonPlan.getDiscipline(),DisciplineDto.class));
            return new ResponseEntity<>(lessonPlanDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}/lesson_plans/{code}")
    @ApiOperation(value = "Remove lesson_plan to the descriptionOfPlan", notes = "It permits to remove the lesson_plan to the descriptionOfPlan")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Lesson_plan removed successfully"),
            @ApiResponse(code = 404, message = "Lesson_plan not found")
    })
    public ResponseEntity<Void> removeLesson_plan(@PathVariable("id") Long id, @PathVariable("code") Long code) {
        DescriptionOfPlan descriptionOfPlan = descriptionOfPlanService.findById(id);
        LessonPlan lessonPlan = lessonPlanService.findById(code);

        if (descriptionOfPlan == null || lessonPlan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            lessonPlanService.delete(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
