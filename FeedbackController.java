/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package nds.pkg2;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Abeeha Zehra
 */
public class FeedbackController implements Initializable {

    @FXML
    private ComboBox<String> fdbf_agegroup;

    @FXML
    private TextField fdbf_feedback;

    @FXML
    private TextField fdbf_username;

    @FXML
    private ImageView logo_fdbf;
    
       @FXML
    private Slider ratingslider;
    
    @FXML
    private Button submibtn;
    
    
    //DB CONNECTIVITY
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;
    private String[] agelist = {"Below 18 ", "18-35" , "35-50","50 Above"};
    
    
    //role
    public void agelist() {
        List<String> listR = new ArrayList<>();
        for (String data : agelist) {
            listR.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(listR);
        fdbf_agegroup.setItems(ListData);

        fdbf_agegroup.setItems(ListData);
    }
    
    //SUBMIT BUTTON FUNCTIONALITY
    public void submibtn() {
        String feedbackData;
        if (fdbf_username.getText().isEmpty() || fdbf_agegroup.getSelectionModel().getSelectedItem() == null) {
            //alert: fill all fields
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields.");
            alert.showAndWait();
        } else {
            int rating = (int) ratingslider.getValue();
            //path for user input
            String agegroup = (String) fdbf_agegroup.getSelectionModel().getSelectedItem();

            //insert data in database
            feedbackData = "INSERT INTO feedback(customer_name, age_group, rating, feeback) VALUES (?, ?, ?, ?)";

            //connect databse
            connect = database.connectDB();

            //ERROR HANDLING
            try {
                    //connection, get user input for values
                    prepare = connect.prepareStatement(feedbackData);

                    prepare.setString(1, fdbf_username.getText());
                    prepare.setString(2, (String) fdbf_agegroup.getSelectionModel().getSelectedItem());
                    prepare.setInt(3, rating); 
                    prepare.setString(4, fdbf_feedback.getText());
                    prepare.executeUpdate();

                    //alert message: account added successfully
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Feedback submitted successfully!");
                    alert.showAndWait();

                    //insertion in columns
                    fdbf_username.setText(" ");
                    fdbf_agegroup.getSelectionModel().clearSelection();
                    fdbf_username.setText(" ");
                    fdbf_feedback.setText(" ");
                    
                

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agelist() ;
    }    
    
}
