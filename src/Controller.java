import javax.xml.parsers.*;
import javafx.application.Application;
import javafx.stage.Stage;
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


public class Controller extends Application{

	Calendar d;
	public static String station; 
	public static String stationNormal;
	public static String url = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=perse&NumMins=30";
	public static String urlStart = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=";
	public static String urlEnd = "&NumMins=30";
	public static String stationGet = "http://api.irishrail.ie/realtime/realtime.asmx/getAllStationsXML";
	public static Map<String,String> stationsMap = new HashMap<String,String>();
	public static ArrayList<String> capitalStations = new ArrayList<String>();
	public static Thread t;
	public static Thread t2;
	public static boolean exit;
	static Scanner input = new Scanner(System.in);

	@Override
	public void start(Stage primaryStage) {
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
					code = code.trim();
					String capitalDesc = e.getElementsByTagName("StationDesc").item(0).getTextContent();
					String desc = e.getElementsByTagName("StationDesc").item(0).getTextContent().toLowerCase();
					stationsMap.put(desc, code);
					capitalStations.add(capitalDesc);

				}
			}
		}catch(Exception e){
			System.out.println("Error, please check network connection");
			System.exit(0);
		}
		
		System.out.println("Which station would you like information on?");
		System.out.println("For a list of stations type 'stations'");
		
		if(input.hasNext()){
			stationNormal = input.nextLine();
			station = stationNormal.toLowerCase();
		}
		
		
		
		try{
				if(station.equals("stations")){
					dart.printInput("Stations:\n");
					for(String value : capitalStations){
						dart.printInput(value + "\n");
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

	public static void main(String[] args){
		launch(args);
	}


}