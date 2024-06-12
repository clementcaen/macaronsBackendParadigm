package fr.clementjaminion.frontendinterface;

import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleDto;
import fr.clementjaminion.macaronsbackend.models.dto.command.CreateSaleEntryDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public void initializeByHelloController(String taste) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", LoginController.getAuthToken());

        String restEndpointUrl = "http://localhost:8080/v1/macarons/" + taste;

        try {
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<MacaronDtoForJavaFx> response = restTemplate.exchange(restEndpointUrl, HttpMethod.GET, request, MacaronDtoForJavaFx.class);

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                MacaronDtoForJavaFx macaronDto = response.getBody();
                macaronName.setText(Objects.requireNonNull(macaronDto).taste());
                stock.setText(String.valueOf(macaronDto.stock()));
                price.setText(String.valueOf(macaronDto.unitPrice()));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void sendPostRequest() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = RestpreparationForJSON.preparationHeaders();
            headers.set("Authorization", LoginController.getAuthToken());

            String restEndpointUrl = "http://localhost:8080/v1/sales";
            CreateSaleDto createSaleDto = new CreateSaleDto(
                    List.of(new CreateSaleEntryDto(
                            macaronName.getText(),
                            (int) sliderNumber.getValue()
                    )),
                    DISPENSER_NAME
            );
            HttpEntity<String> requestEntity = new HttpEntity<>(createSaleDto.toJson(), headers);

            ResponseEntity<SaleDtoForJavaFx> response = restTemplate.postForEntity(restEndpointUrl, requestEntity, SaleDtoForJavaFx.class);

            returnInformations.setStyle("-fx-text-fill: green;");
            if (response.hasBody()) {
                SaleDtoForJavaFx saleDto = response.getBody();
                returnInformations.setText("Order confirmed! Now: " + Objects.requireNonNull(saleDto).firstnameReservation() + " ordered " +
                        saleDto.saleEntryDto().get(0).numberOfMacarons() + " " +
                        saleDto.saleEntryDto().get(0).macaron().taste() + " macarons on " + saleDto.date());
                stock.setText(String.valueOf(saleDto.saleEntryDto().get(0).macaron().stock())); // Update the stock
                price.setText(String.valueOf(saleDto.saleEntryDto().get(0).macaron().unitPrice()));
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
