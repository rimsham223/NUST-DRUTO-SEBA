package nds.pkg2;

import java.sql.Connection;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    //FXIDS

    @FXML
    private Button close;

    @FXML
    private Button close_1;

    @FXML
    private Button close1;

    @FXML
    private Button close11;

    @FXML
    private Button ds_loginbtn;

    @FXML
    private AnchorPane ds_loginform;

    @FXML
    private PasswordField ds_password;

    @FXML
    private TextField ds_username;

    @FXML
    private ComboBox<String> ds_rolebox;

    @FXML
    private Hyperlink fpd_hyper;

    @FXML
    private PasswordField reg_password;

    @FXML
    private AnchorPane reg_regform;

    @FXML
    private Button reg_signupbtn;

    @FXML
    private TextField reg_username;

    @FXML
    private ComboBox<?> reg_questionbox;

    @FXML
    private TextField reg_answer;

    @FXML
    private ComboBox<?> reg_rolebox;

    @FXML
    private Button side_createbtn;

    @FXML
    private Button side_exists;

    @FXML
    private AnchorPane side_form;

    @FXML
    private ImageView side_icon;

    @FXML
    private ComboBox<?> fordotp_questionbox;

    @FXML
    private PasswordField forgotp_confirmp;

    @FXML
    private TextField forgotp_answer;

    @FXML
    private AnchorPane forgotp_form2;

    @FXML
    private TextField forgotp_username;

    @FXML
    private AnchorPane frogotp_form1;

    @FXML
    private PasswordField frogotp_newp;

    @FXML
    private Button forgotp_back1;

    @FXML
    private Button forgotp_back2;

    //CLOSE WINDOW METHOD
    @FXML
    public void close() {
        System.exit(0);
    }

    //DB CONNECTIVITY
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private String[] rolelist = {"Customer", "Admin"};
    private String[] questionlist = {"What is your favorite Color?", "What is your favorite food?", "What is your birth date?"};

    //role
    public void regrolelist() {
        List<String> listR = new ArrayList<>();
        for (String data : rolelist) {
            listR.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(listR);
        ds_rolebox.setItems(ListData);

        reg_rolebox.setItems(ListData);
    }

    //question
    public void questionlist() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionlist) {
            listQ.add(data);
        }
        ObservableList ListData1 = FXCollections.observableArrayList(listQ);

        reg_questionbox.setItems(ListData1);
    }

    //forgot password question
    public void forgotPassQuestionList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionlist) {
            listQ.add(data);
        }
        ObservableList ListData1 = FXCollections.observableArrayList(listQ);
        fordotp_questionbox.setItems(ListData1);
    }

    //chnage password
    public void changePassbtn() {

        if (frogotp_newp.getText().isEmpty() || forgotp_confirmp.getText().isEmpty()) {
            //alert: fill all fields
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields.");
            alert.showAndWait();
        } else {
            if (frogotp_newp.getText().equals(forgotp_confirmp.getText())) {
                String getDate = "SELECT date FROM customer WHERE customer_name = '"
                        + forgotp_username.getText() + "'";

                connect = database.connectDB();

                try {

                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();

                    String date = "";
                    if (result.next()) {
                        date = result.getString("date");
                    }
                    String updatePass = "UPDATE customer SET customer_password='"
                            + frogotp_newp.getText() + "',question='"
                            + fordotp_questionbox.getSelectionModel().getSelectedItem() + "',answer='"
                            + forgotp_answer.getText() + "',date ='"
                            + date + "' WHERE customer_name = '"
                            + forgotp_username.getText() + "'";

                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    alert.showAndWait();

                    ds_loginform.setVisible(true);
                    forgotp_form2.setVisible(false);

                    // TO CLEAR FIELDS
                    forgotp_confirmp.setText("");
                    frogotp_newp.setText("");
                    fordotp_questionbox.getSelectionModel().clearSelection();
                    forgotp_answer.setText("");
                    forgotp_username.setText("");

                } catch (Exception e) {

                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Not match");
                alert.showAndWait();
            }
        }

    }

    //forgot password
    public void switchforgotPassword() {
        frogotp_form1.setVisible(true);
        ds_loginform.setVisible(false);
        forgotPassQuestionList();
    }

    //continue btn
    public void Continuebtn() {

        if (forgotp_username.getText().isEmpty() || fordotp_questionbox.getSelectionModel().getSelectedItem() == null || forgotp_answer.getText().isEmpty()) {
            //alert: fill all fields
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields.");
            alert.showAndWait();
        } else {
            String selectData = "SELECT customer_name, question, answer FROM customer WHERE customer_name=? AND question=? AND answer=?";
            connect = database.connectDB();

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, forgotp_username.getText());
                prepare.setString(2, (String) fordotp_questionbox.getSelectionModel().getSelectedItem());
                prepare.setString(3, forgotp_answer.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    forgotp_form2.setVisible(true);
                    frogotp_form1.setVisible(false);
                } else {
                    //alert: Incorrect answer
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect answer.");
                    alert.showAndWait();
                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    private Alert alert;
    AdminPage1Controller adp2= new AdminPage1Controller();

    //LOGIN BTN FUNCTIONALITY
    public void loginbtn() {
        if (ds_username.getText().isEmpty() || ds_password.getText().isEmpty() || ds_rolebox.getSelectionModel().getSelectedItem() == null) {
            //alert: Incorrect username or password. (empty fields)
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        } else {

            String selectData = "SELECT customer_name, customer_password, customer_role FROM customer WHERE customer_name= ? AND customer_password= ? AND customer_role= ?";
            connect = database.connectDB();

            //ERROR HANDLING
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, ds_username.getText());
                prepare.setString(2, ds_password.getText());
                prepare.setString(3, (String) ds_rolebox.getSelectionModel().getSelectedItem());

                result = prepare.executeQuery();

                //IF LOGGED IN SUCCESSFULLY PROCEED TO NEXT PAGE :(HOME/MAIN FORM)
                if (result.next()) {
                    //to get the username
                    data.username = ds_username.getText();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Logged in successfully!");

                    alert.showAndWait();
                    
                    //SWITCH FORM BASED ON ROLE
                    Parent root= null;

                    switch (ds_rolebox.getSelectionModel().getSelectedItem()) {

                        case ("Admin") -> root = FXMLLoader.load(getClass().getResource("Admin Page1.fxml"));
                        case ("Customer") -> root = FXMLLoader.load(getClass().getResource("Admin Page1.fxml"));
                        default -> {
                        }
                    }
                    //LINK ADMIN PAGE1
                    // Handle invalid roles or show an error message

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();

                    ds_loginform.getScene().getWindow().hide();

                } else {
                    //alert: Incorrect username or password. (empty fields)
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password.");
                    alert.showAndWait();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //SIGNUP BUTTON FUNCTIONALITY
    public void regbtn() {
        String regData;
        if (reg_username.getText().isEmpty() || reg_rolebox.getSelectionModel().getSelectedItem() == null
                || reg_password.getText().isEmpty() || reg_questionbox.getSelectionModel().getSelectedItem() == null
                || reg_answer.getText().isEmpty()) {
            //alert: fill all fields
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields.");
            alert.showAndWait();
        } else {
            //path for user input
            String role = (String) reg_rolebox.getSelectionModel().getSelectedItem();

            //insert data in database
            regData = "INSERT INTO customer(customer_name, customer_password, customer_role, date, question, answer) VALUES (?, ?, ?, ?, ?, ?)";

            //connect databse
            connect = database.connectDB();

            //ERROR HANDLING
            try {
                //CHECK IF USERNAME IS ALREADY TAKEN
                String checkUsername = "SELECT customer_name FROM customer WHERE customer_name= '"
                        + reg_username.getText() + "'";
                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if (result.next()) {
                    //alert: username already taken
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(reg_username.getText() + "is already taken.");
                    alert.showAndWait();
                } else if (reg_password.getText().length() < 8) {
                    //alert: PASSWORD 8-CHAR
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Password should have atleast 8 characters");
                    alert.showAndWait();
                } else {
                    //connection, get user input for values
                    prepare = connect.prepareStatement(regData);

                    prepare.setString(1, reg_username.getText());
                    prepare.setString(2, reg_password.getText());
                    prepare.setString(3, (String) reg_rolebox.getSelectionModel().getSelectedItem());

                    //date
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(4, String.valueOf(sqlDate));
                    prepare.setString(5, (String) reg_questionbox.getSelectionModel().getSelectedItem());
                    prepare.setString(6, reg_answer.getText());

                    prepare.executeUpdate();

                    //alert message: account added successfully
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Account registered successfully!");
                    alert.showAndWait();

                    //insertion in columns
                    reg_username.setText(" ");
                    reg_password.setText(" ");
                    reg_rolebox.getSelectionModel().clearSelection();
                    reg_questionbox.getSelectionModel().clearSelection();
                    reg_answer.setText(" ");

                    //slider
                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(side_form);
                    slider.setToX(0);

                    slider.setOnFinished((ActionEvent e) -> {
                        side_exists.setVisible(false);
                        side_createbtn.setVisible(true);
                    });

                    slider.play();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //BACK BUTTONS
    public void backToLoginForm() {
        ds_loginform.setVisible(true);
        frogotp_form1.setVisible(false);
    }

    public void backToQuestionForm() {
        frogotp_form1.setVisible(true);
        forgotp_form2.setVisible(false);
    }

    //SLIDER
    @FXML
    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_createbtn) {

            slider.setNode(side_form);
            slider.setToX(300);

            slider.setOnFinished((ActionEvent e) -> {
                side_exists.setVisible(true);
                side_createbtn.setVisible(false);

                frogotp_form1.setVisible(false);
                ds_loginform.setVisible(true);
                forgotp_form2.setVisible(false);

                regrolelist();
                questionlist();
            });

            slider.play();
        } else if (event.getSource() == side_exists) {
            slider.setNode(side_form);
            slider.setToX(0);

            slider.setOnFinished((ActionEvent e) -> {
                side_exists.setVisible(false);
                side_createbtn.setVisible(true);

                frogotp_form1.setVisible(false);
                ds_loginform.setVisible(true);
                forgotp_form2.setVisible(false);

            });

            slider.play();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        regrolelist();

    }

}
