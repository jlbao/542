Welcome our company recommendation system.

If you want to use our system, make sure that you have installed followed environment:
Database: MySQL, MongoDB
Architecture: Hadoop
Language: Java

Prequisite:
1 Open the MySQL service
2 Open the MongoDB service

Steps to use the Mongo DB Map Reduce:

1 import the tag.sql into MySQL database.
2 run the DataImportingMain.java. // this is to convert the data from MySQL into MongoDB.
3 run the DataImportingMain.java  // this is to do the map reduce on mongo db
4 this is the last step, check the result.txt at the root of the project folder. You can see all the clustering result.
