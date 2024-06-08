package fr.clementjaminion.frontendinterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class HelloController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    public void raspberry() throws IOException {
        openPostScreen("raspberry");
    }
    public void blackberryButton() throws IOException {
        openPostScreen("blackberry");
    }
    public void strawberryButton() throws IOException {
        openPostScreen("strawberry");
    }
    public void cranberryButton() throws IOException {
        openPostScreen("cranberry");
    }
    public void redBerryButton() throws IOException {
        openPostScreen("redberry");
    }
    public void grapeButton() throws IOException {
        openPostScreen("grape");
    }
    public void openPostScreen(String taste) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("post-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        PostScreenController controller = fxmlLoader.getController();
        controller.initializeByHelloController(taste);//initialize after receive the get
        HelloApplication.getCentralStage().setTitle(taste);
        HelloApplication.getCentralStage().setScene(scene);
        HelloApplication.getCentralStage().show();

    }

    public void openProviderScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("provider-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 350);
            HelloApplication.getCentralStage().setTitle("Provider");
            HelloApplication.getCentralStage().setScene(scene);
            HelloApplication.getCentralStage().show();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
