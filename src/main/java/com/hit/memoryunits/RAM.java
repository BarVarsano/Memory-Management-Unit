package com.hit.memoryunits;

import java.util.HashMap;
import java.util.Map;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class RAM {
	/* Members */
	private int capacity;
	private HashMap<Long, Page<byte[]>> pageMap;

	/* Constructor */
	public RAM(int initialCapacity) {
		capacity = initialCapacity;
		pageMap = new HashMap<Long, Page<byte[]>>(initialCapacity);
	}
	
	/* Public Methods */
	public String toString() {
		return "initialCapacity: " + capacity + ", current size: " + pageMap.size() + ", pages: " + pageMap ;
	}
	
	public void addPage(Page<byte[]> addPage) {
		pageMap.put(addPage.getPageId(), addPage);
	}

	public void addPages(Page<byte[]>[] addPages) {
		for (Page<byte[]> page : addPages) {
			pageMap.put(page.getPageId(), page);
		}
	}

	public int getInitialCapacity() {
		return capacity;
	}

	public Page<byte[]> getPage(Long pageId) {
		return pageMap.get(pageId);
	}

	public Map<Long, Page<byte[]>> getPages() {
		return pageMap;
	}

	public Page<byte[]>[] getPages(Long[] pageIds) {
		Page<byte[]>[] returnedPages = new Page[pageIds.length];
		for (int i = 0; i < pageIds.length; i++) {
			Page<byte[]> page = pageMap.get(pageIds[i]);
			if (page != null)
			{
				returnedPages[i] = page;
			}
		}
		return returnedPages;
	}

	public void removePage(Page<byte[]> removePage) {
		pageMap.remove(removePage.getPageId());
	}

	public void removePages(Page<byte[]>[] removePages) {
		for (Page<byte[]> page : removePages) {
			pageMap.remove(page.getPageId(), page);
		}
	}

	public void setInitialCapacity(int initialCapacity) {
		this.capacity = initialCapacity;
	}

	public void setPages(Map<java.lang.Long, Page<byte[]>> pages) {
		pageMap = new HashMap<Long, Page<byte[]>>(pages);
	}
	
	public boolean isFull() {
		return pageMap.size() == capacity;
	}
}
