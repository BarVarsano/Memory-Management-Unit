//package com.hit.driver;
//
//import java.util.List;
//
//import com.hit.algorithm.IAlgoCache;
//import com.hit.algorithm.LRUAlgoCacheImpl;
//import com.hit.algorithm.MRUAlgoCacheImpl;
//import com.hit.algorithm.Random;
//import com.hit.exception.HardDiskException.ActionError;
//import com.hit.memoryunits.MemoryManagementUnit;
//import com.hit.processes.ProcessCycles;
//import com.hit.processes.RunConfiguration;
//
///*
// * Bar Varsano 307929497
// * Amit Levy 305650517
// */
////TODO
//public class MMUDriver {
//
//	/* Constructor */
//	public MMUDriver() {
//	}
//
//	/* Public Methods */
//
//	 public static void main(String[] args) {
//	 CLI cli = new CLI(System.in, System.out);
//	 new Thread(cli).start();
//	 }
//	 public static void start(String[] command) {
//	 IAlgoCache<Long, Long> algo = null;
//	 int capacity = 0;
//	 
//	 capacity=Integer.parseInt(command[1]);
//	 
//	 switch(command[0])
//	 {
//	 case "LRU":
//			algo=new LRUAlgoCacheImpl<>(capacity);
//			break;
//		case "MRU":
//			algo=new MRUAlgoCacheImpl<>(capacity);
//			break;
//		 case "Random":
//algo=new Random<>(capacity);
//break;
//		default:
//			throw new IllegalArgumentException("Value does not exist.");
//	 }
//	 /**
//	 * Initialize capacity and algo
//	 */
//	 /**
//	 * Build MMU and initial RAM (content)
//	 */
//	 MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
//	// RunConfiguration runConfig = readConfigurationFile();
//	// List<ProcessCycles> processCycles = runConfig.getProcessesCycles();
//	// List<Process> processes = createProcesses(processCycles, mmu);
//	// runProcesses(processes);
//	 }
//	 public static void runProcesses(java.util.List<Process> applications) {}
//	 //public static List<Process> createProcesses(List<ProcessCycles>
////	 appliocationsScenarios,MemoryManagementUnit mmu){}
//	 //public static RunConfiguration readConfigurationFile()()
//}

package com.hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.controller.MMUController;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.model.MMUModel;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;
import com.hit.view.MMUView;
import com.hit.processes.Process;

/*
* Bar Varsano 307929497
* Amit Levy 305650517
*/
public class MMUDriver {

	public MMUDriver() {

	}

	/* Public Method */
	public static void main(String[] args) {
		CLI cli = new CLI(System.in, System.out);
		MMUModel model = new MMUModel();
		MMUView view = new MMUView();
		MMUController controller = new MMUController(model, view);
		model.addObserver(controller);
		cli.addObserver(controller);
		view.addObserver(controller);
		new Thread(cli).start();
	}
}
