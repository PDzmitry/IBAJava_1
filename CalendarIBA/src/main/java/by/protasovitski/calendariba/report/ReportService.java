package by.protasovitski.calendariba.report;

import net.sf.jasperreports.engine.JRDataSource;

import java.util.Map;

public interface ReportService {
	/**
	 * Generates a HTML report with the given input file
	 * 
	 * @param inputFileName
	 *            report source file without extension
	 * @param params
	 *            report parameters
	 * @param dataSource
	 *            the source of data
	 * @return the byte[] containing the PDF
	 */
	byte[] generateReport(String inputFileName, Map<String, Object> params, JRDataSource dataSource,String format);
}
