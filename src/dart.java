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
	static String station; 
	static String stationNormal;
	
	static String url = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=perse&NumMins=30";
	static String urlStart = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=";
	static String urlEnd = "&NumMins=30";
	static String stationGet = "http://api.irishrail.ie/realtime/realtime.asmx/getAllStationsXML";
	public static Map<String,String> stationsMap = new HashMap<String,String>();
	public static ArrayList<String> capitalStations = new ArrayList<String>();
	public static Thread t;
	public static Thread t2;
	public static boolean exit;
	static Scanner input = new Scanner(System.in);
	
	public static boolean stationChanged = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		exit = false;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(stationGet).openStream());
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName("objStation");
			
			for(int i = 0; i < list.getLength(); i++ ){
				Node n = list.item(i);
				if(n.getNodeType() == Node.ELEMENT_NODE){
					Element e = (Element) n;
					
					String code = e.getElementsByTagName("StationCode").item(0).getTextContent();
					String capitalDesc = e.getElementsByTagName("StationDesc").item(0).getTextContent();
					String desc = e.getElementsByTagName("StationDesc").item(0).getTextContent().toLowerCase();
					stationsMap.put(desc, code);
					capitalStations.add(capitalDesc);

				}
			}
		}catch(Exception e){
			System.out.println("Error, please check network connection");
		}
		
		System.out.println("Which station would you like information on?");
		System.out.println("For a list of stations type 'stations'");
		
		if(input.hasNext()){
			stationNormal = input.nextLine();
			station = stationNormal.toLowerCase();
		}
		
		
		
		try{
				if(station.equals("stations")){
					printInput("Stations:\n");
					for(String value : capitalStations){
						printInput(value + "\n");
					}
					System.out.println("Which station would you like information on?");
					if(input.hasNext()){
						station = input.nextLine().toLowerCase();
					}
				}
				
				if(stationsMap.containsKey(station)){
					url = urlStart + stationsMap.get(station) + urlEnd;
					
				}else{
					System.out.println("Did not recognise station entered, defaulting to Dublin Pearse");
					station = "Dublin Pearse";
				}
			}catch(Exception e){
				System.out.println("No station specified, defaulting to Dublin Pearse");
				station = "Dublin Pearse";
			}
		
		t = new Thread(new dart());
		t.start();
		
		t2 = new Thread(new inputChecker());
		t2.start();

}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!exit){
			Document doc;
			Train[] trains;
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(new URL(url).openStream());
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
				String s = "All Trains Serving " + stationNormal + " due in the next 30 mins \n\n";
				printInput(s);
				for(int i = 0; i < trains.length; i++){
					String toPrint = "Train to " + trains[i].getDest() + " at " + trains[i].getExpArr() +"\n";
					printInput(toPrint);
					toPrint = (trains[i].getDue() + " mins \n \n");
					printInput(toPrint);
					
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error, please check your network connection");
			} 
			
			d = Calendar.getInstance();
			
			System.out.println("Last updated: " + d.get(Calendar.HOUR_OF_DAY) + ":" + ((d.get(Calendar.MINUTE)<10)?("0" + d.get(Calendar.MINUTE)):(d.get(Calendar.MINUTE))) + ((d.get(Calendar.MINUTE) >= 12)?(" PM"):(" AM")) );
			try {
				Thread.sleep(120000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				if(!exit){
					System.out.println("Getting station info for " + station);
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
