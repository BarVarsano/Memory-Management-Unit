package com.hit.exception;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
public class HardDiskException extends IOException {
	/* Members */
	private static final long serialVersionUID = 1L;

	public enum ActionError {
		PAGE_FAULT, PAGE_REPLACEMENT
	}

	/* Constructor */
	public HardDiskException() {
		super();
	}

	/* Public Methods */
	public HardDiskException(java.lang.String msg) {
		super(msg);
	}

	public HardDiskException(java.lang.String msg, HardDiskException.ActionError status) {
		super(msg + " " + status.toString());
	}

	public static HardDiskException.ActionError[] values() {
		return new ActionError[] { ActionError.PAGE_FAULT, ActionError.PAGE_REPLACEMENT };
	}

	public static HardDiskException.ActionError valueOf(String name) {
		ActionError error;
		if (name == null) {
			throw new NullPointerException("The value can not be empty.");
		}
		switch (name) {
		case "PAGE_FAULT":
			error = ActionError.PAGE_FAULT;
			break;
		case "PAGE_REPLACEMENT":
			error = ActionError.PAGE_REPLACEMENT;
			break;
		default:
			throw new IllegalArgumentException("Value does not exist.");
		}

		return error;
	}
}
