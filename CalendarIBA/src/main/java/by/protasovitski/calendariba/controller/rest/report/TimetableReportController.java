package by.protasovitski.calendariba.controller.rest.report;

import by.protasovitski.calendariba.controller.rest.ConstantURL;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.report.ReportService;
import by.protasovitski.calendariba.report.TimetableReportService;
import by.protasovitski.calendariba.service.GroupService;
import by.protasovitski.calendariba.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
@RequestMapping(ConstantURL.URL_TIMETABLE_REPORT)
@Api(tags = "report Timetable", description = "Timetable ReportFormat API")
public class TimetableReportController {

    private ReportService reportService;
    private TimetableReportService timetableReportService;
    private GroupService groupService;
    private TeacherService teacherService;

    @Autowired
    public TimetableReportController(ReportService reportService,
                                     TimetableReportService timetableReportService,
                                     GroupService groupService,
                                     TeacherService teacherService) {
        this.reportService = reportService;
        this.timetableReportService = timetableReportService;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/{format}/{username}")
    @ApiOperation(value = "List timetable", notes = "List timetable")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable found"),
            @ApiResponse(code = 404, message = "Timetable  not found")
    })
    public ResponseEntity<byte[]> report(@PathVariable(required = false) String username,
                                         @PathVariable(required = false) String format,
                                         @RequestParam String d1,
                                         @RequestParam String d2) {

        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        JRDataSource dataSource;
        if (!date1.isAfter(date2)) {
            dataSource = timetableReportService.create(date1, date2);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("d1",fullDateString(date1));
        params.put("d2",fullDateString(date2));
        params.put("DS1",dataSource);


        byte[] bytes = reportService.generateReport("alltimetable", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
//                .header("Content-Type", "application/vnd.ms-excel" + "; charset=UTF-8")

                .header("Content-Disposition", "inline; filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                .body(bytes);
    }

    @GetMapping(value = "/group/{id}/{format}/{username}")
    @ApiOperation(value = "List timetable", notes = "List timetable")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable found"),
            @ApiResponse(code = 404, message = "Timetable  not found")
    })
    public ResponseEntity<byte[]> reportGroup(@PathVariable String username,
                                         @PathVariable String format,
                                         @PathVariable String id,
                                         @RequestParam String d1,
                                         @RequestParam String d2) {

        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Group group = groupService.findById(Long.valueOf(id));
        JRDataSource dataSource;
        if (!date1.isAfter(date2) && group!=null) {
            dataSource = timetableReportService.create(date1, date2,group);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("group",group.getGroupName());
        params.put("d1",fullDateString(date1));
        params.put("d2",fullDateString(date2));
        params.put("DS1",dataSource);
        byte[] bytes = reportService.generateReport("grouptimetable", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
//                .header("Content-Type", "application/vnd.ms-excel" + "; charset=UTF-8")

                .header("Content-Disposition", "inline; filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                .body(bytes);
    }
    @GetMapping(value = "/teacher/{id}/{format}/{username}")
    @ApiOperation(value = "List timetable", notes = "List timetable")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timetable found"),
            @ApiResponse(code = 404, message = "Timetable  not found")
    })
    public ResponseEntity<byte[]> reportTeacher(@PathVariable String username,
                                         @PathVariable String format,
                                         @PathVariable Long id,
                                         @RequestParam String d1,
                                         @RequestParam String d2) {

        LocalDate date1 = LocalDate.parse(d1);
        LocalDate date2 = LocalDate.parse(d2);
        Teacher teacher = teacherService.findById(Long.valueOf(id));
        JRDataSource dataSource;
        if (!date1.isAfter(date2) && teacher!=null) {
            dataSource = timetableReportService.create(date1, date2,teacher);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("teacher",teacher.getLastName()+ " "+ teacher.getFirstName()+ " "+ teacher.getPatronymic());
        params.put("d1",fullDateString(date1));
        params.put("d2",fullDateString(date2));
        params.put("DS1",dataSource);
        byte[] bytes = reportService.generateReport("teachertimetable", params, new JREmptyDataSource(), format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/" + format + "; charset=UTF-8")
//                .header("Content-Type", "application/vnd.ms-excel" + "; charset=UTF-8")

                .header("Content-Disposition", "inline; filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
                .body(bytes);
    }
    private String fullDateString(LocalDate d){
        return d.getDayOfMonth()+" "+d.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru","RU"))+" "+d.getYear();

    }
}
