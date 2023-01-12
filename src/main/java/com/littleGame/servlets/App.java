package com.littleGame.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.littleGame.dao.CharacterDao;
import com.littleGame.dao.ScoreDao;
import com.littleGame.model.Character;
import com.littleGame.model.Score;

@WebServlet("/")
public class App extends HttpServlet {
    private static final long serialVersionUID = 1L;
     private CharacterDao characterDao;
     private ScoreDao scoreDao;
     
     public void init() throws ServletException {
    	 characterDao = new CharacterDao();
    	 scoreDao = new ScoreDao();
     }
     
     public static boolean isEmpty(Collection<?> collection) {
         return collection == null || collection.isEmpty();
     }
     
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
     	    throws ServletException, IOException {
     	        doGet(request, response);
     	    }
     
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getServletPath();
        //System.out.println("888888888888888888action");
        //System.out.println(action);
        if(action != null && action.equals("/pseudonym")){
        	//System.out.println("PSEUDONYM");
        }
        

        try {
            switch (action) {
            case "/battle":
                battlePage(request, response);
                break;
            case "/pseudonym":
                recordPseudo(request, response);
                break;
                default:
                    listCharacter(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listCharacter(HttpServletRequest request, HttpServletResponse response)
    	    throws SQLException, IOException, ServletException {
    			//récupérer les personnages
    			List < Character > listCharacter = characterDao.getAllCharacter();
    			// vérifier si on a des données BDD
    	        boolean isEmpty = isEmpty(listCharacter);
    	        if (isEmpty) {     	
    	        	initializeData("Chararacter");
    	        }
    	        listCharacter = characterDao.getAllCharacter();
    	        
    	        
    			// création de faux score 
    	        List < Score > listScore = scoreDao.getAllScore();
    	        // vérifier si on a des données BDD
    	        boolean isEmpty2 = isEmpty(listScore);
    	        if (isEmpty2) {
    	        	initializeData("Score");
    	        }
    	        listScore = scoreDao.getAllScore();
    			
    	        
    	        request.setAttribute("listCharacter", listCharacter);
    	        request.setAttribute("listScore", listScore);
    	        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/start.jsp");
    	        dispatcher.forward(request, response);
    	    }
    
    private void battlePage(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException {
    			//System.out.println("BATTLE PAGE");
    			//*****RECUPERATION, INITIALISATION ET RAFRAICHISSEMENT DE VARIABLES*********
    	
    			//récupération de l'id
    			int id = Integer.parseInt(request.getParameter("id"));
    			//choix du carractère effectué
    			Character choosedCharacter = characterDao.getCharacter(id);
    			// POSSIBLEMENT NULL SELON LA REQUETE
    			String action = request.getParameter("action");
    			String duel = request.getParameter("duel");
    			
    			// set IF MONSTER
    			boolean noMonster = false;
    			// save Char and set if Monster or No;
    			noMonster = saveMyChar(action,duel,choosedCharacter);
    			
    			// set ENEMY ID
    			int idEnemy = 0;
    			String reqId = request.getParameter("idEnemy");
    			idEnemy = idEnemyRefresh(reqId);
    			
    			// initialisation
    			Character enemy = new Character();
    			
    			String isSomebodyDead = "No";
    			
    			String escapeStatus = "None";
    			// n'existe pas forcément
    			String probability = request.getParameter("escapeChance");
    			
    			// set IS IN FIGHT ?
    			boolean startFight = false;
    			startFight = startFightRefresh(action,duel);
    			
    			//*****FIN RECUPERATION, INITIALISATION ET RAFRAICHISSEMENT DE VARIABLES*********
    			
    			/*if(duel != null) {
				System.out.println("DUEL EN COURS ! ");
			}
			else if(action != null) {
				System.out.println("ACTION CHOISIE : "+ action);
			}*/
    			
    			
    			// ACTION DE FUITE
    			//function de fuite retournant un objet
    			Object [] resultObject = escapeTentative(
    						action,probability,choosedCharacter, escapeStatus,
    						idEnemy,isSomebodyDead,noMonster,startFight);
    			
    			// Refresh variables after no escape/success/failed
    			//{idEnemy, isSomebodyDead, escapeStatus,noMonster,startFight };
    			idEnemy = (int)resultObject[0];
    			isSomebodyDead = (String)resultObject[1];
    			escapeStatus = (String)resultObject[2];
    			noMonster = (boolean)resultObject[3];
    			startFight = (boolean)resultObject[4];				
    			
    			// création ou mise à jour de l'ennemi
    			 enemy = generateMonster(idEnemy);
    			
    			//System.out.println(duel.equals("true"));
    			
    			// duel, enlever un PV
    			String deadProbability = resolveDuel(duel, choosedCharacter, enemy);
    			// resultat pris en compte seulement si pas déjà un mort
    			if(isSomebodyDead.equals("No")) {
    				isSomebodyDead = deadProbability;
    			}
    			
    			// Réagir BDD si heroe ou monstre mort
    			Object [] resultDeath = deathConsequences(isSomebodyDead,choosedCharacter,enemy);
    			String resultStr = (String)resultDeath[0];
    			if(resultStr.equals("Enemi")) {
    				noMonster = (boolean)resultDeath[1];
    				startFight = (boolean)resultDeath[2];
    			}
    			
    			// récupérer le pseudo, s'il existe
    			String pseudo = choosedCharacter.getPseudonym();
    			
    			// créer une probabilité de fuite entre 40 & 100
    			int randEscape = getRandEscape();
    			
    			//FIN des changements d'etats - Envoyer les variables
    			request.setAttribute("choosedCharacter", choosedCharacter);
    			request.setAttribute("enemy", enemy);
    			request.setAttribute("startFight", startFight);
    			request.setAttribute("pseudo", pseudo);
    			request.setAttribute("randEscape", randEscape);
    			request.setAttribute("escapeStatus", escapeStatus);
    			request.setAttribute("noMonster", noMonster);
    			
    	        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/battle.jsp");
    	        dispatcher.forward(request, response);}
    
    
    
    
 // RECORD PSEUDO
    private void recordPseudo(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException {
    			//System.out.println("Record PSEUDO");
    			//System.out.println(request.getMethod());
    			/*Enumeration<String> headerNames = request.getHeaderNames();
    			while(headerNames.hasMoreElements()) {
    			  String headerName = headerNames.nextElement();
    			  //System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
    			}
    			Enumeration<String> params = request.getParameterNames(); 
    			while(params.hasMoreElements()){
    			 String paramName = params.nextElement();
    			 //System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
    			}*/
    			
    			String pseudo = request.getParameter("pseudo");
    			
    			int id = Integer.parseInt(request.getParameter("id"));
    			int idEnemy = Integer.parseInt(request.getParameter("idEnemy"));
    			int startFightBool = Integer.parseInt(request.getParameter("startFightBool"));
    			
    			Character choosedCharacter = characterDao.getCharacter(id);
    			Character enemy = characterDao.getCharacter(idEnemy);
    			
    			choosedCharacter.setPseudonym(pseudo);
				characterDao.updateCharacter(choosedCharacter);
				
				//System.out.println(action);
    			boolean startFight = false;
    			if(startFightBool > 0) {
    				//System.out.println("ok true");
        			startFight = true;
    			}
				
				request.setAttribute("choosedCharacter", choosedCharacter);
    			request.setAttribute("enemy", enemy);
    			request.setAttribute("startFight", startFight);
    			request.setAttribute("pseudo", pseudo);
    			
    			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/battle.jsp");
    			dispatcher.forward(request, response);
    	    }
    
    private Object[] escapeTentative(String action,String probability, Character choosedCharacter, String escapeStatus,
    		int idEnemy,String isSomebodyDead, boolean noMonster, boolean startFight) {
    	if(action != null && action.equals("escape") && probability != null) {
    	// lancer l'evenement probability
		// determiner les chances de fuir
        int maxDice = 100;
        int minDice = 0;
        int rangeDice = maxDice - minDice;
        double mathR = (double)Math.random();
		int randDice = (int)(mathR * rangeDice) + minDice;
		int heroeEscape = Integer.parseInt(probability);
		/*System.out.println("mathR");
		System.out.println(mathR);
		System.out.println("FUITE");
		System.out.println(randDice);
		System.out.println(heroeEscape);*/
		
		
		// fuite réussie
		if(heroeEscape > randDice) {
			// restauration des PV et nouveau monstre 
			
			//delete ancien monstre
			Character enemy = characterDao.getCharacter(idEnemy);
			characterDao.deleteCharacter(enemy.getId());
			
			// création d'un nouvel enemi --> à mettre dans une fonction
			enemy = new Character(15);
			characterDao.saveCharacter(enemy);
			// mettre a jour idEnemy car utilisé ensuite
			idEnemy = enemy.getId();
			
			// récupérer ses PV
			int restorePv=  choosedCharacter.getInitialLife();
			choosedCharacter.setLife(restorePv);
			characterDao.updateCharacter(choosedCharacter);
			
			escapeStatus = "success";
			noMonster = true;
			startFight = false;
			
			return new Object[] {idEnemy, isSomebodyDead, escapeStatus,noMonster,startFight };
		}
		//fuite échouée
		else {
		// Perte d'un PV --> à relier à une fonction par la suite
			//vérifier si n'est pas mort
			//enregistrer la modification de pv dans la bdd
			choosedCharacter.damage(1);
			int newLife = choosedCharacter.getLife();
		
			if(newLife == 0) {
				isSomebodyDead = "Heroe";
			}
			
			characterDao.updateCharacter(choosedCharacter);
			escapeStatus = "failed";
			
			return new Object[] {idEnemy, isSomebodyDead, escapeStatus,noMonster,startFight };
		}}
    	return new Object[] {idEnemy, isSomebodyDead, escapeStatus,noMonster,startFight };
		
    }
    
    private Character generateMonster(int idEnemy) {
    	Character enemy = new Character();
    	//génération d'un random Monstre
		if(idEnemy == 0) {
			//calcul pour la génération d'un random Monstre, à mettre dans une fonction
			
			//int aspects = 14 + heroe lvl +/- 1 ?;
			//int aspects = 14 + heroe lvl +/- 1 ?;
			// monster pv = 3 < mpv < 4+heroe lvl 
			int aspects = 15;
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
			if(totalAbilities == totalRand) {
				//System.out.println(totalAbilities);
				//System.out.println(totalRand);
				System.out.println("le total correspond tout à fait");
			}else {
				//System.out.println(totalAbilities);
				//System.out.println(totalRand);
				System.out.println("le total ne correspond pas");
				
			};
		//génération d'un random Monstre
			 enemy = new Character("Monstre","Ennemi",1,monsterPv,rand1,rand2,rand3);
			characterDao.saveCharacter(enemy);
			// récupération avec le nouvel id
			}
		
		//récupération d'un monstre existant
		else {
			 enemy = characterDao.getCharacter(idEnemy);
		}
		return enemy;
    }
    
    private String resolveDuel(String duel, Character choosedCharacter, Character enemy ) {
    	String isSomebodyDead = "No";
    	
    	if(duel != null && duel.equals("true")) {
			System.out.println("duel remporté");
			// enregistrer la modification de pv du monstre dans la bdd
			enemy.damage(1);
			int newLife = enemy.getLife();
			if(newLife == 0) {
				isSomebodyDead = "Enemi";
				int newXp = choosedCharacter.getXp() +1;
				choosedCharacter.setXp(newXp);
				characterDao.updateCharacter(choosedCharacter);
			}
			
			characterDao.updateCharacter(enemy);
		}
		else if(duel != null && duel.equals("false")) {
			System.out.println("duel perdu");
			
			//enregistrer la modification de pv dans la bdd
			choosedCharacter.damage(1);
			int newLife = choosedCharacter.getLife();
			if(newLife == 0) {
				isSomebodyDead = "Heroe";
			}
			characterDao.updateCharacter(choosedCharacter);
			
		}
    	return isSomebodyDead;
    }
    
    private Object[] deathConsequences(String isSomebodyDead, Character choosedCharacter, Character enemy) {
    	if(isSomebodyDead.equals("Heroe")) {
			characterDao.deleteCharacter(choosedCharacter.getId());
			// si le hero meurt le combat cesse et le monstre aussi est suppr bdd
			characterDao.deleteCharacter(enemy.getId());
			
			
			String pseudonym = choosedCharacter.getPseudonym();
			// enregistrer le score si on a un pseudo 
			if(pseudonym != null) {
			int finalScore=  choosedCharacter.getXp();
			Score heroeScore = new Score(finalScore,choosedCharacter.getName(),pseudonym);
			scoreDao.saveScore(heroeScore);}
			
			return new Object[] { "Heroe"};
		}
		else if(isSomebodyDead.equals("Enemi")) {
			characterDao.deleteCharacter(enemy.getId());
			
			boolean noMonster = true;
			boolean startFight = false;
			
			// récupérer ses PV
			int restorePv =  choosedCharacter.getInitialLife();
			choosedCharacter.setLife(restorePv);
			characterDao.updateCharacter(choosedCharacter);
			
			return new Object[] { "Enemi", noMonster,startFight };
			
			
		}
    	return new Object[]{ "No" };
    }
    
    private int getRandEscape() {
    	int maxEscape = 100;
        int minEscape = 40;
        int rangeEscape = maxEscape - minEscape;
		int randEscape = (int)(Math.random() * rangeEscape) + minEscape;
		return randEscape;
    }
    
    private boolean saveMyChar(String action, String duel, Character choosedCharacter) {
    	if(action == null && duel == null) {
			//sauvegarde du choix avec un nouvel id au premier passage
				// & switcher vers le nouveau carractère enregistré
				choosedCharacter.setInitialLife(choosedCharacter.getLife());
			characterDao.saveCharacter(choosedCharacter);
			boolean noMonster = true;
			return noMonster;
			}
    	return false;
    }
    
    private boolean startFightRefresh(String action, String duel) {
    	if((action != null && !action.equals("meet")) || duel != null) {
			//System.out.println("ok true");
			return true;
		}
    	return false;
    }
    
    private int idEnemyRefresh(String reqId) {
    	if(reqId != null) {
    		int idEnemy = Integer.parseInt(reqId);
    		return idEnemy;}
    		return 0;
    }
    
    private void initializeData(String action) {
    	if(action.equals("Character")) {
    		 System.out.println("The collection is empty; creating...");
	            //création d'une liste de personnage dans la bdd
	            Character mage = new Character("Mage","Gandalf",1,5,2,2,6);
 			characterDao.saveCharacter(mage);
 			Character guerrier = new Character("Guerrier","Obelix",1,6,3,5,1);
 			characterDao.saveCharacter(guerrier);
 			Character ninja = new Character("Ninja","Naruto",1,4,4,3,4);
 			characterDao.saveCharacter(ninja);
    	}
    	else if (action.equals("Score")) {
    		 System.out.println("The score results is empty; creating...");
	            //création d'une liste de personnage dans la bdd
	            Score example1 = new Score(14,"Gandalf","John");
 			scoreDao.saveScore(example1);
 			Score example2 = new Score(11,"Naruto","Jimmy");
 			scoreDao.saveScore(example2);
 			Score example3 = new Score(10,"Obelix","Jack");
 			scoreDao.saveScore(example3);
    	}
    }
    
    
    
    
    

    
    
    
    

}
