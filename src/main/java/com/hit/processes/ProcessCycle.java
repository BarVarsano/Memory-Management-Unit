package com.hit.processes;

import java.util.List;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class ProcessCycle {
	/* Members */
	private List<Long> pages;
	private List<byte[]> data;
	private int sleepMs;

	/* Constructor */
	public ProcessCycle(List<Long> pages, int sleepMs, List<byte[]> data) {
		this.data = data;
		this.pages = pages;
		this.sleepMs = sleepMs;
	}

	/* Public Methods */
	public List<Long> getPages() {
		return pages;
	}

	public void setPages(List<Long> pages) {
		this.pages = pages;
	}

	public int getSleepMs() {
		return this.sleepMs;
	}

	public void setSleepMs(int sleepMs) {
		this.sleepMs = sleepMs;
	}

	public List<byte[]> getData() {
		return this.data;
	}

	public void setData(List<byte[]> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ProcessCycle [sleepMs=" + sleepMs + ", data=" + data + ", pages=" + pages + "]";
	}
}
