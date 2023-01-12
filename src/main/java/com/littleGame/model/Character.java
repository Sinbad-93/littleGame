package com.littleGame.model;

import javax.persistence.*;


@Entity
@Table(name="characters")
public class Character {
	 @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    @Column(name="id")
	    protected int id;
	 
	 	@Column(name="pseudonym")
	    protected String pseudonym;
	 
	    @Column(name="type")
	    protected String type;
	 
	    @Column(name="name")
	    protected String name;
	    
	    @Column(name="lvl")
	    protected int lvl;
	    
	    @Column(name="xp")
	    protected int xp;
	    
	    @Column(name="life")
	    protected int life;
	    
	    @Column(name="initialLife")
	    protected int initialLife;
	    
	    @Column(name="agility")
	    protected int agility;
	    
	    @Column(name="strength")
	    protected int strength;
	    
	    @Column(name="wit")
	    protected int wit;
	    
	    @Override
	    public String toString() {
	      return name+'-'+type+'-'+id+'_'+life+'_'+agility+'_'+strength+'_'+wit+'_';
	    }
	    
	    public Character() {
	    }
	  
	 
	    public Character(String type,String name) {
	        super();
	        this.type = type;
	        this.name = name;
	    }

	    public Character(int id, String type, String name) {
	        super();
	        this.id = id;
	        this.type = type;
	        this.name = name;
	    }
	    
	    public Character(String type, String name,int lvl, int life, int agility, int strength, int wit) {
	        super();
	        this.type = type;
	        this.name = name;
	        this.lvl = lvl;
	        this.life = life;
	        this.agility = agility;
	        this.strength = strength;
	        this.wit = wit;
	    }
	    
	    public Character(int asp){
	    	
	    	
	        	//calcul pour la génération d'un random Monstre, à mettre dans une fonction
	    	
				// par la suite asp pourra varier de +1 ou -1
	        	/*int randomInteger = (int)(Math.random() * 2) + 1;
	        	int aspRand = 0;
	        	if(randomInteger == 1) {
	        		aspRand = 1;
	        	}else if (randomInteger == 2){
	        		aspRand = -1;
	        	}
	        	int aspects = asp+aspRand;*/
				int aspects = asp;
				int monsterPv = 5;
				int totalAbilities = aspects-monsterPv;
				
				
				// define the range
		        int max = totalAbilities-2;
		        int min = 1;
		        int range = max - min + 1;
		     // int agility = random max totalAbilities -2;
				int rand1 = (int)(Math.random() * range) + min;
				// int strength = random max totalAbilities-2-agility;
				max = totalAbilities-1-rand1;
				range = max - min + 1;
				int rand2 = (int)(Math.random() * range) + min;
				// int wit = totalAbilities-agility-strength;
				int rand3 = totalAbilities-rand1-rand2;
				int totalRand = rand1+rand2+rand3;
				
				//check if totalAbilities = agility+strength+wit;
				/*if(totalAbilities == totalRand) {
					System.out.println(totalAbilities);
					System.out.println(totalRand);
					System.out.println("*******0000000000**********le total correspond tout à fait");
				}else {
					System.out.println(totalAbilities);
					System.out.println(totalRand);
					System.out.println("*********000000000***********le total ne correspond pas");
	        }*/
				
			     this.type = "Monstre";
			     this.name = "Ennemi";
			     this.lvl = 1;
			     this.life = monsterPv;
			     this.agility = rand1;
			     this.strength = rand2;
			     this.wit = rand3;
			
	    }
	    
	    public void damage(int damage) {
	    	this.life -= damage;
	    }
	    

	    public int getId() {
	        return id;
	    }
	    public void setId(int id) {
	        this.id = id;
	    }
	    public String getType() {
	        return type;
	    }
	    public void setType(String type) {
	        this.type = type;
	    }
	    public String getPseudonym() {
	        return pseudonym;
	    }
	    public void setPseudonym(String pseudonym) {
	        this.pseudonym = pseudonym;
	    }
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    public int getLvl() {
	        return lvl;
	    }
	    public void setLvl(int lvl) {
	        this.lvl = lvl;
	    } 
	    public int getXp() {
	        return xp;
	    }
	    public void setXp(int xp) {
	        this.xp = xp;
	    }
	    public int getInitialLife() {
	        return initialLife;
	    }
	    public void setInitialLife(int initialLife) {
	        this.initialLife = initialLife;
	    }
	    public int getLife() {
	        return life;
	    }
	    public void setLife(int life) {
	        this.life = life;
	    }
	    public int getAgility() {
	        return agility;
	    }
	    public void setAgility(int agility) {
	        this.agility = agility;
	    }
	    public int getStrength() {
	        return strength;
	    }
	    public void setStrength(int strength) {
	        this.strength = strength;
	    }
	    public int getWit() {
	        return wit;
	    }
	    public void setWit(int wit) {
	        this.wit = wit;
	    }
}
