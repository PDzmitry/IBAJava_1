package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.*;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.dto.*;
import by.protasovitski.calendariba.exceptions.DuplicationException;
import by.protasovitski.calendariba.service.*;
import by.protasovitski.calendariba.util.Mapper;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@CrossOrigin(origins = ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_TIMETABLE_OF_CLASSES)
@Api(tags = "timetable_of_classes", description = "Timetable_of_classes API")
public class TimetableOfClassesController {


    private TimetableOfClassesService timetableOfClassesService;
    private ClassroomService classroomService;
    private TeacherService teacherService;
    private DisciplineService disciplineService;
    private GroupService groupService;
    private UserService userService;

    @Autowired
    public TimetableOfClassesController(TimetableOfClassesService timetableOfClassesService,
                                        ClassroomService classroomService,
                                        TeacherService teacherService,
                                        DisciplineService disciplineService,
                                        GroupService groupService,
                                        UserService userService) {
        this.timetableOfClassesService = timetableOfClassesService;
        this.classroomService = classroomService;
        this.teacherService = teacherService;
        this.disciplineService = disciplineService;
        this.groupService = groupService;
        this.userService = userService;
    }





    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find timetable of classes", notes = "Find the Timetable of classes by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable_of_classes found"),
            @ApiResponse(code = 404, message = "Timetable_of_classes not found")
    })
    public ResponseEntity<TimetableOfClassesDto> findOneTimetableOfClasses(@PathVariable("id") Long id) {
        TimetableOfClasses timetable = timetableOfClassesService.findById(id);
        TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
        timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
        timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
        timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
        GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
        timetableOfClassesDto.setGroupDto(groupDto);
        return new ResponseEntity<>(timetableOfClassesDto, HttpStatus.OK);
    }

    @GetMapping("/types")
    @ApiOperation(value = "List types_of_work", notes = "List all types_of_work")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_work found"),
            @ApiResponse(code = 404, message = "Types_of_work not found")
    })
    public ResponseEntity<List<TypeOfWork>> findAllTypesOfWork() {
        if (TypeOfWork.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfWork.orderedValues, HttpStatus.OK);
        }
    }

    @GetMapping("/span")
    @ApiOperation(value = "List timetable of classes by date", notes = "List all timetable of classes by date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableOfClassesDto>> findAllSpan(
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        List<TimetableOfClasses> timetableOfClasses;
        if (!date1.isAfter(date2)) {
            timetableOfClasses = timetableOfClassesService.findAllByClassDateIsBetween(date1, date2);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (timetableOfClasses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableOfClassesDto> timetableOfClassesDtoList = new ArrayList<>();
            for (TimetableOfClasses timetable : timetableOfClasses) {
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableOfClassesDtoList.add(timetableOfClassesDto);
            }

            return new ResponseEntity<>(timetableOfClassesDtoList, HttpStatus.OK);
        }
    }
    @GetMapping("/span/owner")
    @ApiOperation(value = "List timetable of classes by date and owner", notes = "List all timetable of classes by date and owner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableOfClassesDto>> findAllSpanByOwner(
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        List<TimetableOfClasses> timetableOfClasses;
        if (!date1.isAfter(date2)) {
            timetableOfClasses = timetableOfClassesService.findAllByClassDateIsBetweenAndCreatedBy(date1, date2);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (timetableOfClasses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableOfClassesDto> timetableOfClassesDtoList = new ArrayList<>();
            for (TimetableOfClasses timetable : timetableOfClasses) {
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableOfClassesDtoList.add(timetableOfClassesDto);
            }

            return new ResponseEntity<>(timetableOfClassesDtoList, HttpStatus.OK);
        }
    }



    @GetMapping("/span/group/{id}")
    @ApiOperation(value = "List timetable of classes by date and group", notes = "List all timetable of classes by date and group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableOfClassesDto>> findAllSpanGroup(
            @PathVariable("id") Long id,
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        List<TimetableOfClasses> timetableOfClasses;
        Group group = groupService.findById(id);
        if (!date1.isAfter(date2)) {
            timetableOfClasses = timetableOfClassesService.findAllByClassDateIsBetweenAndGroup(date1, date2, group);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (timetableOfClasses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableOfClassesDto> timetableOfClassesDtoList = new ArrayList<>();
            for (TimetableOfClasses timetable : timetableOfClasses) {
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableOfClassesDtoList.add(timetableOfClassesDto);
            }

            return new ResponseEntity<>(timetableOfClassesDtoList, HttpStatus.OK);
        }
    }

    @GetMapping("/span/teacher/{id}")
    @ApiOperation(value = "List timetable of classes by date and teacher", notes = "List all timetable of classes by date and teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableOfClassesDto>> findAllSpanTeacher(
            @PathVariable("id") Long id,
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Teacher teacher = teacherService.findById(id);
        List<TimetableOfClasses> timetableOfClasses;
        if (!date1.isAfter(date2)) {
            timetableOfClasses = timetableOfClassesService.findAllByClassDateIsBetweenAndTeacher(date1, date2, teacher);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (timetableOfClasses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableOfClassesDto> timetableOfClassesDtoList = new ArrayList<>();
            for (TimetableOfClasses timetable : timetableOfClasses) {
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableOfClassesDtoList.add(timetableOfClassesDto);
            }

            return new ResponseEntity<>(timetableOfClassesDtoList, HttpStatus.OK);
        }
    }

    @GetMapping("/span/classroom/{id}")
    @ApiOperation(value = "List timetable of classes by date and classroom", notes = "List all timetable of classes by date and classroom")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes found"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableOfClassesDto>> findAllSpanClassroom(
            @PathVariable("id") Long id,
            @RequestParam String d1,
            @RequestParam String d2
    ) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Classroom classroom = classroomService.findById(id);
        List<TimetableOfClasses> timetableOfClasses;
        if (!date1.isAfter(date2)) {
            timetableOfClasses = timetableOfClassesService.findAllByClassDateIsBetweenAndClassroom(date1, date2, classroom);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (timetableOfClasses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableOfClassesDto> timetableOfClassesDtoList = new ArrayList<>();
            for (TimetableOfClasses timetable : timetableOfClasses) {
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableOfClassesDtoList.add(timetableOfClassesDto);
            }

            return new ResponseEntity<>(timetableOfClassesDtoList, HttpStatus.OK);
        }
    }


    @GetMapping("/need/{idGroup}")
    @ApiOperation(value = "List timetable of classes and number of needs", notes = "List timetable of classes and number of needs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " List Timetable of classes found"),
            @ApiResponse(code = 404, message = "List  Timetable of classes not found")
    })
    public ResponseEntity<List<TimetableNeedDto>> findNeed(@PathVariable("idGroup") Long id,
                                                           @RequestParam(value = "d", required = false) Long d) {
        Map<TimetableOfClasses, Integer> map;
        if (d == null) {
            map = timetableOfClassesService.createNeed(id);
        } else {
            map = timetableOfClassesService.createNeed(id, d);
        }

        if (map.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<TimetableNeedDto> timetableNeedDtoList = new ArrayList<>();
            for (Map.Entry<TimetableOfClasses, Integer> entry : map.entrySet()) {
                TimetableOfClasses timetable = entry.getKey();
                TimetableOfClassesDto timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
                timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
                timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
                GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                timetableOfClassesDto.setGroupDto(groupDto);
                timetableNeedDtoList.add(new TimetableNeedDto(timetableOfClassesDto, entry.getValue()));
            }

            return new ResponseEntity<>(timetableNeedDtoList, HttpStatus.OK);
        }
    }

    @GetMapping("/free")
    @ApiOperation(value = "List free classrooms by date and time and type of work", notes = "List free classrooms by date and time and type of work")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Classrooms found"),
            @ApiResponse(code = 404, message = "Classrooms not found")
    })
    public ResponseEntity<List<ClassroomDto>> findAllFreeClassrooms(
            @RequestParam String d,
            @RequestParam String t1,
            @RequestParam String t2
    ) {
        LocalDate date = LocalDate.parse(d);
        LocalTime time1 = LocalTime.parse(t1);
        LocalTime time2 = LocalTime.parse(t2);
        if (time1.isBefore(time2)) {
            List<Classroom> classrooms = timetableOfClassesService.getFreeClassrooms(date, time1, time2);
            if (classrooms.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(Mapper.mapAll(classrooms, ClassroomDto.class), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping
    @ApiOperation(value = "Create timetable of classes", notes = "It permits to create a new timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Classroom created successfully"),
            @ApiResponse(code = 403, message = "Forbidden to create"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<TimetableOfClassesDto> newTimetable(@RequestBody TimetableOfClassesDto timetableOfClassesDto) {
        TimetableOfClasses newTimetable = Mapper.map(timetableOfClassesDto, TimetableOfClasses.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        long count = user.getRoles().stream().filter(r-> r.getName()== RoleName.ROLE_ADMIN).count();
        if (count==0 && newTimetable.getClassDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (timetableOfClassesDto.getClassroomDto() != null) {
            newTimetable.setClassroom(classroomService.findById(timetableOfClassesDto.getClassroomDto().getId()));
        }
        if (timetableOfClassesDto.getDisciplineDto() != null) {
            newTimetable.setDiscipline(disciplineService.findById(timetableOfClassesDto.getDisciplineDto().getId()));
        }
        if (timetableOfClassesDto.getTeacherDto() != null) {
            newTimetable.setTeacher(teacherService.findById(timetableOfClassesDto.getTeacherDto().getId()));
        }
        if (timetableOfClassesDto.getGroupDto() != null) {
            newTimetable.setGroup(groupService.findById(timetableOfClassesDto.getGroupDto().getId()));
        }
        TimetableOfClasses timetable = timetableOfClassesService.save(newTimetable);

        timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
        timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
        timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
        timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
        GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
        timetableOfClassesDto.setGroupDto(groupDto);

        return new ResponseEntity<>(timetableOfClassesDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove timetable of classes", notes = "It permits to remove a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes removed successfully"),
            @ApiResponse(code = 404, message = "Timetable of classes not found")
    })
    public ResponseEntity<Void> removeTimetable(@PathVariable("id") Long id) {
        if (timetableOfClassesService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            timetableOfClassesService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update timetable of classes", notes = "It permits to update a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable of classes update successfully"),
            @ApiResponse(code = 404, message = "Timetable of classes not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<TimetableOfClassesDto> updateTimetable(@PathVariable("id") Long id, @RequestBody TimetableOfClassesDto timetableOfClassesDto) {
        if (!id.equals(timetableOfClassesDto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (timetableOfClassesService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            TimetableOfClasses newTimetable = Mapper.map(timetableOfClassesDto, TimetableOfClasses.class);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            long count = user.getRoles().stream().filter(r-> r.getName()== RoleName.ROLE_ADMIN).count();
            if (count==0 && newTimetable.getClassDate().isBefore(LocalDate.now())){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (timetableOfClassesDto.getClassroomDto() != null) {
                newTimetable.setClassroom(classroomService.findById(timetableOfClassesDto.getClassroomDto().getId()));
            }
            if (timetableOfClassesDto.getDisciplineDto() != null) {
                newTimetable.setDiscipline(disciplineService.findById(timetableOfClassesDto.getDisciplineDto().getId()));
            }
            if (timetableOfClassesDto.getTeacherDto() != null) {
                newTimetable.setTeacher(teacherService.findById(timetableOfClassesDto.getTeacherDto().getId()));
            }
            if (timetableOfClassesDto.getGroupDto() != null) {
                newTimetable.setGroup(groupService.findById(timetableOfClassesDto.getGroupDto().getId()));
            }
            TimetableOfClasses timetable = timetableOfClassesService.save(newTimetable);
            timetableOfClassesDto = Mapper.map(timetable, TimetableOfClassesDto.class);
            timetableOfClassesDto.setClassroomDto(Mapper.map(timetable.getClassroom(), ClassroomDto.class));
            timetableOfClassesDto.setDisciplineDto(Mapper.map(timetable.getDiscipline(), DisciplineDto.class));
            timetableOfClassesDto.setTeacherDto(Mapper.map(timetable.getTeacher(), TeacherDto.class));
            GroupDto groupDto = Mapper.map(timetable.getGroup(), GroupDto.class);
            groupDto.setDescriptionOfPlanDto(Mapper.map(timetable.getGroup().getDescriptionOfPlan(), DescriptionOfPlanDto.class));
            timetableOfClassesDto.setGroupDto(groupDto);
            return new ResponseEntity<>(timetableOfClassesDto, HttpStatus.OK);
        }
    }

    @PutMapping("/confirms")
    @ApiOperation(value = "Confirm timetable of classes", notes = "It permits to confirm a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes confirmed successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> confirmTimetable(@RequestBody Long [] id) {
        timetableOfClassesService.confirm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/confirm/span")
    @ApiOperation(value = "Confirm timetable of classes", notes = "It permits to confirm a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes confirmed successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> confirmTimetableSpan(@RequestParam String d1,
                                                 @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.confirm(date1, date2);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/confirm/group/{id}")
    @ApiOperation(value = "Confirm timetable of classes by group", notes = "It permits to confirm a timetable of classes by group")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes confirmed successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> confirmTimetableByGroup(@PathVariable("id") Long id,
                                                        @RequestParam String d1,
                                                        @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Group group = groupService.findById(id);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.confirm(date1, date2, group);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/confirm/teacher/{id}")
    @ApiOperation(value = "Confirm timetable of classes by teacher", notes = "It permits to confirm a timetable of classes by teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes confirmed successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> confirmTimetableByTeacher(@PathVariable("id") Long id,
                                                          @RequestParam String d1,
                                                          @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Teacher teacher = teacherService.findById(id);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.confirm(date1, date2, teacher);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cancel")
    @ApiOperation(value = "Cancel confirmation timetable of classes", notes = "It permits to confirm a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes canceled successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> cancelTimetable(@RequestBody Long [] id) {
        timetableOfClassesService.cancelConfirmation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/cancel/span")
    @ApiOperation(value = "Cancel confirmation timetable of classes", notes = "It permits to confirm a timetable of classes")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes canceled successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> cancelTimetableSpan(@RequestParam String d1,
                                                @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.cancelConfirmation(date1, date2);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cancel/group/{id}")
    @ApiOperation(value = "Cancel confirmation timetable of classes by group", notes = "It permits to cancel confirmation a timetable of classes by group")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes canceled successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> cancelTimetableByGroup(@PathVariable("id") Long id,
                                                       @RequestParam String d1,
                                                       @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Group group = groupService.findById(id);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.cancelConfirmation(date1, date2, group);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cancel/teacher/{id}")
    @ApiOperation(value = "Cancel confirmation timetable of classes by teacher", notes = "It permits to cancel confirmation a timetable of classes by teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Timetable of classes canceled successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> cancelTimetableByTeacher(@PathVariable("id") Long id,
                                                         @RequestParam String d1,
                                                         @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Teacher teacher = teacherService.findById(id);
        if (!date1.isAfter(date2)) {
            timetableOfClassesService.cancelConfirmation(date1, date2, teacher);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
