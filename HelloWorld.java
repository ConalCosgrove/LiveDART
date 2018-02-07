import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.*;

public class HelloWorld extends Application 
{

    @Override
    public void start(Stage primaryStage) 
    {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");

        ListView<String> listView = new ListView<String>();
        listView.getItems().add("Trains");

        try{
            BufferedReader in = new BufferedReader(new FileReader("data.txt"));
            String line = "";
            while((line = in.readLine()) != null){
                listView.getItems().add(line);
            }
        }catch(Exception e){
            System.out.print(e);
        }

        btn.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        root.getChildren().add(listView);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addTrains(){

    }

}
