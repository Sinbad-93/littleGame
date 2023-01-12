<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Little Game</title>
<link rel="stylesheet" type="text/css" href="style.css"/>
<style type="text/css"> 
<%@ include file="style.css"%>
</style> 
</head>
<body>
	<h1>
	Mini RPG
	<c:if test="${startFight.equals(true)}" >
  - En Combat !
</c:if>
 </h1>
 

   <c:choose>
   <c:when test="${noMonster.equals(true)}">
	<h2> Vous vous mettez en route... </h2>
	</c:when>
	
	<c:when test="${choosedCharacter.life > 0}">
	<h2> Un ennemi vous attaque ! </h2>
	</c:when>
	
		<c:otherwise>
			
			<a
			href="main"><button>
			<h2> Retour Accueil - Tableau des scores </h2></button></a>
			
			</c:otherwise>
			   </c:choose>
			   
			   <div>Score actuel : ${choosedCharacter.xp}</div>
			   <br>
<div class="character-cards">
 <c:choose>
				<c:when test="${choosedCharacter.life < 1}">
				<div class="character-card" > Vous êtes mort &#128128;  </div>
				</c:when>
               	<c:otherwise>
                <div class="character-card" >
                    
                     <!--<div><c:out value="${character.id}" /></div>-->
                     
                     <div><c:out value="${choosedCharacter.name}" />
                     
                     </div>
                    <div>${choosedCharacter.type} / niv. ${choosedCharacter.lvl}</div> 
                    <div>
                    Point de vie <span> &#128156;</span>
                    <span id="myPDV"><c:out value="${choosedCharacter.life}" /> </span>
                    
                    <!--<img alt="coeur" src="<%=request.getContextPath()%>/image/2.jpeg"/>-->
                    <!--<img alt="coeur" src="images/userImg.png"/>-->
                    
                    </div>
                    <div id="myAgility"> Agilité <span> &#127993;</span><c:out value="${choosedCharacter.agility}" /></div>
                    <div id="myStrength">Force <span> &#128170;</span><c:out value="${choosedCharacter.strength}" /></div>
                    <div id="myWit">Esprit <span> &#129412;</span><c:out value="${choosedCharacter.wit}" /></div>   
                    <br>(Total Compétences :  ${choosedCharacter.agility+choosedCharacter.strength+choosedCharacter.wit}) <br>
                    -----------------------
                    <br>                     
               
                </div>
                </c:otherwise>
                 </c:choose>
                 
                 
                 
                 <div class="attackInfos">
<c:if test="${startFight.equals(true) && ((escapeStatus.equals('None')) || (escapeStatus.equals('failed'))) && choosedCharacter.life > 0}">

<button
id="diceButton"
 onclick="dés()"
>Attaquer !</button>
</c:if>


<div id="resultDice">&#127922;</div>
<br>
<div id="battleEvent"></div>
 
</div>


                 <c:choose>
                <c:when test="${enemy.life < 1}">
                <div class="character-card" > L'enemi a été terassé ! &#9876;&#65039;  </div>
                </c:when>
                 <c:when test="${noMonster.equals(true)}">
                <div class="character-card" > Vous êtes hors de danger  </div>
                </c:when>
                
               	<c:otherwise>
                <div class="character-card enemy" >
                    
                     <!--<div><c:out value="${character.id}" /></div>-->
                     
                     <div><c:out value="${enemy.name}" />
                     
                     </div>
                    <div>${enemy.type} / niv. ${enemy.lvl}</div> 
                    <div>
                    Point de vie <span> &#128156;</span>
                    <span id="enemyPDV"> <c:out value="${enemy.life}" /></span>
                    
                    <!--<img alt="coeur" src="<%=request.getContextPath()%>/image/2.jpeg"/>-->
                    <!--<img alt="coeur" src="images/userImg.png"/>-->
                    
                    </div>
                    <div> Agilité <span> &#127993;</span><c:out value="${enemy.agility}" /></div>
                    <div>Force <span> &#128170;</span><c:out value="${enemy.strength}" /></div>
                    <div>Esprit <span> &#129412;</span><c:out value="${enemy.wit}" /></div>   
                    <br>(Total Compétences : ${enemy.agility+enemy.strength+enemy.wit}) <br>
                    -----------------------
                    <br>
                                        
               
                </div></c:otherwise>
                 </c:choose>
                
               
     
