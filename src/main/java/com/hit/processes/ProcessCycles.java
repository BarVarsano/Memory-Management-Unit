package com.hit.processes;

import java.util.List;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class ProcessCycles {

	/* Members */
	private List<ProcessCycle> processCycles;

	/* Constructor */
	public ProcessCycles(List<ProcessCycle> processCycles) {
		this.processCycles = processCycles;
	}

	/* Public Methods */
	public List<ProcessCycle> getProcessCycles() {
		return this.processCycles;
	}

	public void setProcessCycles(List<ProcessCycle> processCycles) {
		this.processCycles = processCycles;
	}

	@Override
	public java.lang.String toString() {
		return "ProcessCycles [processCycles=" + processCycles + "]";
	}

}
