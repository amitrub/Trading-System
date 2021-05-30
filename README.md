# Trading-System

System operating instructions: 

Step 1: Choosing Init State
- Go to src/main/resources/initialization_System
- If you want the Shachaf's init state on our system --> choose "externalState": true
- If you want fresh system (empty) --> choose "externalState": false

Step 2: Choosing DB
- The default state is an remote DB --> "local": false
- If you have a DB that already defined localy and you want to use it --> choose "local": true

Step 3: Running Trading System Server
- Go to src\main\java\TradingSystem\Server\TradingSystemApplication.java in IntelliJ, or some other IDE.
- Run!

Step 4: Running client
- Open your terminal
- Go to src\main\frontend folder
- Install npm packages by running: > npm install
- Start client: > npm start

Step 5: Multiple Clients
- Now the client is running on http://localhost:3000/
- If you want to run more clients, you should open more browsers and open http://localhost:3000/ many times as you want.

Have Fun! Buy a lot! 

Version_3 documention Google docs: 
https://docs.google.com/document/d/1DBNMJ8H6GPUFqfR4lDEbJKWOq0bYXd9tV0bi47D2YzE/edit?usp=sharing

Requierment with Rubin's colors Google docs: 
https://docs.google.com/document/d/131XJsclNhyCOoUzgnGSag1QCWTam7vJnLKT6YWoond4/edit?usp=sharing
