package com.littleGame.model;

import javax.persistence.*;


@Entity
@Table(name="score")
public class Score {
	 @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    @Column(name="id")
	    protected int id;
	 
	    @Column(name="score")
	    protected int score;
	 
	    @Column(name="name")
	    protected String name;
	    
	    @Column(name="heroe")
	    protected String heroe;
	    
	    @Override
	    public String toString() {
	      return name+'-'+heroe+'-'+id+'_'+score;
	    }
	    
	    public Score() {
	    }

	    public Score(int score, String heroe, String name) {
	        super();
	        this.score = score;
	        this.heroe = heroe;
	        this.name = name;
	    }
	    

	    public int getScore() {
	        return score;
	    }
	    public void setScore(int score) {
	        this.score = score;
	    }
	    public String getHeroe() {
	        return heroe;
	    }
	    public void setType(String heroe) {
	        this.heroe = heroe;
	    }
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    
}