</div>
 <div
 class="combatEvents"> <!-- <a href="fight">Combattre</a> --> 
                <c:choose>
                <c:when test="${choosedCharacter.life < 1}">
        <div> Partie terminée</div>
    </c:when>
    <c:when test="${startFight.equals(true) && ((escapeStatus.equals('None')) || (escapeStatus.equals('failed')))}">
        <div> Combat lancé</div>
    </c:when>
     <c:when test="${startFight.equals(false) && noMonster.equals(true)}">
        <div> 
         <c:choose>
         <c:when test="${enemy.life < 1}">
          <a href="battle?id=<c:out value='${choosedCharacter.id}' />&action=meet&idEnemy=<c:out value='0' />">Nouvelle rencontre</a></c:when>
        <c:otherwise>
        <a href="battle?id=<c:out value='${choosedCharacter.id}' />&action=meet&idEnemy=<c:out value='${enemy.id}' />">Nouvelle rencontre</a>
        </c:otherwise>
          </c:choose>
         </div>
    </c:when>
    <c:otherwise>
    <div> <a href="battle?id=<c:out value='${choosedCharacter.id}' />&action=baston&idEnemy=<c:out value='${enemy.id}' />">Combattre</a></div>
        
    </c:otherwise>
</c:choose> <br>
                
                <div class="infosDiv">
                <div>
                <c:choose>
    <c:when test="${escapeStatus.equals('success')}">
        <div> LA FUITE A REUSSIE</div>
    </c:when>
    <c:when test="${escapeStatus.equals('failed')}">
   <div> VOUS NE POUVEZ PAS FUIR</div>
    </c:when>
