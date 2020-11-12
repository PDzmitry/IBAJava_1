package by.protasovitski.calendariba.report.factory;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ReportFormatHTML implements ReportFormat {
    @Override
    public byte[] generate(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            HtmlExporter exporter = new HtmlExporter();
            SimpleHtmlExporterOutput htmlExporterOutput = new SimpleHtmlExporterOutput(byteArray,"UTF-8");
            exporter.setExporterOutput(htmlExporterOutput);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.exportReport();
            return byteArray.toByteArray();
        }catch (JRException | IOException e){
            log.error(e.getMessage());
        }
        return null;
    }
}
