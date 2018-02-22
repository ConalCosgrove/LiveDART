# LiveDART
Terminal based Java app which gives live updates on IrishRail trains serving a specified station including arrival time and destination.

## To Run: 

- Clone this repo
- In terminal, navigate to `/LiveDART/` directory 
- Run `./compile.sh` on Linux/Mac or `./compile.bat` on Windows

### 	Linux/Mac
- Run `./run.sh`

###		Windows
- Navigate into LiveDART/classes/ 
- Run `java dart` in the command line
- If file does not have sufficient permissions to run, use command `chmod 757 dart.class`


## In App:

- You will be be greeted with the initial UI, defaulting to Dublin Pearse.
![alt text](https://github.com/ConalCosgrove/LiveDART/blob/master/screenshots/SCUI1.png "Inital Screen")

- Use the dropdown menu to select your desired station and the list of trains will populate.
![alt text](https://github.com/ConalCosgrove/LiveDART/blob/master/screenshots/SCUI2.png "Using Dropdown")

## In Console: 

- You will be prompted to enter a station name, a full list of stations can be found by typing `stations` at this point.
- Once a station is entered the app should produce a list of any trains serving the chosen station in the next 30 mins.
- This list will refresh every 2 mins or whenever `refresh` is entered by the user.

## Commands:
- `refresh` refreshes the current station with the latest data.
- Typing in the name of another station will print the data of trains serving this newly entered station.
- `exit` exits the program.

![alt text](https://github.com/ConalCosgrove/LiveDART/blob/master/screenshots/SC1.png "Inital Prompt")
![alt text](https://github.com/ConalCosgrove/LiveDART/blob/master/screenshots/SC2.png "Example of Data")
