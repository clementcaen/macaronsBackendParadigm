package fr.clementjaminion.frontendinterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;


public class ProviderController {
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

        RestTemplate restTemplate = RestpreparationForJSON.preparationRequest();

        try {
            String restEndpointUrl = "http://localhost:8080/v1/macarons/" + addingchoiceBoxTaste.getValue() + "/add";

            HttpEntity<Integer> requestEntity = new HttpEntity<>((int) addingsliderNumberMacaron.getValue());

            ResponseEntity<MacaronDtoForJavaFx> macaronDtoResponseEntity =
                    restTemplate.exchange(restEndpointUrl, HttpMethod.PUT, requestEntity, MacaronDtoForJavaFx.class);

            returnInformations.setStyle("-fx-text-fill: green;");
            returnInformations.setText("Stock added ! Now : " + Objects.requireNonNull(macaronDtoResponseEntity.getBody()).taste() + " " + Objects.requireNonNull(macaronDtoResponseEntity.getBody()).stock() + " macarons are in stock.");
        } catch (Exception e) {
            returnInformations.setStyle("-fx-text-fill: red;");
            returnInformations.setText("Error: Make sure you provide all the necessary information to add stock.");
        }
    }

    @FXML
    public void deletingRequest() {

        try {

            RestTemplate restTemplate = RestpreparationForJSON.preparationRequest();

            String restEndpointUrl = "http://localhost:8080/v1/macarons/" + deletingchoiceBoxTaste.getValue() + "/reduce";

            HttpEntity<Integer> requestEntity = new HttpEntity<>((int) deletingsliderNumberMacaron.getValue());

            ResponseEntity<MacaronDtoForJavaFx> macaronDtoResponseEntity =
                    restTemplate.exchange(restEndpointUrl, HttpMethod.DELETE, requestEntity, MacaronDtoForJavaFx.class);

            returnInformations.setStyle("-fx-text-fill: green;");
            returnInformations.setText("Stock reduced ! Now : " + Objects.requireNonNull(macaronDtoResponseEntity.getBody()).taste() + " " + Objects.requireNonNull(macaronDtoResponseEntity.getBody()).stock() + " macarons are in stock.");
        }
        catch (Exception e) {
            returnInformations.setStyle("-fx-text-fill: red;");
            returnInformations.setText("Error: Make sure you provide all the necessary information to delete stock.");
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
