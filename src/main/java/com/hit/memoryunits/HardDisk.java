package com.hit.memoryunits;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.hit.exception.HardDiskException;
import com.hit.util.MMULogger;

/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class HardDisk {
	/* Members */
	private static final int SIZE = 100;
	private static final String DEFAULT_FILE_NAME = "HardDisk.bin";
	private static HardDisk HDinstance;
	private HashMap<Long, Page<byte[]>> pages;
	private static final Object lock = new Object();

	/* Constructor */
	private HardDisk() {
		pages = new LinkedHashMap<Long, Page<byte[]>>(SIZE);
		for (Long i = (long) 0; i < SIZE; i++) {
			Page<byte[]> page = new Page<>(i, new byte[0]);
			pages.put(page.getPageId(), page);
		}
		updateHd();
	}

	/* Public Methods */
	public static HardDisk getInstance() {
		// Checking whether the instance has been created before.
		if (HDinstance == null) {
			// preventing from other threads to interrupt current thread.
			synchronized (lock) {
				// Checking again whether the instance has been created or not,
				// double check.
				if (HDinstance == null)
					HDinstance = new HardDisk();
			}
		}

		return HDinstance;
	}

	public Page<byte[]> pageFault(Long pageId) throws HardDiskException {
		return pages.get(pageId);
	}

	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws HardDiskException {
		pages.put(moveToHdPage.getPageId(), moveToHdPage);
		return pages.get(moveToRamId);
	}

	/* Private Methods */
	private void updateHd() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream(DEFAULT_FILE_NAME);
			oos = new ObjectOutputStream(fout);
			for (Entry<Long, Page<byte[]>> p : this.pages.entrySet()) {
				oos.writeObject(p.getValue().getPageId());
				oos.writeObject(p.getValue().getContent());
			}
		} catch (Exception e) {
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
		} finally {
			try {
				if (oos != null) {
					oos.close();
					fout.close();
				}
			} catch (IOException e) {
				MMULogger.getInstance().write(
						"Error while trying to close the write stream / HardDisk.writeData()" + System.lineSeparator(),
						Level.SEVERE);
			}
		}
	}

	private void readHd() throws FileNotFoundException, IOException {
		boolean isOkToKeepReading = true;
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(DEFAULT_FILE_NAME));
			while (isOkToKeepReading) {
				Page<byte[]> currentPage = (Page<byte[]>) inputStream.readObject();
				if (currentPage != null) {
					this.pages.put(currentPage.getPageId(), currentPage);
				} else {
					isOkToKeepReading = false;
				}
			}

			inputStream.close();
		} catch (EOFException ex) {
			MMULogger.getInstance().write("Finished loading data to Hard Disk." + System.lineSeparator(), Level.SEVERE);
		} catch (Exception e) {
			MMULogger.getInstance().write(
					"Error while trying to close the read stream / HardDisk.readData()" + System.lineSeparator(),
					Level.SEVERE);
		}
	}

}
