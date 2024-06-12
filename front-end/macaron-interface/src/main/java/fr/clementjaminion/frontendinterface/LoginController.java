package fr.clementjaminion.frontendinterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private static String authToken;

    @FXML
    protected void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            if (isAdmin()) {
                openProviderScreen();
            } else {
                openHelloScreen();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            String url = "http://localhost:8080/v1/macarons";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                authToken = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAdmin() {
        try {
            String url = "http://localhost:8080/manage/v1/role";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            return response.getStatusCode().is2xxSuccessful() && "admin".equals(response.getBody());
        } catch (Exception e) {
            //probably user is not admin
            System.out.println("User is not admin");
        }
        return false;
    }

    private void openProviderScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("provider-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 350);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Provider");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openHelloScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 350);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Welcome to Macaron's shop!");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAuthToken() {
        return authToken;
    }
}
