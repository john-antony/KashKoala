# KashKaola
Repo for KashKaola project for CS411

KashKoala is a fictional company in the same vein as venmo, cashapp, etc.

The main functionality of the program allows users to create accounts, add friends, deposit, withdraw, and send money. 

This functionality was the base for my groups project, and was the first iterations we developed. 

After the basic KashKoala functionality was implemented, our next step was to add a stock tracker + portfolio for users,
to allow them to buy and sell stocks and check their live price changes.

To do this, we added a yahoo finance api integration through https://financequotes-api.com/. 
we used maven to add the library as a dependency for our project. 

The program also stores user login info, friendslist, and portfolio information in three separate files.

The majority of the code is included in the backbone file, and a custom tuple was created for the use of portfolio tracking.
The project uses Java JFrames and JPanels for the UI and functionality.
