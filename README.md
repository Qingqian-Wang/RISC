# RISC

- Overview: RISC is a real-time online strategy war game where players achieve victory by competing for each other's territories. In this project, we utilized the Jira platform for Scrum Agile development. The project involved designing a territory-conquering game using the MVC (Model-View-Controller) architecture. The front end was implemented using JavaFX, while SQLite was employed for data storage.


<div align="center">
      <img src="/image/screenshot.png" alt="Alt text" width="500"/>    
</div>


• In the game, players employed soldiers to attack the territories of other players. The gameplay included basic interactions 
like soldier movement and upgrades. Additionally, players could engage in optional activities such as upgrading 
technology, training spies, and forming alliances.
• After the game concluded, scores were calculated and stored in a database (db) file. Users could log back in using their 
account credentials






## technique stack

| frontEnd | backend | Database | 
| -------- | -------| ---------- |
| JavaFx | Java | SQLite |


## Design
UML
In this project, we used MVC to construct our design. Here is the UML diagram:
![UML](image/651UML.png)

## Game Flow

### Login game and select room
- Utilized sockets for communication between clients and servers. Players could request game room creation from the 
server and await the entry of other players.

- login
the user's info will be stored in the database
<div align="center">
    <img src="/image/login.png" alt="Alt text" width="500"/>
</div>

- select a room to join
The project can have two rooms simultaneously, and players choose one of them to join.
<div align="center">
<img src="/image/selectroom.png" alt="Alt text" width="500"/>
</div>

### Assign your soldiers to the territory
For each player, he has 50 soldiers in total. The player can assign these soldiers to his initial territory.
<div align="center">
<img src="/image/assignmentsoldiers.png" alt="Alt text" width="500"/>
</div>


### Select an operation
In a single round, the player can move or upgrade his soldiers. He can also attack the enemy's territory or cloak his territory's recourse from the enemy.
<div align="center">
<img src="/image/operationpage.png" alt="Alt text" width="500"/>
</div>


