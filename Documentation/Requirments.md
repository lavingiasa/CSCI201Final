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
+Map of LA
	+Integrate a mapping API to display map to user
		+Zoom-in and Zoom-out features
	+Overlay nodes for practical movement of data
	+Display flow of traffic in the form of individual cars
		+Display cars in varying colors depending on latest speed
	+Keep map up-to-date as server provides data 
----

##Car Design:
+ List of cars and all pertinent data
	+ Car speed
	+ Car location
	+ Car destination

----

##Server Design:
+ It will pull and parse the JSON into a datastructure (most likely ArrayLists of Cars)
+ This will be done on a certain time interval
+ This data will be pulled by the database and the client to get the information on the cars that will be overlaid on the maps

----

##Client Interaction:
+ Map Design:
	+ Graphical Interface that displays map and car locations to user
		+ Displays flow of traffic as individual cars moving at their listed speeds
	+ Display fastest routes from specified starting and ending location
		+ Method to select starting and ending locations
			+ We will most likely overlay nodes (small ImageIcons) on the map that can be selected in order to do this
		+ Provide estimated time of journey at speed limit and current speed
+ Interface to display historical data from database
	+ Allow for determinations such as best time to travel from source to destination 
	+ This will be done in a graph fashion
		+ We will seperate the graph into increments of 15 minutes and make bar graphs
		+ Adjacent bars will be of different colours to allow the user to distinguish them easily
		+ On top of the bars will be a number to show a time that the user can read
		+ This whole contraption will scale in size depending on the data to make sure that it will not be cut off

----
