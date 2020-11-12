package by.protasovitski.calendariba.controller.rest.report;


import by.protasovitski.calendariba.controller.rest.ConstantURL;
import by.protasovitski.calendariba.report.DisciplineReportService;
import by.protasovitski.calendariba.report.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(ConstantURL.URL_CROSS_ORIGINS)
@RequestMapping(ConstantURL.URL_DISCIPLINE_REPORT)
@Api(tags = "report disciplines", description = "Disciplines ReportFormat API")
public class DisciplineReportController {
    private ReportService reportService;
    private DisciplineReportService disciplineReportService;

    @Autowired
    public DisciplineReportController(ReportService reportService, DisciplineReportService disciplineReportService) {
        this.reportService = reportService;
        this.disciplineReportService = disciplineReportService;
    }

    @GetMapping(value = "/{format}/{username}")
    @ApiOperation(value = "List teachers", notes = "List all disciplines")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Disciplines found"),
            @ApiResponse(code = 404, message = "Disciplines  not found")
    })
//    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_VIEW')")
    public ResponseEntity<byte[]> reportDisciplines(@PathVariable(required = false) String username,
                                         @PathVariable(required = false) String format) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        JRDataSource dataSource = disciplineReportService.create();
        params.put("DS1", dataSource);
        byte[] bytes = reportService.generateReport("alldisciplines",params,new JREmptyDataSource(),format);
        return ResponseEntity
                .ok()
                .header("Content-Type", "application/"+format+"; charset=UTF-8")
                .header("Content-Disposition", "filename=\"" + username + "." + format + "\"")
                .contentLength(bytes.length)
//                .header("Content-Disposition", "attachment; filename=\"" + username + "."+format+"\"")
                .body(bytes);
    }
}
