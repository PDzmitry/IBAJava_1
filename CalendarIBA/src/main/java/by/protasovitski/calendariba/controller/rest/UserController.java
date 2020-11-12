package by.protasovitski.calendariba.controller.rest;


import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.User;
import by.protasovitski.calendariba.domain.entities.enums.RoleName;
import by.protasovitski.calendariba.domain.entities.enums.Status;
import by.protasovitski.calendariba.dto.DisciplineDto;
import by.protasovitski.calendariba.dto.NewUserDto;
import by.protasovitski.calendariba.dto.RoleDto;
import by.protasovitski.calendariba.dto.UserDto;
import by.protasovitski.calendariba.service.RoleService;
import by.protasovitski.calendariba.service.UserService;
import by.protasovitski.calendariba.util.Mapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@RequestMapping(ConstantURL.URL_USER)
@Api(tags = "users", description = "User API")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }



    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Find user", notes = "Find the User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id){
        User user = userService.findById(id);
        return new ResponseEntity<>(Mapper.map(user,UserDto.class), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "List users", notes = "List all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users found"),
            @ApiResponse(code = 404, message = "Users not found")
    })
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(users, UserDto.class), HttpStatus.OK);
        }
    }
    @GetMapping("/page" )
    @ApiOperation(value = "Sorted users page", notes = "Sorted users page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users found"),
            @ApiResponse(code = 404, message = "Users not found")
    })
    public ResponseEntity<List<UserDto>> findAllUsersPage(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<User> users = userService.findAll(pageNo,pageSize,sortBy);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Mapper.mapAll(users, UserDto.class), HttpStatus.OK);
        }
    }

    @GetMapping("/status")
    @ApiOperation(value = "List status", notes = "List all status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status found"),
            @ApiResponse(code = 404, message = "Status not found")
    })
    public ResponseEntity<List<Status>> findAllStatus() {
        if (Status.orderedValues.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(Status.orderedValues, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "Create user", notes = "It permits to create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<UserDto> newUser(@RequestBody NewUserDto newUserDto) {

        User user = Mapper.map(newUserDto, User.class);

        System.out.println(newUserDto.getPassword()+"!!!!!!!!!!!!!!!!");
        return new ResponseEntity<>(
                Mapper.map(this.userService.save(Mapper.map(newUserDto, User.class)), UserDto.class),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Remove user", notes = "It permits to remove a user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User removed successfully"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<Void> removeUser(@PathVariable("id") Long id) {
        if (userService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user", notes = "It permits to update a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User update successfully"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody NewUserDto newUserDto) {
        User oldUser;
        if (!(id.equals(newUserDto.getId()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if ((oldUser=userService.findById(id)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            User user = Mapper.map(newUserDto, User.class);
            user.setRoles(oldUser.getRoles());
            return new ResponseEntity<>(
                    Mapper.map(userService.save(user), UserDto.class),
                    HttpStatus.OK);
        }
    }
    @GetMapping(value = "/{id}/roles")
    @ApiOperation(value = "List roles of user", notes = "List all roles of user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User and roles found"),
            @ApiResponse(code = 404, message = "User or roles of user not found")
    })
    public ResponseEntity<List<RoleDto>> findAllRolesOfUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user==null || user.getRoles().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>
                    (Mapper.mapAll(user.getRoles(), RoleDto.class),HttpStatus.OK);
        }
    }

    @PutMapping(value = "/{id}/roles")
    @ApiOperation(value = "Add  the role to the user", notes = "It permits to add  the role to the user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Role added"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<Void> addRoleToUser(@PathVariable("id") Long id,
                                                       @RequestParam String rolename) {
        Role role = roleService.findByRolename(rolename.toUpperCase());
        userService.addUserRole(id,role.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/roles")
    @ApiOperation(value = "Remove role from the user", notes = "It permits to remove the role from the user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Role removed successfully"),
            @ApiResponse(code = 404, message = "Role not found")
    })
    public ResponseEntity<Void> removeRoleOfUser(@PathVariable("id") Long id,
                                                 @RequestParam String rolename) {

        Role role = roleService.findByRolename(rolename.toUpperCase());
        userService.delUserRole(id,role.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
