package by.protasovitski.calendariba.report.factory;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ReportFormatXLSX implements ReportFormat {

    @Override
    public byte[] generate(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArray));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            return byteArray.toByteArray();
        }catch (JRException | IOException e){
            log.error(e.getMessage());
        }
        return null;
    }
}
