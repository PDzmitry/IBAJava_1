package by.protasovitski.calendariba.report.impl;

import by.protasovitski.calendariba.report.GroupReportService;
import by.protasovitski.calendariba.service.GroupService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupReportServiceImpl implements GroupReportService {

    @Autowired
    private GroupService groupService;


    @Override
    public JRDataSource create() {
        List<Map<String, Object>> result = new ArrayList<>();

        groupService.findAll()
                .forEach(group->{
                    Map<String, Object> item = new HashMap<>();
                    item.put("group",group.getGroupName());
                    item.put("course",group.getDescriptionOfPlan().getTypeOfCourse().getShort_description().toLowerCase());
                    item.put("education",group.getTypeOfEducation().getShort_description().toLowerCase());
                    item.put("plan",group.getDescriptionOfPlan().getDescription());
                    result.add(item);
                });
        return new JRBeanCollectionDataSource(result);
    }
}
