package by.protasovitski.calendariba.report.impl;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.TimetableOfClasses;
import by.protasovitski.calendariba.report.TimetableReportService;
import by.protasovitski.calendariba.service.TimetableOfClassesService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Slf4j
public class TimetableReportServiceImpl implements TimetableReportService {

    @Autowired
    private TimetableOfClassesService timetableOfClassesService;

    @Value(value = "ru_Ru")
    private Locale locale;

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end) {
        List<Map<String, Object>> result = new ArrayList<>();
//        Locale locale = new Locale("ru","RU");
        TextStyle style = TextStyle.SHORT;
        Comparator<TimetableOfClasses> comparator = Comparator.comparing(TimetableOfClasses::getClassDate)
                .thenComparing(TimetableOfClasses::getSubgroup)
                .thenComparing(TimetableOfClasses::getBeginTime);
        List<TimetableOfClasses> classesList = timetableOfClassesService.findAllByClassDateIsBetweenAndStatusIsTrue(begin,end);
        log.info("Отобрано {} записей",classesList.size());
        classesList.sort(comparator);

        for (TimetableOfClasses timetable : classesList) {
            Map<String, Object> item= new HashMap<>();
            item.put("date",timetable.getClassDate().getDayOfMonth()+" "+timetable.getClassDate().getMonth().getDisplayName(style,locale));
            item.put("day",timetable.getClassDate().getDayOfWeek().getDisplayName(style,locale));
            item.put("time",timetable.getBeginTime()+"-"+timetable.getFinishTime());
            item.put("short_disc_name",timetable.getDiscipline().getShortDisciplineName());
            item.put("classroom",timetable.getClassroom().getNumber());
            item.put("short_type_work",timetable.getTypeOfWork().getShort_description());
            item.put("teacher",timetable.getTeacher().getLastName()+
                    " "+timetable.getTeacher().getFirstName().substring(0,1)+"."+
                    timetable.getTeacher().getPatronymic().substring(0,1)+".");
            item.put("group",timetable.getGroup().getGroupName());
            item.put("subgroup",String.valueOf(timetable.getSubgroup()));
            result.add(item);
        }
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end, Group group) {
        List<Map<String, Object>> result = new ArrayList<>();
//        Locale locale = new Locale("ru","RU");
        TextStyle style = TextStyle.SHORT;
        Comparator<TimetableOfClasses> comparator = Comparator.comparing(TimetableOfClasses::getClassDate)
                .thenComparing(TimetableOfClasses::getSubgroup)
                .thenComparing(TimetableOfClasses::getBeginTime);
        List<TimetableOfClasses> classesList = timetableOfClassesService.findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(begin,end,group);
        classesList.sort(comparator);

        for (TimetableOfClasses timetable : classesList) {
            Map<String, Object> item = new HashMap<>();
            item.put("date",timetable.getClassDate().getDayOfMonth()+" "+timetable.getClassDate().getMonth().getDisplayName(style,locale));
            item.put("day",timetable.getClassDate().getDayOfWeek().getDisplayName(style,locale));
            item.put("time",timetable.getBeginTime()+"-"+timetable.getFinishTime());
            item.put("short_disc_name",timetable.getDiscipline().getShortDisciplineName());
            item.put("classroom",timetable.getClassroom().getNumber());
            item.put("short_type_work",timetable.getTypeOfWork().getShort_description());
            item.put("teacher",timetable.getTeacher().getLastName()+
                    " "+timetable.getTeacher().getFirstName().substring(0,1)+"."+
                    timetable.getTeacher().getPatronymic().substring(0,1)+".");
            item.put("group",timetable.getGroup().getGroupName());
            item.put("subgroup",String.valueOf(timetable.getSubgroup()));
                        result.add(item);
        }
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end, Teacher teacher) {
        List<Map<String, Object>> result = new ArrayList<>();
//        Locale locale = new Locale("ru","RU");
        TextStyle style = TextStyle.SHORT;
        Comparator<TimetableOfClasses> comparator = Comparator.comparing(TimetableOfClasses::getClassDate)
                .thenComparing(TimetableOfClasses::getSubgroup)
                .thenComparing(TimetableOfClasses::getBeginTime);
        List<TimetableOfClasses> classesList = timetableOfClassesService.findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(begin,end,teacher);
        classesList.sort(comparator);

        for (TimetableOfClasses timetable : classesList) {
            Map<String, Object> item = new HashMap<>();
            item.put("date",timetable.getClassDate().getDayOfMonth()+" "+timetable.getClassDate().getMonth().getDisplayName(style,locale));
            item.put("day",timetable.getClassDate().getDayOfWeek().getDisplayName(style,locale));
            item.put("time",timetable.getBeginTime()+"-"+timetable.getFinishTime());
            item.put("short_disc_name",timetable.getDiscipline().getShortDisciplineName());
            item.put("classroom",timetable.getClassroom().getNumber());
            item.put("short_type_work",timetable.getTypeOfWork().getShort_description());
            item.put("teacher",timetable.getTeacher().getLastName()+
                    " "+timetable.getTeacher().getFirstName().substring(0,1)+"."+
                    timetable.getTeacher().getPatronymic().substring(0,1)+".");
            item.put("group",timetable.getGroup().getGroupName());
            item.put("subgroup",String.valueOf(timetable.getSubgroup()));
            result.add(item);
        }
        return new JRBeanCollectionDataSource(result);
    }
}
