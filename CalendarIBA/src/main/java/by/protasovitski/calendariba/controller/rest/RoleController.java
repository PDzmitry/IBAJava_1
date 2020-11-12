package by.protasovitski.calendariba.controller.rest;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfAddWork;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.dto.DescriptionOfPlanDto;
import by.protasovitski.calendariba.dto.GroupDto;
import by.protasovitski.calendariba.dto.RoleDto;
import by.protasovitski.calendariba.service.RoleService;
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
@RequestMapping(ConstantURL.URL_ROLE)
@Api(tags = "roles", description = "Roles API")
public class RoleController {
    @GetMapping
    @ApiOperation(value = "List roles", notes = "List all roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Types_of_add_work found"),
            @ApiResponse(code = 404, message = "Types_of_add_work not found")
    })
    public ResponseEntity<List<RoleName>> findAllRoles() {
        if (RoleName.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(RoleName.orderedValues, HttpStatus.OK);
        }
    }

}
