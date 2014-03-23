# Final Project Design Document
Design document for out traffic simulator final project.
The Team:
----
+ Samir Lavingia
+ Jeffrey Kang
+ Aidan Blant
+ Ian Whelan
+ Alexei Naumann

##Premise:
----
The goal is to showcase all that we have learned over the course of CSCI 201. This includes, but is not limited to: Multi-threading, Java based applications, programs using the Swing Javax library, and more.

More specifically this application will work as a traffic similator that is constantly pulling live data from the server given to us.
##Classes:
----
+ ###Car:
	+ This class will represent the cars on the map. Each car will be its own thread
	+ #####Parents:
		+ Runnable: This is because each car will be its own thread and due to Javas rules about multiplay inheritence we must impliment this, not inherit it.
		+ JComponent(?): Each car will be able to draw itself on the map, so it must have the pain component method overwritten. To let cars draw themselves we need to inherit this class //TO-DO: Double check this.
	+ #####Functions:
		+ PaintComponent(Graphics g)
			+ Using the location and speed the cars will draw themselves on that map with the graphics instance that is passed in
		+ Getters/Setters for the data
		+ UpdateLocation:
			+ Using the location, direction, and speed of the car this method will be called to reupdate the location of the car
		+ Constructor:
			+ Given the data from the JSON a new car will be created 
	+ #####Data:
		+ int ID: This is the identification number of the car
		+ double Speed: This is the speed that the car is currenly going
		+ String Direction: This will be the direction the car is heading:
			+ Acceptable Strings:
				+ West
				+ North
				+ South
				+ East
			+ If the String that is given will not work for the highway (trying to go west on a north-south freeway) it will be remapped as given here:
				+ North -> West
				+ West -> North
				+ South -> East
				+ East -> South
		+ String Ramp: This is the name of the ramp that the car got on
		+ String Freeway: This is the name of the freeway the car is on
		+ double XLocation: This is the x location of the car
			+ I believe that we will make this work easist for the map. This would mean latitude in degrees in this case 
		+ double YLocation: This is the y location of the car
			+ I believe that we will make this work easist for the map. This would mean longitude in degrees in this case 

+ ###JSONParser:
	+ This class will pull and parse the JSON. This will be done at a certain time interval (set for three minutes at this moment)
	+ #####Parents:
		+ Thread: Because this class will run itself every X time interval, it seemed the best idea to make it its own thread that will put itself to sleep and spin back up everytime it is needed (and the OS slices it in)
	+ #####Functions:
		+ run:
			+ This method just pulls and parses the data then puts itself to sleep for a certain time interval
		+ pullNewData:
			+ This method will pull the new JSON file from the server and save it locally
		+ parseTheData:
			+ This method will use the new pulled JSON to update and add new cars. If the car is already in the system then it will just update, else it will create a new car
	+ #####Data:
		+ ArrayList <Car> cars: This will be the list of cars that are parsed out of the JSON
+ ###MapPanel:
	+ We did not write this class. It displays the map using data from Open Street Maps
+ ###MapGUI:
	+ This will be the main GUI of the application where everything will happen
	+ #####Parents:
	+ #####Data:
	+ #####Functions:

##Flow of The Application: