package org.hurricanegames.packetholograms.utils;

public class MutableBoolean {

	private boolean value;

	public MutableBoolean() {
		this.value = false;
	}

	public MutableBoolean(boolean value) {
		this.value = value;
	}

	public void set(boolean newVaule) {
		this.value = newVaule;
	}

	public boolean get() {
		return value;
	}

}
