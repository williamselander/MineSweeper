## Minesweeper
Ett klassiskt minesweeper spel inspirerat av windows 95. Spelet har tre nivåer med olika svårighetsgrader,
spelaren kan även välja att köra med eller utan en timer. 

## Installation
Kräver JDK 21 och Apache Maven. 
Öppna en terminal i projektmappen och kör:

````shell
mvn javafx:run
````

Maven hämtar automatiskt de beroenden som behövs för att starta spelet. 

## Hur kör man spelet?
För att köra spelet så behöver beroenden vara hämtade, starta sedan spelet i terminalen genom `mvn javafx:run` eller
genom att starta `main` i din IDE. 

I spelet kan användaren sedan välja svårighetsgrad och om timer eller inte önskas, detta görs i settings. 
För att börja leta minor så klickar spelaren på spelplanen och öppnar sin första ruta genom vänsterklick, om rutan inte 
har minor runt sig så öppnas dessa automatiskt och om den har minor runt sig visar hur många. För att flagga en ruta
som du tror har en mina använder du högerklick och högerklickar igen för att avflagga rutan. Du kan se hur många minor 
som finns kvar genom en räknare, denna uppdateras när du flaggar en ruta. För att vinna behövs alla rutor som ej har 
en mina vara öppnad, om tider har gått ut eller att en mina har öppnats så förloras spelet. För att starta om spelet 
trycker du antingen på den gula smileyn eller går in i inställningar och trycker på "nytt spel". 