</c:choose></div>
                <c:if test="${noMonster.equals(false) && choosedCharacter.life > 0}">
                <a href="battle?id=<c:out value='${choosedCharacter.id}' />&action=escape&idEnemy=<c:out value='${enemy.id}' />&escapeChance=<c:out value='${randEscape - ((choosedCharacter.initialLife - choosedCharacter.life)*10)}'/>">
                
                <c:set var="chanceToEscape" value="${randEscape - ((choosedCharacter.initialLife - choosedCharacter.life)*10)}" scope="session" />
                Fuir (${chanceToEscape} % de chance)
               
                </a>
                <span>[-1 &#128156; PV en cas d'echec] </span>
                <span>[-10% de chance de fuir supplémentaire par PV manquant ]</span>
                <span>[malus de fuite actuel : - ${(choosedCharacter.initialLife - choosedCharacter.life)*10} %]</span>
                
                </c:if>
                </div>
                </div>
                <br>
<div>
 Joueur :
 <c:choose>
 <c:when test="${pseudo != null}">
 ${pseudo}
 </c:when>
 <c:otherwise>

 <form method="post" action="pseudonym">
 <input type=text name="pseudo" id="pseudo"/>
 <input style="display : none" type=text name="id" value="${choosedCharacter.id}"/>
 <input style="display : none" type=text name="idEnemy" value="${enemy.id}"/>
 <input style="display : none" type=text name="startFightBool" value="${startFight ? 1:0}" />

 <input type="submit" />
 <div>Enregistrer votre pseudonyme (avant la fin du jeu) pour rentrer dans le podium des scores !</div>
    
 </form>
 
 </c:otherwise>
 </c:choose>
 
  </div>


</body>

<script type="text/javascript">
    function randomBattle () {
    	alert('mon texte');
      return false;
    }
    function alerter (value) {
    	alert(value);
      return false;
    }
    function duel(bool){
    	if(bool ===true){
    		document.getElementById("battleEvent").innerHTML="Duel remporté ! L'enemi perd 1pv";
    	}else {
    		document.getElementById("battleEvent").innerHTML="Duel perdu ! Vous perdez 1pv";
    	}
    }
    function dés() {
    	//alert('${choosedCharacter.name}');
    	document.getElementById("diceButton").disabled = true;
    	document.getElementById("resultDice").innerHTML="&#128165; le duel commence ! ...";
    	let href;
    	setTimeout(() => {
    		var random = Math.floor(Math.random() * 3);
        	switch (random) {
        	  case 0:
        		  document.getElementById("resultDice").innerHTML="Type de duel : &#127993";
        		  //alert(parseInt('${enemy.agility}');)
        		 if(
        				 (parseInt('${enemy.agility}')) > (parseInt('${choosedCharacter.agility}')) 
        				 ){
        			  document.getElementById("myAgility").style.backgroundColor = "red";
        			  duel(false);
        			  var pdv = (parseInt('${choosedCharacter.life}'))-1;
        			  document.getElementById("myPDV").innerHTML=pdv;
        			  href = "battle?id="+"${choosedCharacter.id}"+"&duel=false&idEnemy="+"${enemy.id}";
        			  
        		  } 
        		 else {
        			  document.getElementById("myAgility").style.backgroundColor = "greenyellow";
        			  duel(true);
        			  var pdv = (parseInt('${enemy.life}'))-1;
                	  document.getElementById("enemyPDV").innerHTML=pdv;
                	  href = "battle?id="+"${choosedCharacter.id}"+"&duel=true&idEnemy="+"${enemy.id}";
        		  };
        	    break;
        	  case 1:
        		  document.getElementById("resultDice").innerHTML="Type de duel : &#128170;";
        		  if((parseInt('${enemy.strength}')) > (parseInt('${choosedCharacter.strength}')) ){
        			  document.getElementById("myStrength").style.backgroundColor = "red";
        			  duel(false);
        			  var pdv = (parseInt('${choosedCharacter.life}'))-1;
                	  document.getElementById("myPDV").innerHTML=pdv;
                	  href = "battle?id="+"${choosedCharacter.id}"+"&duel=false&idEnemy="+"${enemy.id}";
        		  } else {
        			  document.getElementById("myStrength").style.backgroundColor = "greenyellow";
        			  duel(true);
        			  var pdv = (parseInt('${enemy.life}'))-1;
                	  document.getElementById("enemyPDV").innerHTML=pdv;
                	  href = "battle?id="+"${choosedCharacter.id}"+"&duel=true&idEnemy="+"${enemy.id}";
        		  };
          	    break;
        	  case 2:
        		  document.getElementById("resultDice").innerHTML="Type de duel : &#129412;";
        		  if((parseInt('${enemy.wit}')) > (parseInt('${choosedCharacter.wit}')) ){
        			  document.getElementById("myWit").style.backgroundColor = "red";
        			  duel(false);
        			  var pdv = (parseInt('${choosedCharacter.life}'))-1;
                	  document.getElementById("myPDV").innerHTML=pdv;
                	  href = "battle?id="+"${choosedCharacter.id}"+"&duel=false&idEnemy="+"${enemy.id}";
        			  
        		  } else {
        			  document.getElementById("myWit").style.backgroundColor = "greenyellow";
        			  duel(true);
        			  var pdv = (parseInt('${enemy.life}'))-1;
                	  document.getElementById("enemyPDV").innerHTML=pdv;
                	  href = "battle?id="+"${choosedCharacter.id}"+"&duel=true&idEnemy="+"${enemy.id}";
        		  };
        	    break;
        	  default:
        		break;
        	};
        	
        	
    		}, 1000);
    	setTimeout( ()=>{
    		window.location=href; }, 3500
    	);
    	
    	
    	
      return false;
    }
    
  </script>
</html>