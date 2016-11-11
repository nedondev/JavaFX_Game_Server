
package ServerMainBody;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * @author KJW finish at 2016/ 08/ 11
 * @version 2.0.0v
 * @description this class manage the Server Starter
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */

public class RootProcessor implements Initializable {

    /**
     * start Server button
     */
    @FXML
    private Button btnLogin;

    /**
     * this hbox is that include server button
     */
    @FXML
    HBox boxb;

    /*
     * this function set the start windows about server
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Draw server Main Logo
        final ImageView imv = new ImageView();
        // load image file
        File file = new File("src/Asset/JerryCoLogo.png");
        // allocate the image file to image variable
        Image image = new Image(file.toURI().toString());
        // set image file about imv
        imv.setImage(image);
        // set image position x in the application
        imv.setLayoutX(100);
        // set image position y in the application
        imv.setLayoutY(500);
        // set image Height size
        imv.setFitHeight(300);
        imv.setScaleY(2);
        
        // set image Width size
        imv.setFitWidth(500);

        // add The Draw Item to the bosb
        boxb.getChildren().add(imv);

        // register the click Event about button Listener
        btnLogin.setOnAction(e -> handleBtnLogin(e));
    }

    /**
     * this function change the First Logo Scene to Main Server Scene and this
     * function is used in the button listener
     * 
     * @param event
     */
    public void handleBtnLogin(ActionEvent event) {
        try {

            // Change The Scene. Logo -> Main Server Windows - you must read the
            // this is java book
            Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
            StackPane root = (StackPane)btnLogin.getScene().getRoot();
            root.getChildren().add(login);

            login.setTranslateX(350);

            // Set Change Scene attribute using slide Change Format
            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(login.translateXProperty(), 0);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
