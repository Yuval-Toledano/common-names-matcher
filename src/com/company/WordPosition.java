package com.company;

public class WordPosition {
	private final int lineOffset;
	private final int charOffset;

	WordPosition(int lineOffset, int charOffset){
		this.lineOffset = lineOffset;
		this.charOffset = charOffset;
	}

	@Override
	public String toString() {
		return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
	}
}
