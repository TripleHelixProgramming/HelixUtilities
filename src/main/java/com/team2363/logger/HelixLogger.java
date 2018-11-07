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
			if (DriverStation.getInstance().isFMSAttached()) {
				file = Paths.get(loggingLocation + 
						DriverStation.getInstance().getEventName() + "_"+ 
						DriverStation.getInstance().getMatchType() + 
						DriverStation.getInstance().getMatchNumber() + ".csv");
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
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addDoubleSource(String name, Supplier<Double> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addIntegerSource(String name, Supplier<Integer> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addLongSource(String name, Supplier<Long> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addBooleanSource(String name, Supplier<Boolean> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addStringSource(String name, Supplier<String> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void addCharacterSource(String name, Supplier<Character> supplier) {
		dataSources.add(new LogSource(name, () -> supplier.get().toString()));
	}

	public void saveLogs() {
		try {
			if (file == null) {
				createFile();
			}

			StringBuilder data = new StringBuilder();
			data.append(Instant.now().toString()).append(",");
			data.append(DriverStation.getInstance().getMatchTime()).append(",");
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
		return dataSources.stream().map(s -> s.supplier.get()).collect(Collectors.joining(","));
	}

	private class LogSource {
		private final String name;
		private final Supplier<String> supplier;

		public LogSource(String name, Supplier<String> supplier) {
			this.name = name;
			this.supplier = supplier;
		}
	}
}