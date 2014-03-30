#Technical Requirements
----
##Team:
+ Samir Lavingia
+ Alexei Naumann
+ Jeff Kang
+ Ian Whelean
+ Aidan Blant

----
##Database Design:
+ Data that will be put into the database:
	+ All the data coming from the JSON files in terms of car speeds, locations, etc.
	+ All messages received from server stored for fast retrieval
	+ Must implement SQL, and must connect to it using SQL statements

----
##Data Export:
+ It should take the important data from the database and export it
+ We want it to export to a comma delimited file that can be opened in a nice format in excel
+ Will export: 
	+ Car Data:
		+ How many cars were on the road over the course of execution
		+ Most traveled highways
	+ Travel Times:
		+ This will be for all the intersections that were used during the life of the program to all the other ones
		+ Specifically:
			+ Travel time at certain times of the day
		  + Average travel times
  		+ Slowest highways on average

----

##Map Design:

----

##Car Design:
+ List of cars and all pertinent data
	+ Car speed
	+ Car location
	+ Car destination

----

##Server Design:


----

##Client Interaction:
+ Graphical Interface that displays map and car locations to user
	+ Displays flow of traffic as individual cars moving at their listed speeds
	+ Display fastest routes from specified starting and ending location
		+ Method to select starting and ending locations
		+ Provide estimated time of journey at speed limit and current speed
+ Interface to display historical data from database
	+ Allow for determinations such as best time to travel from source to destination 

----
