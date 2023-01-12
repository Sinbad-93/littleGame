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
	/*css code 
	.character-card {
	display : flex;
	flex-direction : column;
	}
	
	.character-cards {
	display : flex;
	flex-direction : row;
	}*/
</style> 
</head>
<body>
	<h1> MINI RPG</h1>
	
	<h2> Choisissez votre aventurier :</h2>
<div class="character-cards">
<c:forEach var="character" begin="0" end="2" items="${listCharacter}">
                <div class="character-card">
                    
                     <!--<div><c:out value="${character.id}" /></div>-->
                     
                     <div><c:out value="${character.name}" />
                     
                     </div>
                    <div>${character.type} / niv.1</div> 
                    <div>
                    Point de vie <span> &#128156;</span><c:out value="${character.life}" /> 
                    
                    <!--<img alt="coeur" src="<%=request.getContextPath()%>/image/2.jpeg"/>-->
                    <!--<img alt="coeur" src="images/userImg.png"/>-->
                    
                    </div>
                    <div> Agilité <span> &#127993;</span><c:out value="${character.agility}" /></div>
                    <div>Force <span> &#128170;</span><c:out value="${character.strength}" /></div>
                    <div>Esprit <span> &#129412;</span><c:out value="${character.wit}" /></div>   
                    <br>(Total Aspects : 15) <br>
                    -----------------------
                    <br>
                     <a href="battle?id=<c:out value='${character.id}' />">Let's Go !</a>                       
               
                </div>
            </c:forEach>
</div>
<div>
<h3>Palmarès des scores :</h3>
    <p>Seul les scores supérieurs à 4 sont enregistrés</p>
    
<c:forEach var="score" items="${listScore}">
                <div class="character-card">
                      
                     <div>Joueur : <c:out value="${score.name}" /> </div>
                    <div>Avec : ${score.heroe} </div>  
                    <div> Nombre de monstres vaincus : <c:out value="${score.score}" /></div>
                    
                                           
               
                </div>
            </c:forEach>
        
</div>

</body>


</html>