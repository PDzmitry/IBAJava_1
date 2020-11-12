package by.protasovitski.calendariba.report;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import by.protasovitski.calendariba.exceptions.ResourceNotFoundException;
import net.sf.jasperreports.engine.JRDataSource;

import java.time.LocalDate;

public interface TeacherReportService {
    JRDataSource create();
    JRDataSource create(LocalDate begin, LocalDate end);
    JRDataSource create(LocalDate begin, LocalDate end, Teacher teacher);
    JRDataSource create(LocalDate begin, LocalDate end, Group group);
    JRDataSource createConsolidatedByGroup(LocalDate begin, LocalDate end, Group group);

}
