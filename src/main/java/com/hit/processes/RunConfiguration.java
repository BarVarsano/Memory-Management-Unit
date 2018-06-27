package com.hit.processes;

import java.util.List;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class RunConfiguration {
	/* Members */
	private List<ProcessCycles> processesCycles;

	/* Constructor */
	public RunConfiguration(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}

	/* Public Methods */
	public List<ProcessCycles> getProcessesCycles() {
		return this.processesCycles;
	}

	public void setProcessesCycles(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}

	@Override
	public String toString() {
		return "RunConfiguration [processesCycle=" + processesCycles + "]";
	}
}
