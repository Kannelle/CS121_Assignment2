/*
Kristina Wong, 76513468
Haoming Li, 20426226
Shengjie Xu, 10616769
Yirui Jiang, 64137163
*/

package ir.assignments.three;

/**
 * Basic class for pairing a word/2-gram/palindrome with its frequency.
 * 
 * DO NOT MODIFY THIS CLASS
 */
public final class Frequency {
	private final String word;
	private int frequency;
	
	public Frequency(String word) {
		this.word = word;
		this.frequency = 0;
	}
	
	public Frequency(String word, int frequency) {
		this.word = word;
		this.frequency = frequency;
	}
	
	public String getText() {
		return word;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void incrementFrequency() {
		frequency++;
	}
	
	@Override
	public String toString() {
		return word + ":" + frequency;
	}
}
