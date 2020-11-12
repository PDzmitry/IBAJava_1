package by.protasovitski.calendariba.report.factory;

import by.protasovitski.calendariba.exceptions.ResourceNotFoundException;
import by.protasovitski.calendariba.report.enums.FormatReport;
import org.springframework.stereotype.Component;

@Component
public class FactoryReportFormat {

    public ReportFormat format(String format) throws ResourceNotFoundException {
        format = format.toUpperCase();
        FormatReport formatEnum = FormatReport.valueOf(format);

        switch (formatEnum) {
            case PDF:
                return new ReportFormatPDF();
            case DOCX:
                return new ReportFormatDOCX();
            case XLSX:
                return new ReportFormatXLSX();
            case HTML:
                return new ReportFormatHTML();
            default:
                throw new ResourceNotFoundException("Format" + format + "not found");
        }

    }
}
