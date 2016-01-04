package com.obs.brs.controller;

public class Score  implements Comparable<Score>{
    private long id;
	private float score;
	

    public Score() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Score(long id, float score) {
		super();
		this.id = id;
		this.score = score;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
    public int compareTo(Score o) {
        return this.score > o.score ? 1 : (this.score < o.score ? -1 : 0);
    }
  
    /*
     * implementing toString method to print orderId of Order
     */
    @Override
    public String toString(){
        return String.valueOf(score);
    }

}