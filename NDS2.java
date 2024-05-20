package nds.pkg2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class NDS2 extends Application {
    private double x=0;
    private double y=0;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        
        
        Scene scene = new Scene(root);
         root.setOnMousePressed((MouseEvent event)->{
        x=event.getSceneX();
        y=event.getSceneY();
        });
        
        root.setOnMouseDragged((MouseEvent event)->{
            stage.setX(event.getScreenX()-x);
            stage.setY(event.getScreenY()-y);

        });
        
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("NUST DRUTO SEBA");
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        stage.setScene(scene);
        stage.show();
       
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
