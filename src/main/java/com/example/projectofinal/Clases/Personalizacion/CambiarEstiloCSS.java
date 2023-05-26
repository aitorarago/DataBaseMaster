package com.example.projectofinal.Clases.Personalizacion;

import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class CambiarEstiloCSS extends DataMasterController implements Initializable {

@FXML
private RadioButton azul;
    @FXML
    private RadioButton amarillo;
    @FXML
    private RadioButton verde;
    @FXML
    private RadioButton rojo;
    @FXML
    private RadioButton negro;
    @FXML
    private RadioButton blanco;

    private String estilo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logicaRadioButtons();

    }

    private void logicaRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        rojo.setToggleGroup(group);
        negro.setToggleGroup(group);
        amarillo.setToggleGroup(group);
        verde.setToggleGroup(group);
        blanco.setToggleGroup(group);
        azul.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle() == null) {
                // Si no hay un RadioButton seleccionado, muestra una alerta o realiza alguna otra acción
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atención");
                alert.setContentText("Debe seleccionar una opción");
                alert.showAndWait();
                // También puedes desactivar un botón de "siguiente" u otra acción hasta que se seleccione una opción
            } else {
                System.out.println("Toggle seleccionado: " + group.getSelectedToggle().toString());
                // Realiza alguna acción en función del RadioButton seleccionado
                RadioButton selectedToggle = (RadioButton) group.getSelectedToggle();
                String toggleId = selectedToggle.getId();
                switch (toggleId) {
                    case "rojo" -> estilo = "rojo";
                    case "negro" -> estilo = "negro";
                    case "amarillo" -> estilo = "amarillo";
                    case "verde" -> estilo = "verde";
                    case "blanco" -> estilo = "blanco";
                    case "azul" -> estilo = "azul";
                }
            }

        });

    }

    public void setStyleCSS(){
        switch (estilo){
            case "rojo" -> MainApplication.cambiarEstilo("CSS/rojo.css");
            case "azul" -> MainApplication.cambiarEstilo("CSS/azul.css");
            case "amarillo" -> MainApplication.cambiarEstilo("CSS/amarillo.css");
            case "verde" -> MainApplication.cambiarEstilo("CSS/verde.css");
            case "negro" -> MainApplication.cambiarEstilo("CSS/negro.css");
            case "blanco" -> MainApplication.cambiarEstilo("CSS/blanco.css");
        }
    }

    public void drpoStyleCSS() {
        MainApplication.deleteStyle();
    }
}
