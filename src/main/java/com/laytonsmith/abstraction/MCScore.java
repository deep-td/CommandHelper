package com.laytonsmith.abstraction;

/**
 * 
 * @author jb_aero
 */
public interface MCScore {
	public MCObjective getObjective();
	public int getScore();
	public MCScoreboard getScoreboard();
	public void setScore(int score);
}
