package by.protasovitski.calendariba.report.impl;

import by.protasovitski.calendariba.report.DisciplineReportService;
import by.protasovitski.calendariba.service.DisciplineService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisciplineReportServiceImpl implements DisciplineReportService {

    @Autowired
    private DisciplineService disciplineService;

    @Override
    public JRDataSource create() {
        List<Map<String, Object>> result = new ArrayList<>();
        disciplineService.findAll()
                .forEach(discipline->{
                    Map<String, Object> item = new HashMap<>();
                    item.put("short_disc_name", discipline.getShortDisciplineName());
                    item.put("disc_name", discipline.getDisciplineName());
                    result.add(item);
                });
        return new JRBeanCollectionDataSource(result);
    }
}
