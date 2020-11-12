package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.DescriptionOfPlan;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.NumberOfPersons;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEducation;
import by.protasovitski.calendariba.dto.DescriptionOfPlanDto;
import by.protasovitski.calendariba.dto.GroupDto;
import by.protasovitski.calendariba.dto.NumberOfPersonsDto;
import by.protasovitski.calendariba.service.GroupService;
import by.protasovitski.calendariba.util.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_GROUP)
@Api(tags = "groups", description = "Groups API")
public class GroupController {
    @Autowired
    private GroupService groupService;


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find group", notes = "Find the group by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Group found"),
            @ApiResponse(code = 404, message = "Group not found")
    })
    public ResponseEntity<GroupDto> findOneGroup(@PathVariable("id") Long id) {
        Group group = groupService.findById(id);
        GroupDto groupDto = Mapper.map(group, GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(),DescriptionOfPlanDto.class));
        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List groups", notes = "List all groups")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Groups found"),
            @ApiResponse(code = 404, message = "Groups not found")
    })
//    @Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<List<GroupDto>> findAllGroups() {
        List<Group> groups = groupService.findAll();
        if (groups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
          /*  List<GroupDto> groupDtoList = new ArrayList<>();
            for (Group group:groups){
                GroupDto groupDto = Mapper.map(group, GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                groupDtoList.add(groupDto);
            }*/

            List<GroupDto> groupDtoList = groups.stream().map(group -> {
                GroupDto groupDto = (Mapper.map(group,GroupDto.class));
                groupDto.setDescriptionOfPlanDto(
                                Mapper.map(group.getDescriptionOfPlan(),DescriptionOfPlanDto.class)
                        );
                return groupDto;
            }).collect(Collectors.toList());
            return new ResponseEntity<>(groupDtoList, HttpStatus.OK);
        }
    }

    @GetMapping("/page" )
    @ApiOperation(value = "Sorted groups page", notes = "Sorted groups page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Group found"),
            @ApiResponse(code = 404, message = "Group not found")
    })
    public ResponseEntity<List<GroupDto>> findAllGroupsPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<Group> groups = groupService.findAll(pageNo,pageSize,sortBy);
        if (groups.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<GroupDto> groupDtoList = new ArrayList<>();
            for (Group group:groups){
                GroupDto groupDto = Mapper.map(group, GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(), DescriptionOfPlanDto.class));
                groupDtoList.add(groupDto);
            }
            return new ResponseEntity<>(groupDtoList, HttpStatus.OK);
        }
    }
    @GetMapping("/types")
    @ApiOperation(value = "List types_of_education", notes = "List all types_of_education")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_education found"),
            @ApiResponse(code = 404, message = "Types_of_education not found")
    })
    public ResponseEntity<List<TypeOfEducation>> findAllTypeOfEducation() {
        if (TypeOfEducation.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(TypeOfEducation.orderedValues, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create group", notes = "It permits to create a new group")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Group created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<GroupDto> newGroup(@RequestBody GroupDto groupDto) {
        Group newGroup = Mapper.map(groupDto, Group.class);
        DescriptionOfPlan descriptionOfPlan = Mapper.map(groupDto.getDescriptionOfPlanDto(),DescriptionOfPlan.class);
        newGroup.setDescriptionOfPlan(descriptionOfPlan);
        Group group = groupService.save(newGroup);
        groupDto = Mapper.map( group, GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(),DescriptionOfPlanDto.class));
        return new ResponseEntity<>(groupDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove group", notes = "It permits to remove a group")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Group removed successfully"),
            @ApiResponse(code = 404, message = "Group not found")
    })
    public ResponseEntity<Void> removeGroup(@PathVariable("id") Long id) {
        if (groupService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            groupService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update group", notes = "It permits to update a group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Group update successfully"),
            @ApiResponse(code = 404, message = "Group not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<GroupDto> updateGroup(@PathVariable("id") Long id, @RequestBody GroupDto groupDto) {
        if (id != groupDto.getId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (groupService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Group newGroup = Mapper.map(groupDto, Group.class);
            DescriptionOfPlan descriptionOfPlan = Mapper.map(groupDto.getDescriptionOfPlanDto(),DescriptionOfPlan.class);
            newGroup.setDescriptionOfPlan(descriptionOfPlan);
            Group group = groupService.save(newGroup);
            groupDto = Mapper.map( group, GroupDto.class);
            groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(),DescriptionOfPlanDto.class));
            return new ResponseEntity<>(groupDto,HttpStatus.OK);
        }
    }

    @GetMapping("/quantity/group/{id}")
    @ApiOperation(value = "List number of people in the group", notes = "List number of people in the group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Number of people found"),
            @ApiResponse(code = 404, message = "Number of people not found")
    })
    public ResponseEntity<List<NumberOfPersonsDto>> findAllQuantity(@PathVariable("id") Long id) {
        Group group = groupService.findById(id);
        List<NumberOfPersons> numberOfPersons = groupService.findByGroup(group);
        if (numberOfPersons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<NumberOfPersonsDto> numberOfPersonsDtos = numberOfPersons.stream().map(number -> {
                NumberOfPersonsDto numberOfPersonsDto = (Mapper.map(number,NumberOfPersonsDto.class));
                GroupDto groupDto = Mapper.map(number.getGroup(),GroupDto.class);
                groupDto.setDescriptionOfPlanDto(Mapper.map(number.getGroup().getDescriptionOfPlan(),DescriptionOfPlanDto.class));
                numberOfPersonsDto.setGroupDto(groupDto);
                return numberOfPersonsDto;
            }).collect(Collectors.toList());
            return new ResponseEntity<>(numberOfPersonsDtos, HttpStatus.OK);
        }
    }

    @PostMapping("/quantity")
    @ApiOperation(value = "Create number of people in the group", notes = "It permits to create a new number of people in the group")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "number of people in the group created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<NumberOfPersonsDto> newQuantity(@RequestBody NumberOfPersonsDto numberOfPersonsDto) {
        NumberOfPersons newNumberOfPersons= Mapper.map(numberOfPersonsDto, NumberOfPersons.class);
        Group group = groupService.findById(numberOfPersonsDto.getGroupDto().getId());
        newNumberOfPersons.setGroup(group);
        NumberOfPersons numberOfPersons = groupService.saveNumberOfPersons(newNumberOfPersons);
        numberOfPersonsDto = Mapper.map(numberOfPersons,NumberOfPersonsDto.class);
        GroupDto groupDto = Mapper.map(group,GroupDto.class);
        groupDto.setDescriptionOfPlanDto(Mapper.map(group.getDescriptionOfPlan(),DescriptionOfPlanDto.class));
        numberOfPersonsDto.setGroupDto(groupDto);
        return new ResponseEntity<>(numberOfPersonsDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/quantity/{id}")
    @ApiOperation(value = "Remove number of people in the group", notes = "It permits to remove number of people in the group")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Number of people in the group removed successfully"),
            @ApiResponse(code = 404, message = "Number of people in the group not found")
    })
    public ResponseEntity<Void> removeQuantity(@PathVariable("id") Long id) {
        if (groupService.findNumberOfPersonsById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            groupService.deleteNumberOfPersons(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
