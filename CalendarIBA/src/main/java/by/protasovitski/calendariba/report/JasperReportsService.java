package by.protasovitski.calendariba.report;


import by.protasovitski.calendariba.exceptions.ResourceNotFoundException;
import by.protasovitski.calendariba.report.factory.FactoryReportFormat;
import by.protasovitski.calendariba.report.factory.ReportFormat;
import by.protasovitski.calendariba.report.factory.ReportFormatXLSX;
import by.protasovitski.calendariba.report.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class JasperReportsService implements ReportService {
    private final StorageService storageService;

    @Autowired
    private final FactoryReportFormat factoryReportFormat;

    @Override
    public byte[] generateReport(String inputFileName, Map<String, Object> params,
                                 JRDataSource dataSource, String format) {
        byte[] bytes = null;
        JasperReport jasperReport = null;
        try  {
            // Check if a compiled report exists
            if (storageService.jasperFileExists(inputFileName)) {
                jasperReport = (JasperReport) JRLoader
                        .loadObject(storageService.loadJasperFile(inputFileName));
            }
            // Compile report from source and save
            else {
//                String jrxml = storageService.loadJrxmlFile(inputFileName);
//                log.info("{} loaded. Compiling report", jrxml);
//                jasperReport =  JasperCompileManager.compileReport(jrxml);
                // Save compiled report. Compiled report is loaded next time
                InputStream is = getClass().getResourceAsStream("/reports/"+inputFileName + ".jrxml");
                jasperReport = JasperCompileManager.compileReport(is);
                is.close();
                JRSaver.saveObject(jasperReport,
                        storageService.loadJasperFile(inputFileName));
            }
            ReportFormat reportFormat = factoryReportFormat.format(format);
            if (reportFormat.getClass() == ReportFormatXLSX.class){
                params.put(JRParameter.REPORT_LOCALE, new Locale("ru","RU"));
                params.put(JRParameter.IS_IGNORE_PAGINATION, true);
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
                    dataSource);

            bytes = reportFormat.generate(jasperPrint);
        } catch (JRException | ResourceNotFoundException | IOException e) {
            e.printStackTrace();
            log.error("Encountered error when loading jasper file", e);
        }

        return bytes;
    }
}
