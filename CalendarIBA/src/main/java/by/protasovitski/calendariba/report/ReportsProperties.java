package by.protasovitski.calendariba.report;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "report.properties")
public class ReportsProperties {
	/**
	 * The base path where reports will be stored after compilation
	 */
	@NotNull
	private Resource storageLocation;
	/**
	 * The location of JasperReports source files
	 */
	@NotNull
	private Resource reportLocation;

}
