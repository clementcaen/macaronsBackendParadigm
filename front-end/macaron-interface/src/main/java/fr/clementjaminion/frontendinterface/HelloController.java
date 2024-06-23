package fr.clementjaminion.frontendinterface;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HelloController implements Initializable {
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

    public void openPostScreen(String taste) {
        loadFXML("post-screen.fxml", controller -> {
            PostScreenController postController = (PostScreenController) controller;
            postController.initializeByHelloController(taste);
        }, taste);
    }
    private void loadFXML(String fxmlFile, Consumer<Object> controllerInitializer, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), 600, 350);
            controllerInitializer.accept(fxmlLoader.getController());
            Stage stage = HelloApplication.getCentralStage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openProviderScreen() {
        if (!isAdmin()) {
            showForbiddenMessage();
            return;
        }
        loadFXML("provider-view.fxml", controller -> {}, "Provider");
    }


    private boolean isAdmin() {
        try {
            String url = "http://localhost:8080/manage/v1/role";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", LoginController.getAuthToken());

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            return response.getStatusCode().is2xxSuccessful() && "admin".equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }

    private void showForbiddenMessage() {
        Alert alert = new Alert(AlertType.ERROR, "You do not have permission to access this screen.", ButtonType.OK);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}