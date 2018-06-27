package com.hit.memoryunits;

import java.util.Arrays;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class Page<T> implements java.io.Serializable {

	/* Members */
	private static final long serialVersionUID = 1L;
	private T content;
	private Long pageID;

	/* Constructor */
	public Page(Long id, T content) {
		this.pageID = id;
		this.content = content;
	}
	
	public Page(Page<T> pageToCopy)
	{
		this.content = pageToCopy.content;
		this.pageID  = pageToCopy.pageID;
	}

	/* Public Methods */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		if (obj == this) {
			result = true;
		} else if ((obj != null) && (obj instanceof Page)) {
			Page<T> cloneObj = ((Page<T>)obj).clone();
			if (cloneObj.getContent() == this.content && cloneObj.pageID == this.pageID) {
				result = true;
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		return content.hashCode();
	}

	@Override
	public String toString() {
		return Arrays.toString((byte[]) content) + " " + pageID.toString();
	}

	public T getContent() {
		return content;
	}

	public Long getPageId() {
		return pageID;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setPageId(Long pageId) {
		this.pageID = pageId;
	}

	@Override
	public Page<T> clone() {
		return new Page<T>(pageID, content);
	}

}
