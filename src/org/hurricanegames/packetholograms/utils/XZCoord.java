package org.hurricanegames.packetholograms.utils;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class XZCoord {

	protected final int x;
	protected final int z;

	public XZCoord(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(getClass())) {
			return false;
		}
		XZCoord other = (XZCoord) obj;
		return (x == other.x) && (z == other.z);
	}

	public Iterable<XZCoord> iterateTo(XZCoord other) {
		if ((other.getX() < getX()) || (other.getZ() < getZ())) {
			throw new IllegalArgumentException("Other coordinates should be bigger");
		}
		return () -> new Iterator<XZCoord>() {
			protected XZCoord next = new XZCoord(getX(), getZ());
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public XZCoord next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				XZCoord ret = next;
				int x = next.getX();
				int z = next.getZ();
				if (z < other.getZ()) {
					next = new XZCoord(x, z + 1);
				} else if (x < other.getX()) {
					next = new XZCoord(x + 1, getZ());
				} else {
					next = null;
				}
				return ret;
			}
		};
	}

	public boolean isInAABB(XZCoord otherMin, XZCoord otherMax) {
		return
			(x >= otherMin.getX()) && (x <= otherMax.getX()) &&
			(z >= otherMin.getZ()) && (z <= otherMax.getZ());
	}

	@Override
	public int hashCode() {
		return (x * 31) ^ z;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0}(x: {1}, z: {2})", getClass().getSimpleName(), getX(), getZ());
	}


	public static XZCoord getChunkCoord(Block block) {
		return new XZCoord(block.getX() >> 4, block.getZ() >> 4);
	}

	public static XZCoord getChunkCoord(Vector location) {
		return new XZCoord(location.getBlockX() >> 4, location.getBlockZ() >> 4);
	}

}
