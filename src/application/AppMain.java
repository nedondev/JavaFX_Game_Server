
package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import Utility.EncryptionManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author KJW finish at 2016/ 02/ 15
 * @version 1.0.0v
 * @description this class Start The Application and Set The init State
 * @copyRight of KJW all Rights Reserved and follow the MIT license
 */
public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Settings.isDevelopingMode)
            BuildingTimeReadFile();

        System.out.println("Start Server");

        // set the window's title
        primaryStage.setTitle(Settings.sServerTitle);
        // load the fxml resource
        Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
        // set the fxml resource to the scene
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        // set the window's width
        primaryStage.setWidth(480);
        // set the windows' height
        primaryStage.setHeight(480);
        // set the user can resize the window
        primaryStage.setResizable(false); // 윈도우 크기를 조정할 수 없도록 함
        primaryStage.show();

        /*
         * this event is occurred when you click the exit button in windows
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // check the termination
                System.out.println("Terminate the System");
                fileSetBuildingTime();
                /*
                 * totally terminate the application. this application's
                 * resource will return to system resource manager
                 */
                System.exit(0);
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see javafx.application.Application#stop() this function manage the event
     * exit; if you exit the application, then this function is called by
     * system.
     */
    @Override
    public void stop() throws Exception {
        // TODO Auto-generated metho5d stub
        super.stop();
        System.out.println("Terminate the System");
        fileSetBuildingTime();
        // totally terminate the application. this application's resource will
        // return to system resource manager
        System.exit(0);
    }

    public static void main(String[] args) {
        // launch the fx application init function.
        launch(args);
    }

    private void fileSetBuildingTime() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(Settings.sBuildingVersionFileName);
            pw.println(EncryptionManager.encrypt(Settings.nBuildingTimes + ""));
            pw.println(EncryptionManager.encrypt(Settings.clientVersion));
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("serverinfo.jrc do not exist");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void BuildingTimeReadFile() {
        BufferedReader br;
        try {

            br = new BufferedReader(new FileReader(Settings.sBuildingVersionFileName));

            String temp = br.readLine();

            temp = temp == null ? "0" : temp;

            if (!temp.equals("0"))
                Settings.nBuildingTimes = Integer.parseInt(EncryptionManager.decrypt(temp));
            else
                Settings.nBuildingTimes = Integer.parseInt("0");
            Settings.nBuildingTimes++;
            System.out.println("BuildTime : " + Settings.nBuildingTimes);
            br.close();
        }

        catch (FileNotFoundException e) {
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
