package fr.clementjaminion.frontendinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

public class ProviderController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    ObservableList<String> listTaste = FXCollections.observableArrayList("blackberry", "cranberry", "grape", "raspberry", "redberry", "strawberry");

    @FXML
    private Slider addingsliderNumberMacaron;

    @FXML
    private ChoiceBox<String> addingchoiceBoxTaste;

    @FXML
    private Slider deletingsliderNumberMacaron;

    @FXML
    private ChoiceBox<String> deletingchoiceBoxTaste;

    @FXML
    private TextField returnInformations;

    @FXML
    private void initialize() {
        addingchoiceBoxTaste.setItems(listTaste);
        deletingchoiceBoxTaste.setItems(listTaste);
    }

    @FXML
    public void addingPutRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", LoginController.getAuthToken());

        try {
            String restEndpointUrl = "http://localhost:8080/manage/v1/macarons/" + addingchoiceBoxTaste.getValue() + "/add";
            HttpEntity<Integer> requestEntity = new HttpEntity<>((int) addingsliderNumberMacaron.getValue(), headers);
            logger.info("Sending add stock request to: {}", restEndpointUrl);
            ResponseEntity<MacaronDtoForJavaFx> response = restTemplate.exchange(restEndpointUrl, HttpMethod.PUT, requestEntity, MacaronDtoForJavaFx.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                returnInformations.setStyle("-fx-text-fill: green;");
                returnInformations.setText("Stock added! Now: " + Objects.requireNonNull(response.getBody()).taste() + " " + response.getBody().stock() + " macarons are in stock.");
                logger.info("Stock added successfully for taste: {}", response.getBody().taste());
            } else {
                returnInformations.setStyle("-fx-text-fill: red;");
                returnInformations.setText("Error adding stock: " + response.getStatusCode());
                logger.error("Failed to add stock, status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            returnInformations.setStyle("-fx-text-fill: red;");
            returnInformations.setText("Error: Make sure you provide all the necessary information to add stock.");
            logger.error("Exception during adding stock request", e);
        }
    }

    @FXML
    public void deletingRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", LoginController.getAuthToken());

        try {
            String restEndpointUrl = "http://localhost:8080/manage/v1/macarons/" + deletingchoiceBoxTaste.getValue() + "/reduce";
            HttpEntity<Integer> requestEntity = new HttpEntity<>((int) deletingsliderNumberMacaron.getValue(), headers);
            logger.info("Sending reduce stock request to: {}", restEndpointUrl);
            ResponseEntity<MacaronDtoForJavaFx> response = restTemplate.exchange(restEndpointUrl, HttpMethod.DELETE, requestEntity, MacaronDtoForJavaFx.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                returnInformations.setStyle("-fx-text-fill: green;");
                returnInformations.setText("Stock reduced! Now: " + Objects.requireNonNull(response.getBody()).taste() + " " + response.getBody().stock() + " macarons are in stock.");
                logger.info("Stock reduced successfully for taste: {}", response.getBody().taste());
            } else {
                returnInformations.setStyle("-fx-text-fill: red;");
                returnInformations.setText("Error reducing stock: " + response.getStatusCode());
                logger.error("Failed to reduce stock, status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            returnInformations.setStyle("-fx-text-fill: red;");
            returnInformations.setText("Error: Make sure you provide all the necessary information to delete stock.");
            logger.error("Exception during reducing stock request", e);
        }
    }

    public void openHelloScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 350);
        HelloApplication.getCentralStage().setTitle("Welcome to Macaron's shop!");
        HelloApplication.getCentralStage().setScene(scene);
        HelloApplication.getCentralStage().show();
    }
}
