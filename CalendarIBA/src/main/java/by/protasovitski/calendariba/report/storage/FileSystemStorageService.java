package by.protasovitski.calendariba.report.storage;


import by.protasovitski.calendariba.report.ReportsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileSystemStorageService implements StorageService {
	private final Path rootLocation;
	private final ReportsProperties properties;

	public FileSystemStorageService(ReportsProperties properties) throws IOException {
		this.properties = properties;
		this.rootLocation = Paths.get(properties.getStorageLocation().getURL().getPath());
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			FileSystemUtils.deleteRecursively(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not delete files and folders", e);
		}
	}

	@Override
	public boolean jrxmlFileExists(String file) {
		// @formatter:off
		try {
			Path reportFile = Paths.get(properties.getReportLocation().getURI());
			reportFile = reportFile.resolve(file + ".jrxml");
			if (Files.exists(reportFile))
				return true;
		} catch (IOException e) {
			log.error("Error while trying to get file URI", e);
			return false;
		}
		// @formatter:on
		return false;
	}

	@Override
	public boolean jasperFileExists(String file) {
		Path reportFile = rootLocation;
		reportFile = reportFile.resolve(file + ".jasper");
		if (Files.exists(reportFile))
			return true;
		return false;
	}

	@Override
	public String loadJrxmlFile(String file) {
		// @formatter:off
		try {
			URL res = getClass().getResource("/reports/"+file+".jrxml");
			log.info(res.getPath()+"!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Path reportFile = Paths.get(res.toURI());
//			Path reportFile = Paths.get(properties.getReportLocation().getURI());
//			reportFile = reportFile.resolve(file + ".jrxml");
			return reportFile.toString();
		} catch ( URISyntaxException e) {
			log.error("Error while trying to get file prefix", e);
			throw new StorageFileNotFoundException("Could not load file", e);
		}
		// @formatter:on
	}


	@Override
	public File loadJasperFile(String file) {
		Path reportFile = rootLocation;
		reportFile = reportFile.resolve(file + ".jasper");
		return reportFile.toFile();
	}

}
