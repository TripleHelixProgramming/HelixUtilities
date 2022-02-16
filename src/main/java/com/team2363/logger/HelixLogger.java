package com.team2363.logger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.DriverStation;

public class HelixLogger {

	private static HelixLogger INSTANCE = new HelixLogger();


	public static HelixLogger getInstance() {
		return INSTANCE;
	}

	private final List<LogSource> dataSources = new ArrayList<>();
	private Path file;
	
	private String loggingLocation = "/home/lvuser/logs/";
	
	private HelixLogger() {
		File usb1 = new File("/media/sda1/");
		if (usb1.exists()) {
			loggingLocation = "/media/sda1/logs/";
		}
	}
	
	
	private void createLogDirectory() throws IOException {
		File logDirectory = new File(loggingLocation);
		if (!logDirectory.exists()) {
			Files.createDirectory(Paths.get(loggingLocation));
		}
	}
	
	private void createFile() {
		Writer output = null;
		try {
			createLogDirectory();
			if (DriverStation.isFMSAttached()) {
				file = Paths.get(loggingLocation + 
						DriverStation.getEventName() + "_"+ 
						DriverStation.getMatchType() + 
						DriverStation.getMatchNumber() + ".csv");
			} else {
				file = Paths.get(loggingLocation + "test.csv");
			}
			if (Files.exists(file)) {
				Files.delete(file);
			}
			Files.createFile(file);
			saveTitles();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) { }
			}
		}
	}

	public void addSource(String name, Supplier<Object> supplier) {
		dataSources.add(new LogSource(name, supplier));
	}

	public void saveLogs() {
		try {
			if (file == null) {
				createFile();
			}

			StringBuilder data = new StringBuilder();
			data.append(Instant.now().toString()).append(",");
			data.append(DriverStation.getMatchTime()).append(",");
			data.append(getValues());
			Files.write(file, Collections.singletonList(data.toString()), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveTitles() throws IOException {
		StringBuilder titles = new StringBuilder();
		titles.append("Timestamp,");
		titles.append("match_time,");
		titles.append(dataSources.stream().map(t -> t.name).collect(Collectors.joining(","))).append(",");
		Files.write(file, Collections.singletonList(titles.toString()), StandardOpenOption.APPEND);
	}

	private String getValues() {
		return dataSources.stream().map(s -> s.supplier.get()).map(Object::toString).collect(Collectors.joining(","));
	}

	private class LogSource {
		private final String name;
		private final Supplier<Object> supplier;

		public LogSource(String name, Supplier<Object> supplier) {
			this.name = name;
			this.supplier = supplier;
		}
	}
}