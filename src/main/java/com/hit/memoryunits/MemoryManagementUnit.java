package com.hit.memoryunits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.util.MMULogger;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class MemoryManagementUnit {
	/* Members */
	private IAlgoCache<Long, Long> algo;
	private RAM ram;

	/* Constructor */
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo) {
		this.algo = algo;
		ram = new RAM(ramCapacity);
	}

	/* Public Methods */
	public Page<byte[]>[] getPages(Long[] pageIds) throws java.io.IOException {
		Page<byte[]>[] pagesReturned = new Page[pageIds.length];
		for (int i = 0; i < pagesReturned.length; i++) {
			if (this.ram.getPage(pageIds[i]) == null) {
				Page<byte[]> pageFromHD = HardDisk.getInstance().pageFault(pageIds[i]);
				if (!this.ram.isFull()) {
					pagesReturned[i] = pageFromHD;
					this.ram.addPage(pageFromHD);
					this.algo.putElement(pageFromHD.getPageId(), pageFromHD.getPageId());
					MMULogger.getInstance().write("PF: " + pageIds[i]  + "\r\n", Level.INFO);
				} else {
					Long localKey = algo.putElement(pageFromHD.getPageId(), pageFromHD.getPageId());
					Page<byte[]> pageToHD = new Page<byte[]>(ram.getPage(localKey));
					HardDisk.getInstance().pageReplacement(pageToHD, pageIds[i]);
					this.ram.removePage(pageToHD);
					this.ram.addPage(pageFromHD);
					pagesReturned[i] = pageFromHD;
					MMULogger.getInstance().write("PR:MTH " + pageToHD.getPageId() + " MTR " + pageIds[i] + "\r\n" , Level.INFO);
				}
			} else {
				pagesReturned[i] = this.ram.getPage(pageIds[i]);
			}
		}

		return pagesReturned;

	}

	public void shutDown() throws java.io.FileNotFoundException, java.io.IOException {
		HardDisk hd = HardDisk.getInstance();

		Map<Long, Page<byte[]>> ramMemory = ram.getPages();
		for (Map.Entry<Long, Page<byte[]>> page : ramMemory.entrySet())
			hd.pageReplacement(page.getValue(), null);
	}

	public IAlgoCache<Long, Long> getAlgo() {
		return algo;
	}

	public void setAlgo(IAlgoCache<Long, Long> algo) {
		this.algo = algo;
	}

	public RAM getRam() {
		return this.ram;
	}

	public void setRam(RAM ram) {
		this.ram = ram;
	}

	public void update() {
	}
}