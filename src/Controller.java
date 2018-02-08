import javax.xml.parsers.*;

import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

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
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Controller extends Application{

	Calendar d;
	public static String station; 
	public static String stationNormal;
	public static String url = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=perse&NumMins=30";
	public static String urlStart = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=";
	public static String numberOfMinutes = "90";
	public static String urlEnd = "&NumMins=" + numberOfMinutes;
	public static String stationGet = "http://api.irishrail.ie/realtime/realtime.asmx/getAllStationsXML";
	public static Map<String,String> stationsMap = new HashMap<String,String>();
	public static ArrayList<String> capitalStations = new ArrayList<String>();
	public static Thread t;
	public static Thread t2;
	public static boolean exit;
	static Scanner input = new Scanner(System.in);
	public static ListView<String> listView = new ListView<String>();
	public static ComboBox<String> stationPicker;

	@Override
	public void start(Stage primaryStage) {
		
		Group root = new Group();
		Scene scene = new Scene(root, 400, 750);
		station = "Dublin Pearse";
		
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
			//System.out.println("Error, please check network connection");
			System.exit(0);
		}
		
		

		stationPicker = new ComboBox<String>(FXCollections.observableList(capitalStations));
		
		listView.setPrefSize( 400, 700 );
		
		stationPicker.setPrefSize(400,50);
		stationPicker.getSelectionModel().select(capitalStations.indexOf(station));

		stationPicker.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String prev, String t1) {
	          url = urlStart + Controller.stationsMap.get(t1.toLowerCase()) + urlEnd;
	          station = t1;
	          t.interrupt();
	        }    
   		});

		listView.setTranslateY(50);
		root.getChildren().add(listView);
		root.getChildren().add(stationPicker);
		primaryStage.setTitle("LiveDART");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
        primaryStage.show();

		//System.out.println("Which station would you like information on?");
		//System.out.println("For a list of stations type 'stations'");
		
		/*if(input.hasNext()){
			stationNormal = input.nextLine();
			station = stationNormal.toLowerCase();
		}*/

		

		try{
				if(station.equals("stations")){
					dart.printInput("Stations:\n");
					for(String value : capitalStations){
						dart.printInput(value + "\n");
					}
					//System.out.println("Which station would you like information on?");
					if(input.hasNext()){
						station = input.nextLine().toLowerCase();
					}
				}
				
				if(stationsMap.containsKey(station)){
					url = urlStart + stationsMap.get(station) + urlEnd;

					
				}else{
					//System.out.println("Did not recognise station entered, defaulting to Dublin Pearse");
					station = "Dublin Pearse";
				}
			}catch(Exception e){
				//System.out.println("No station specified, defaulting to Dublin Pearse");
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