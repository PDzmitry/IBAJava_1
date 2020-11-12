package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.AddWorkOfTeacher;
import by.protasovitski.calendariba.domain.entities.Department;
import by.protasovitski.calendariba.domain.entities.TimetableOfClasses;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfAddWork;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.dto.*;
import by.protasovitski.calendariba.service.AddWorkOfTeacherService;
import by.protasovitski.calendariba.service.DisciplineService;
import by.protasovitski.calendariba.service.GroupService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_ADDWORK)
@Api(tags = "addition works of teacher", description = "Addition works of teacher API")
public class AddWorkOfTeacherController {

    private AddWorkOfTeacherService addWorkOfTeacherService;
    private TeacherService teacherService;
    private GroupService groupService;

    @Autowired
    public AddWorkOfTeacherController(AddWorkOfTeacherService addWorkOfTeacherService, TeacherService teacherService, GroupService groupService) {
        this.addWorkOfTeacherService = addWorkOfTeacherService;
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find addition work of teacher", notes = "Find addition work of teacher by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Addition work found"),
            @ApiResponse(code = 404, message = "Addition work not found")
    })
    public ResponseEntity<AddWorkOfTeacherDto> findOneWork(@PathVariable("id") Long id) {
        AddWorkOfTeacher addwork = addWorkOfTeacherService.findById(id);
        AddWorkOfTeacherDto addWorkOfTeacherDto = Mapper.map(addwork, AddWorkOfTeacherDto.class);
        addWorkOfTeacherDto.setTeacherDto(Mapper.map(addwork.getTeacher(), TeacherDto.class));
        GroupDto groupDto = Mapper.map(addwork.getGroup(), GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(addwork.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
        addWorkOfTeacherDto.setGroupDto(groupDto);
        return new ResponseEntity<>(addWorkOfTeacherDto, HttpStatus.OK);
    }

    @GetMapping("/types")
    @ApiOperation(value = "List types_of_add_work", notes = "List all types_of_add_ work")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_add_work found"),
            @ApiResponse(code = 404, message = "Types_of_add_work not found")
    })
    public ResponseEntity<List<TypeOfAddWork>> findAllTypesOfAddWork() {
        if (TypeOfWork.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfAddWork.orderedValues, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create addition work of teacher", notes = "It permits to create a new addition work")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Addition work created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<AddWorkOfTeacherDto> newWork(@RequestBody AddWorkOfTeacherDto addWorkOfTeacherDto) {

        AddWorkOfTeacher newAddWork = Mapper.map(addWorkOfTeacherDto, AddWorkOfTeacher.class);
        if (addWorkOfTeacherDto.getTeacherDto() != null) {
            newAddWork.setTeacher(teacherService.findById(addWorkOfTeacherDto.getTeacherDto().getId()));
        }
        if (addWorkOfTeacherDto.getGroupDto() != null) {
            newAddWork.setGroup(groupService.findById(addWorkOfTeacherDto.getGroupDto().getId()));
        }
        AddWorkOfTeacher addwork = addWorkOfTeacherService.save(newAddWork);

        addWorkOfTeacherDto = Mapper.map(addwork, AddWorkOfTeacherDto.class);
        addWorkOfTeacherDto.setTeacherDto(Mapper.map(addwork.getTeacher(), TeacherDto.class));
        GroupDto groupDto = Mapper.map(addwork.getGroup(), GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(addwork.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
        addWorkOfTeacherDto.setGroupDto(groupDto);
        return new ResponseEntity<>(addWorkOfTeacherDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove addition work of teacher", notes = "It permits to remove a addition work")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Addition work removed successfully"),
            @ApiResponse(code = 404, message = "Addition work not found")
    })
    public ResponseEntity<Void> removeWork(@PathVariable("id") Long id) {
        if (addWorkOfTeacherService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            addWorkOfTeacherService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update addition work", notes = "It permits to update a addition work")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Addition work update successfully"),
            @ApiResponse(code = 404, message = "Addition work not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<AddWorkOfTeacherDto> updateAddWork(@PathVariable("id") Long id, @RequestBody AddWorkOfTeacherDto addWorkOfTeacherDto) {
        if (id != addWorkOfTeacherDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (addWorkOfTeacherService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            AddWorkOfTeacher newAddWork = Mapper.map(addWorkOfTeacherDto, AddWorkOfTeacher.class);
            if (addWorkOfTeacherDto.getTeacherDto() != null) {
                newAddWork.setTeacher(teacherService.findById(addWorkOfTeacherDto.getTeacherDto().getId()));
            }
            if (addWorkOfTeacherDto.getGroupDto() != null) {
                newAddWork.setGroup(groupService.findById(addWorkOfTeacherDto.getGroupDto().getId()));
            }
            AddWorkOfTeacher addwork = addWorkOfTeacherService.save(newAddWork);

            addWorkOfTeacherDto = Mapper.map(addwork, AddWorkOfTeacherDto.class);
            addWorkOfTeacherDto.setTeacherDto(Mapper.map(addwork.getTeacher(), TeacherDto.class));
            GroupDto groupDto = Mapper.map(addwork.getGroup(), GroupDto.class);
            groupDto.setDescriptionOfPlanDto(Mapper.map(addwork.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
            addWorkOfTeacherDto.setGroupDto(groupDto);
            return new ResponseEntity<>(addWorkOfTeacherDto, HttpStatus.OK);
        }
    }

    @GetMapping("/span")
    @ApiOperation(value = "List addition works of teacher by date", notes = "List addition works of teacher by date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<AddWorkOfTeacherDto>> findAllAddWork(
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        List<AddWorkOfTeacher> addWorks;
        if (!date1.isAfter(date2)) {
            addWorks = addWorkOfTeacherService.findAllByDateIsBetween(date1, date2);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (addWorks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<AddWorkOfTeacherDto> addWorkOfTeacherDtoList = new ArrayList<>();
            for (AddWorkOfTeacher addwork : addWorks) {
                AddWorkOfTeacherDto addWorkOfTeacherDto = Mapper.map(addwork, AddWorkOfTeacherDto.class);
                addWorkOfTeacherDto.setTeacherDto(Mapper.map(addwork.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(addwork.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(addwork.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                addWorkOfTeacherDto.setGroupDto(groupDto);
                addWorkOfTeacherDtoList.add(addWorkOfTeacherDto);
            }
            return new ResponseEntity<>(addWorkOfTeacherDtoList, HttpStatus.OK);
        }
    }
}
