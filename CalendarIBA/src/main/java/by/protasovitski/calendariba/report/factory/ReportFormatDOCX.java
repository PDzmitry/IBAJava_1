package by.protasovitski.calendariba.report.factory;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ReportFormatDOCX implements ReportFormat {

    @Override
    public byte[] generate(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArray));
            SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return byteArray.toByteArray();
        }catch (JRException | IOException e){
            log.error(e.getMessage());
        }
        return null;
    }
}
