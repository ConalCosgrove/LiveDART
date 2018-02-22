
import java.util.Calendar;
import java.util.Date;


public class Train {
	private String destination;
	private int dueIn;
	private String expArrival;
	
	
	public Train(String dest, String due,String expArr){
		this.destination = dest;
		this.expArrival = expArr;
		try{
			this.dueIn = Integer.parseInt(due);
		}catch(Exception e){
			this.dueIn = -1;
		}

		if(expArr.equals("00:00")){
			Calendar d = Calendar.getInstance();
			int hour = d.get(Calendar.HOUR_OF_DAY);
			int minute = d.get(Calendar.MINUTE);
			if(minute + dueIn >= 60){
				minute = (minute + dueIn) % 60;
				if(hour + 1 == 24){
					hour = 00;
				}else{
					hour++;
				}

			}

			expArrival = hour + ":" + minute;
			
		}
	}
	public String getDest(){
		return destination;
	}
	
	public int getDue(){
		return dueIn;
	}

	public String getExpArr(){
		return expArrival;
	}
}
