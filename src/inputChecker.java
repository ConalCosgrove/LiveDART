import java.util.Scanner;

public class inputChecker implements Runnable{

	private Scanner scan = new Scanner(System.in);
	
	static String url = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=perse&NumMins=30";
	static String urlStart = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByCodeXML_WithNumMins?StationCode=";
	static String urlEnd = "&NumMins=30";
	private boolean quit;
	public inputChecker(){
		quit = false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!quit){
			
			if(scan.hasNext()){
				try{
					
					String statin = scan.nextLine();
					String stat = statin.toLowerCase();
					if(Controller.stationsMap.containsKey(stat)){
						url = urlStart + Controller.stationsMap.get(stat) + urlEnd;
						//System.out.println(stat);
						Controller.station = statin;
						Controller.url = url;
						
						Controller.t.interrupt();
						
						
					}else if(stat.equals("exit")){
						dart.quit();
						quit = true;
						Controller.t.interrupt();
					}else if(stat.equals("refresh")){
						Controller.t.interrupt();
					}
					else{
						System.out.println("Did not recognise station entered");
						
					}
				}catch(Exception e){
					System.out.println("No station specified");
					System.out.print(e);
					
				}
			}
		}
	}

}
