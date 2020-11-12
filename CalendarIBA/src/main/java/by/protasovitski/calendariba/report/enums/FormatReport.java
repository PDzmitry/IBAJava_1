package by.protasovitski.calendariba.report.enums;

public enum FormatReport {
    PDF("PDF"), XLSX("XLSX"), DOCX("DOCX"),HTML("HTML");

    private String format;

    FormatReport(String format) {
        this.format = format;
    }
}
