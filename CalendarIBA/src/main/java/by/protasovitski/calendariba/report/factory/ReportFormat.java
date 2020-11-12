package by.protasovitski.calendariba.report.factory;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportFormat {
    byte[] generate(JasperPrint jasperPrint);
}
