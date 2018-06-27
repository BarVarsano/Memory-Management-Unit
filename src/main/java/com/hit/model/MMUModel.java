package com.hit.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;

import javax.security.auth.login.Configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.driver.CLI;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;


/*
* Bar Varsano 307929497
* Amit Levy 305650517
*/
public class MMUModel extends Observable implements Model {
	/* Members */
	private int ramCapacity;
	private int numProcesses;
	private String DEFAULT_FILE_NAME = "log.txt";
	private String CONFIG_FILE_NAME = "src/main/resources/com/hit/config/Configuration.json‬";
	private static String[] configuration;
	private BufferedReader logReader;
	private List<String> logData;
	int currentLogIndex;
	private String jsonFileName;
	private Map<Long, Page<byte[]>> ramPagesInTheEnd;
	private MemoryManagementUnit mmu ;
	private List<Process> processes;
	
	public MMUModel() {
		try {
			logReader = new BufferedReader(new FileReader(DEFAULT_FILE_NAME));
			currentLogIndex = 0;
			logData = new ArrayList<String>();
			//start();
			logReader.close();
		} catch (FileNotFoundException e) {
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
		} catch (IOException e) {
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
		}
		currentLogIndex = 2;
	}
	
	/* Public Methods */
	@Override
	public void start() {
		IAlgoCache<Long, Long> algo = null;
		String algoCommand = configuration[0];
		ramCapacity = Integer.parseInt(configuration[1]);

		switch (algoCommand.toUpperCase()) {
		case "LRU":
			algo = new LRUAlgoCacheImpl<Long, Long>(ramCapacity);
			break;
		case "MRU":
			algo = new MRUAlgoCacheImpl<Long, Long>(ramCapacity);
			break;
		case "RANDOM":
			algo = new com.hit.algorithm.Random<Long, Long>(ramCapacity);
			break;
		}

		mmu = new MemoryManagementUnit(ramCapacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
		processes = createProcesses(processCycles, mmu);
		this.numProcesses = processes.size();
		MMULogger.getInstance().write("PN:" + this.numProcesses + "\r\n\r\n", Level.INFO);
	}

	public void restartRun()
	{
		currentLogIndex = 2;
	}
	public int getRamCapacity() {
		return ramCapacity;
	}
	
	public int getNumProcesses() {
		return numProcesses;
	}

	public void setConfiguration(String configuration) {
		this.jsonFileName = configuration;
	}
	
	public void setConfiguration(List<String> configuration) {
	this.configuration = (String[]) configuration.toArray();
}
	/* Private Methods */
	private static RunConfiguration readConfigurationFile() {

		String pe = "src/main/resources/com/hit/config/Configuration.json";
		try {
			return new Gson().fromJson(new JsonReader(new FileReader(pe)), RunConfiguration.class);
		} catch (FileNotFoundException e) {
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
		}

		return null;
	}

	private static List<Process> createProcesses(List<ProcessCycles> processCycles, MemoryManagementUnit mmu) {
		List<Process> processList = new ArrayList<Process>();

		for (int i = 0; i < processCycles.size(); i++) {
			ProcessCycles currentProcess = processCycles.get(i);
			Process newProcess = new Process(i, mmu, currentProcess);
			processList.add(newProcess);
		}

		return processList;
	}

	private static void runProcesses(List<Process> processes) {
		for (Process pr : processes) {
			FutureTask task = new FutureTask(pr);
			Thread currentThread = new Thread(task);
			currentThread.start();
		}
	}
	
	public Map<Long, Page<byte[]>> getRamPagesInTheEnd() 
	{
		return ramPagesInTheEnd;
	}
	
	public void startProcessProgress(List<Process> actualProcessesToRun)
	{
		runProcesses(actualProcessesToRun);
		try 
		{
			Thread.sleep((long) 100.0);   //The main program needs to be paused a little in order to let all its threads to finish
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		this.ramPagesInTheEnd = mmu.getRam().getPages();
		readLogFile();
		setChanged();
		notifyObservers(this.logData);
	}
	
	public void readLogFile() 
	{
		try 
		{
			this.logData = Files.readAllLines(Paths.get(MMULogger.DEFAULT_FILE_NAME));
		} 
		catch (IOException exception) 
		{
			MMULogger.getInstance().write(exception.getMessage(), Level.SEVERE);
		}
	}
	
	public List<Process> getProcesses()
	{
		return this.processes;
	}
}
