package fr.clementjaminion.frontendinterface;

import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PostScreenController {
    public static final String DISPENSER_NAME = "dispenser1";
    @FXML
    private TextField macaronName;

    @FXML
    private TextField stock;

    @FXML
    private TextField price;

    @FXML
    private Slider sliderNumber;

    @FXML
    private TextField returnInformations;

    public void initialize() {
        macaronName.setText("");
    }


    public void initializeByHelloController(String taste){
        RestTemplate restTemplate = RestpreparationForJSON.preparationRequest();

        String restEndpointUrl = "http://localhost:8080/v1/macarons/" + taste;

        try {
            ResponseEntity<MacaronDtoForJavaFx> macaronDto =
                    restTemplate.getForEntity(restEndpointUrl, MacaronDtoForJavaFx.class);
            macaronName.setText(Objects.requireNonNull(macaronDto.getBody()).taste());
            if(macaronDto.hasBody()) {
                stock.setText(String.valueOf(macaronDto.getBody().stock()));
                price.setText(String.valueOf(macaronDto.getBody().unitPrice()));
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void sendPostRequest() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = RestpreparationForJSON.preparationHeaders();

            String restEndpointUrl = "http://localhost:8080/v1/sales";
            CreateSaleDto createSaleDto = new CreateSaleDto(
                    List.of(new CreateSaleEntryDto(
                            macaronName.getText(),
                            (int) sliderNumber.getValue()
                    )),
                    DISPENSER_NAME
            );
            HttpEntity<String> requestEntity = new HttpEntity<>(createSaleDto.toJson(), headers);

            ResponseEntity<SaleDtoForJavaFx> saleDtoForJavaFxResponseEntity =
                    restTemplate.postForEntity(restEndpointUrl, requestEntity, SaleDtoForJavaFx.class);

            returnInformations.setStyle("-fx-text-fill: green;");
            if (saleDtoForJavaFxResponseEntity.hasBody()) {
                returnInformations.setText("Order confirmed ! Now : " + Objects.requireNonNull(saleDtoForJavaFxResponseEntity.getBody()).firstnameReservation() + " ordered " + saleDtoForJavaFxResponseEntity.getBody().saleEntryDto().getFirst().numberOfMacarons() + " " + Objects.requireNonNull(saleDtoForJavaFxResponseEntity.getBody().saleEntryDto().getFirst().macaron().taste()) + " macarons on " + Objects.requireNonNull(saleDtoForJavaFxResponseEntity.getBody()).date());
                stock.setText(String.valueOf(saleDtoForJavaFxResponseEntity.getBody().saleEntryDto().getFirst().macaron().stock()));//update of the stock
                price.setText(String.valueOf(saleDtoForJavaFxResponseEntity.getBody().saleEntryDto().getFirst().macaron().unitPrice()));
            }

        } catch (Exception e) {
            returnInformations.setStyle("-fx-text-fill: red;");
            returnInformations.setText("Error: Make sure the stock is sufficient");
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
