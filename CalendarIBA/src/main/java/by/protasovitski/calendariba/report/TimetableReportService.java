package by.protasovitski.calendariba.report;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import net.sf.jasperreports.engine.JRDataSource;

import java.time.LocalDate;

public interface TimetableReportService {
    JRDataSource create(LocalDate begin, LocalDate end);
    JRDataSource create(LocalDate begin, LocalDate end, Group group);
    JRDataSource create(LocalDate begin, LocalDate end, Teacher teacher);
}
