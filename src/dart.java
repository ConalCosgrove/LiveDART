import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.String;


public class dart implements Runnable{

	
	Calendar d;
	
	static String stationNormal = Controller.stationNormal;
	

	public static Map<String,String> stationsMap = Controller.stationsMap;
	public static ArrayList<String> capitalStations = Controller.capitalStations;
	public static Thread t = Controller.t;
	public static Thread t2 = Controller.t2;
	public static boolean exit = Controller.exit;





	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!exit){
			Document doc;
			Train[] trains;
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(new URL(Controller.url).openStream());
				doc.getDocumentElement().normalize();
				//System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
				NodeList nList = doc.getElementsByTagName("objStationData");
				trains = new Train[nList.getLength()];
				
				for(int i = 0; i < nList.getLength(); i++){
					Node n = nList.item(i);
					//System.out.println(n.getNodeName());
					if(n.getNodeType() == Node.ELEMENT_NODE){
						Element e = (Element) n;
						
						Train trai = new Train(e.getElementsByTagName("Destination").item(0).getTextContent(),e.getElementsByTagName("Duein").item(0).getTextContent(),e.getElementsByTagName("Exparrival").item(0).getTextContent());
						trains[i] = trai;
						//System.out.println(e.getElementsByTagName("Destination").item(0).getTextContent() + " " + e.getElementsByTagName("Duein").item(0).getTextContent());
						
						
					}
					
				}
				sortTrainsByDue(trains);
				
				System.out.println("\n**************************************************************\n");
				String s = "All Trains Serving " + Controller.station + " due in the next 30 mins \n\n";
				printInput(s);

				FileWriter out = new FileWriter("data.txt");
				for(int i = 0; i < trains.length; i++){
					String toPrint = "Train to " + trains[i].getDest() + " at " + trains[i].getExpArr() +"\n";
					out.write(toPrint);
					printInput(toPrint);

					toPrint = (trains[i].getDue() + " mins \n \n");
					out.write(toPrint);
					printInput(toPrint);
					
					
					
				}
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error, please check your network connection");
				System.out.println(e);
			} 
			
			d = Calendar.getInstance();
			
			System.out.println("Last updated: " + d.get(Calendar.HOUR_OF_DAY) + ":" + ((d.get(Calendar.MINUTE)<10)?("0" + d.get(Calendar.MINUTE)):(d.get(Calendar.MINUTE))) + ((d.get(Calendar.MINUTE) >= 12)?(" PM"):(" AM")) );
			try {
				Thread.sleep(120000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				if(!exit){
					System.out.println("Getting station info for " + Controller.station);
				}
			}
		}
		
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sortTrainsByDue(Train[] trains){
		
		for(int i = 0; i < trains.length; i++){
			int currentDue = trains[i].getDue();
			int smallest = i;
			
			for(int j = i+1; j < trains.length; j++){
				
				
				if(trains[j].getDue() < trains[smallest].getDue()){
					smallest = j;
				}
			}
			
			if(smallest != i){
				Train temp = trains[i];
				trains[i] = trains[smallest];
				//System.out.println("Swapped " + trains[i].getDest() + " with " + temp.getDest());
				trains[smallest] = temp;
			}
			
			
		}
	}
	
	public static void quit(){
		exit = true;
	}

	public static void printInput(String input){
		for(int i = 0; i < input.length(); i++){
			System.out.print(input.charAt(i));
			try{
				Thread.sleep(10);
			}catch(Exception e){

			}
		}
	}
}
