package com.hit.processes;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;

/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class Process implements Callable<Boolean> {

	/* Members */
	private int ProcessId;
	private MemoryManagementUnit mmuInstance;
	private ProcessCycles processCycles;

	/* Constructor */
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles) {
		this.ProcessId = id;
		this.mmuInstance = mmu;
		this.processCycles = processCycles;
	}

	/* Public Methods */
	@Override
	public Boolean call() throws Exception {
		Boolean result = true;
		List<ProcessCycle> processCycleList = processCycles.getProcessCycles();

		for (ProcessCycle currentCycle : processCycleList) {
			synchronized (mmuInstance) {
				Long[] pagesFromCycle = new Long[currentCycle.getPages().size()];
				currentCycle.getPages().toArray(pagesFromCycle);

				try {
					Page<byte[]>[] pagesFromMMU = mmuInstance.getPages(pagesFromCycle);
					for (int i = 0; i < pagesFromMMU.length; i++) {
						pagesFromMMU[i].setContent(currentCycle.getData().get(i));
					}
				} catch (IOException ioe) {
					result = false;
				}
				try {
					Thread.sleep(currentCycle.getSleepMs());
				}

				catch (InterruptedException interrupted) {
					result = false;
				}
			}
		}

		return result;
	}

	public int getId() {
		return ProcessId;
	}

	public void setId(int id) {
		this.ProcessId = id;
	}

}
