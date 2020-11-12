package by.protasovitski.calendariba.controller.rest.report;


import by.protasovitski.calendariba.controller.rest.ConstantURL;
import by.protasovitski.calendariba.report.DisciplineReportService;
import by.protasovitski.calendariba.report.GroupReportService;
import by.protasovitski.calendariba.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_GROUP_REPORT)
@Api(tags = "report groups", description = "Groups ReportFormat API")
public class GroupReportController {
    private ReportService reportService;
    private GroupReportService groupReportService;

    @Autowired
    public GroupReportController(ReportService reportService, GroupReportService groupReportService) {
        this.reportService = reportService;
        this.groupReportService = groupReportService;
    }




    @GetMapping(value = "/{format}/{username}")
    @ApiOperation(value = "List groups", notes = "List all groups")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Groups found"),
            @ApiResponse(code = 404, message = "Groups  not found")
    })
    public ResponseEntity<byte[]> reportGroups(@PathVariable(required = false) String username,
                                         @PathVariable(required = false) String format) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        JRDataSource dataSource = groupReportService.create();
        params.put("DS1", dataSource);
        byte[] bytes = reportService.generateReport("allgroups",params,new JREmptyDataSource(),format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/"+format+"; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
//                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }
}
