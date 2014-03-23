package JSON;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Cars.Car;

public class JSONsParser extends Thread {
	private ArrayList<Car> cars;

	public JSONsParser(ArrayList<Car> cars) {
		this.cars = cars;
	}

	public void run() {
		// pullTheNewData(); //Reenable later
		parseTheData();
		try {
			sleep(180000); // 180000ms = 3 minutes
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void pullTheNewData() {
		URL website;
		try {
			website = new URL(
					"http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(
					"JSONs/currentData.json");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void parseTheData() // http://www.mkyong.com/java/json-simple-example-read-and-write-json/
	{
		JSONParser parser = new JSONParser();
		try {
			JSONArray arrayFromFile = (JSONArray) parser.parse(new FileReader("JSONs/initialTest.json"));

			for (int i = 0; i < arrayFromFile.size(); i++) 
			{
				JSONObject car = (JSONObject) arrayFromFile.get(i);
				boolean carExists = false;
				int indexOfExistingCar = -1;
				Long longID = (long) -1;
				int id = -1;
				double speed = -1;
				String direction = "";
				String ramp = "";
				String freeway = "";
				
				longID = (Long) car.get("id");
				speed = (Double) car.get("speed");
				direction = (String) car.get("direction");
				ramp = (String) car.get("on/off ramp");
				freeway = (String) car.get("freeway");
				id = Integer.parseInt(longID.toString());

				/*
				System.out.println(id);
				System.out.println(speed);
				System.out.println(direction);
				System.out.println(ramp);
				System.out.println(freeway + "\n");
				*/

				for(int i = 0; i < cars.size(); i++)
				{
					if(cars.get(i).getID() == id)
					{
						carExists = true;
						index = i;
						break;
					}
				}

				if(carExists)
				{
					Car currentCar = cars.get(indexOfExistingCar);
					currentCar.setSpeed(speed);
					currentCar.setDirection(direction);
					currentCar.setRamp(ramp);
					currentCar.setFreeway(freeway);
				}else{
					Car currentCar = new Car(id, speed, direction, ramp, freeway);
					System.out.println(currentCar.toString());
					cars.add(currentCar);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
	}
}
