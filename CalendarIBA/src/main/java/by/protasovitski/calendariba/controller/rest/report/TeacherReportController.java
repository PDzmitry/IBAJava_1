package by.protasovitski.calendariba.controller.rest.report;


import by.protasovitski.calendariba.controller.rest.ConstantURL;
import by.protasovitski.calendariba.domain.entities.Employee;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import by.protasovitski.calendariba.exceptions.ResourceNotFoundException;
import by.protasovitski.calendariba.report.ReportService;
import by.protasovitski.calendariba.report.TeacherReportService;
import by.protasovitski.calendariba.service.EmployeeService;
import by.protasovitski.calendariba.service.GroupService;
import by.protasovitski.calendariba.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_TEACHER_REPORT)
@Api(tags = "report teachers", description = "Teacher ReportFormat API")
public class TeacherReportController {


    private ReportService reportService;
    private TeacherReportService teacherReportService;
    private TeacherService teacherService;
    private GroupService groupService;
    private EmployeeService employeeService;

    @Autowired
    public TeacherReportController(ReportService reportService,
                                   TeacherReportService teacherReportService,
                                   TeacherService teacherService,
                                   GroupService groupService,
                                   EmployeeService employeeService) {
        this.reportService = reportService;
        this.teacherReportService = teacherReportService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/{format}/{username}")
    @ApiOperation(value = "List teachers", notes = "List all teachers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teachers found"),
            @ApiResponse(code = 404, message = "Teachers not found")
    })
    public ResponseEntity<byte[]> reportTeachers(@PathVariable(required = false) String username,
                                         @PathVariable(required = false) String format) {
        JRDataSource dataSource = teacherReportService.create();
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("DS1", dataSource);
        byte[] bytes = reportService.generateReport("allteacher", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                //                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }

    @GetMapping(value = "/form02015/{format}/{username}")
    @ApiOperation(value = "List teachers", notes = "List all teachers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teachers found"),
            @ApiResponse(code = 404, message = "Teachers not found")
    })
    public ResponseEntity<byte[]> reportForm02015All(@PathVariable(required = false) String username,
                                                  @PathVariable(required = false) String format,
                                                  @RequestParam String d1,
                                                  @RequestParam String d2) throws ResourceNotFoundException {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        JRDataSource dataSource;
        if (!date1.isAfter(date2)) {
            dataSource = teacherReportService.create(date1, date2);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("d1", fullDateString(date1));
        params.put("d2", fullDateString(date2));
        params.put("DS1", dataSource);
        Employee employee = employeeService.findByTypeOfPosition(TypeOfPosition.RECTOR);
        params.put("rector",employee.getFirstName().substring(0,1)+
                "."+employee.getPatronymic().substring(0,1)
                +"."+ employee.getLastName());
        byte[] bytes = reportService.generateReport("form_2_015", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                //                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }

    @GetMapping(value = "/{id}/form02015/{format}/{username}")
    @ApiOperation(value = "Teacher", notes = "Teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher found"),
            @ApiResponse(code = 404, message = "Teacher not found")
    })
    public ResponseEntity<byte[]> reportForm02015Teacher(@PathVariable(required = false) Long id,
                                                  @PathVariable(required = false) String username,
                                                  @PathVariable(required = false) String format,
                                                  @RequestParam String d1,
                                                  @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Teacher teacher = teacherService.findById(Long.valueOf(id));
        JRDataSource dataSource;
        if (!date1.isAfter(date2)) {
            dataSource = teacherReportService.create(date1, date2, teacher);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("d1", fullDateString(date1));
        params.put("d2", fullDateString(date2));
        params.put("DS1", dataSource);
        Employee employee = employeeService.findByTypeOfPosition(TypeOfPosition.RECTOR);
        params.put("rector",employee.getFirstName().substring(0,1)+
                "."+employee.getPatronymic().substring(0,1)
                +"."+ employee.getLastName());
        byte[] bytes = reportService.generateReport("form_2_015", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                //                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }

    @GetMapping(value = "/group/{id}/form02015/{format}/{username}")
    @ApiOperation(value = "Teacher", notes = "Teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher found"),
            @ApiResponse(code = 404, message = "Teacher not found")
    })
    public ResponseEntity<byte[]> reportForm02015Group(@PathVariable(required = false) Long id,
                                                       @PathVariable(required = false) String username,
                                                       @PathVariable(required = false) String format,
                                                       @RequestParam String d1,
                                                       @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Group group = groupService.findById(id);
        JRDataSource dataSource;
        if (!date1.isAfter(date2)) {
            dataSource = teacherReportService.create(date1, date2, group);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("d1", fullDateString(date1));
        params.put("d2", fullDateString(date2));
        params.put("DS1", dataSource);
        Employee employee = employeeService.findByTypeOfPosition(TypeOfPosition.RECTOR);
        params.put("rector",employee.getFirstName().substring(0,1)+
                "."+employee.getPatronymic().substring(0,1)
                +"."+ employee.getLastName());
        params.put("groupname",group.getGroupName());
        params.put("plan",group.getDescriptionOfPlan().getDescription());
        byte[] bytes = reportService.generateReport("form_2_015_group", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                //                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }

    @GetMapping(value = "/group/{id}/consolidated/{format}/{username}")
    @ApiOperation(value = "Teacher", notes = "Teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Teacher found"),
            @ApiResponse(code = 404, message = "Teacher not found")
    })
    public ResponseEntity<byte[]> consolidatedByGroup(@PathVariable(required = false) Long id,
                                                  @PathVariable(required = false) String username,
                                                  @PathVariable(required = false) String format,
                                                  @RequestParam String d1,
                                                  @RequestParam String d2) {
        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Group group = groupService.findById(id);
        JRDataSource dataSource1,dataSource2;
        if (!date1.isAfter(date2)) {
            dataSource1 = teacherReportService.create(date1, date2, group);
            dataSource2 =teacherReportService.createConsolidatedByGroup(date1, date2, group);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("d1", fullDateString(date1));
        params.put("d2", fullDateString(date2));
        params.put("DS1", dataSource1);
        params.put("DS2", dataSource2);
        Employee employee = employeeService.findByTypeOfPosition(TypeOfPosition.RECTOR);
        params.put("rector",employee.getFirstName().substring(0,1)+
                "."+employee.getPatronymic().substring(0,1)
                +"."+ employee.getLastName());
        params.put("groupname",group.getGroupName());
        params.put("plan",group.getDescriptionOfPlan().getDescription());
        byte[] bytes = reportService.generateReport("consolidatedbygroup", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                //                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }





    private String fullDateString(LocalDate d) {
        return d.getDayOfMonth() + " " + d.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru", "RU")) + " " + d.getYear();

    }


}
