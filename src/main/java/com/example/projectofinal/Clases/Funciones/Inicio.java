package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.Clases.Personalizacion.ItemList;
import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Inicio extends DataMasterController implements Initializable {
    @FXML
    private ImageView delete;
    @FXML
    private ImageView add;
    @FXML
    private ImageView show;
    @FXML
    private Button siguientebtn;
    @FXML
    private ListView<String> listabdid = new ListView<>();
    @FXML
    private AnchorPane scenatot = new AnchorPane();
    private int selectedItem;

    public void refresh(){
       listabdid.getItems().clear();
        ObservableList<String> basedeDatosObservableList = listabdid.getItems();
        basedeDatosObservableList.setAll(MainApplication.getBduser().stream().map(BasedeDatos::toString).collect(Collectors.toList()));
        scenatot = (AnchorPane) MainApplication.getStage().getScene().getRoot();
        listabdid.setCellFactory(stringListView -> new ItemList());
        listabdid.setPrefSize(300, 300);

// Ancla el centro de la ListView al centro del AnchorPane
        AnchorPane.setTopAnchor(listabdid, (scenatot.getHeight() - listabdid.getPrefHeight()) / 2);
        AnchorPane.setBottomAnchor(listabdid, (scenatot.getHeight() - listabdid.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(listabdid, (scenatot.getWidth() - listabdid.getPrefWidth()) / 2);
        AnchorPane.setRightAnchor(listabdid, (scenatot.getWidth() - listabdid.getPrefWidth()) / 2);

// Agrega la ListView al AnchorPane
        scenatot.getChildren().add(listabdid);
        listabdid.refresh();
        listabdid.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedItem = listabdid.getSelectionModel().getSelectedIndex();
                MainApplication.setDB(MainApplication.getBD(newValue));
            }
        });

        listabdid.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedItem = listabdid.getSelectionModel().getSelectedIndex();
                // Realizar acciones adicionales para el doble clic (por ejemplo, cambiar de escena)
                try {
                    MainApplication.cambiarEscena("FXML/visualizarTabla.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MainApplication.inicio();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.refresh();
        anadirToastIconos();
        if(listabdid.getItems().size()==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sin conectores");
            alert.setContentText("Actualmente no dispones de conectores," +
                    "para crear uno ve a la barra superior al apartado" +
                    " bases de datos y, cicla en nueva, seguidamente" +
                    "rellenalo con la información correspondiente  ");
            alert.show();
            selectedItem = listabdid.getSelectionModel().getSelectedIndex();
        }


    }

    private void anadirToastIconos() {
        //eliminar tabla
        String bt = "Eliminar Base de Datos";
        Tooltip tooltip = new Tooltip(bt);
        tooltip.setShowDuration(Duration.millis(5000));
        tooltip.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(delete, tooltip);
        //añadir tabla
        String at = "Crear Base de Datos";
        Tooltip tooltip1 = new Tooltip(at);
        tooltip1.setShowDuration(Duration.millis(5000));
        tooltip1.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(add, tooltip1);
        //visualizar tabla
        String vt = "Visualizar Base de Datos";
        Tooltip tooltip3 = new Tooltip(vt);
        tooltip3.setShowDuration(Duration.millis(5000));
        tooltip3.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(show, tooltip3);
    }

    public void buscartablas() {
       if(selectedItem==-1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setContentText("""
                        no se ha seleccionado ninguna
                        base de datos, por favor seleccione
                        una para poder continuar.""");
                alert.show();
            }
            else {
                try {
                    MainApplication.cambiarEscena("FXML/visualizarTabla.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

    }

    public void eliminarBD(MouseEvent mouseEvent) {
        if(selectedItem==-1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                        no se ha seleccionado ninguna
                        base de datos, por favor seleccione
                        una para poder continuar.""");
            alert.show();
        }
        else {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Confirmar Eliminación");

            // Crear etiquetas y botones
            Label label = new Label("¿Estás seguro de que quieres eliminar el connector a esta Base de Datos?");
            Button deleteButton = new Button("Eliminar");
            Button cancelButton = new Button("Cancelar");

            // Configurar los botones
            deleteButton.setOnAction(e -> {
                MainApplication.deleteBD(MainApplication.getDB());
                listabdid.getItems().remove(selectedItem);
                dialog.close();
                refresh();
            });

            cancelButton.setOnAction(e -> {
                dialog.close();
            });

            // Añadir las etiquetas y botones a un layout
            HBox hbox = new HBox(10, deleteButton, cancelButton);
            VBox vbox = new VBox(10, label, hbox);
            vbox.setAlignment(Pos.CENTER);
            hbox.setAlignment(Pos.CENTER);

            // Añadir el layout a la escena y mostrar la ventana
            Scene dialogScene = new Scene(vbox, 300, 100);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        }
    }


    public void addBD(MouseEvent mouseEvent) throws IOException{
        MainApplication.cambiarEscena("FXML/crearconectorBD.fxml");
    }


    //Editar nombre BD, no funciona por tema permisos, desde un connector no se puede realizar.
    public void editBD(MouseEvent mouseEvent) {
        BasedeDatos bdselected = MainApplication.getBduser().get(selectedItem);
        MainApplication.setDB(bdselected);
        if(selectedItem==-1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                        no se ha seleccionado ninguna
                        base de datos, por favor seleccione
                        una para poder continuar.""");
            alert.show();
        }
        else {
            TextInputDialog dialogo = new TextInputDialog();
            dialogo.setTitle("Editar Base de Datos");
            dialogo.setHeaderText("Introduzca el nuevo nombre");
            dialogo.setContentText("Nombre:");

            Optional<String> resultado = dialogo.showAndWait();
            String nuevonom = "";
            if (resultado.isPresent()) {
                nuevonom = resultado.get();
            } else {
                dialogo.close();
            }
            cambionombre(nuevonom);
        }
    }

    private void cambionombre(String nuevonom) {
        // Nombre de la base de datos original y nueva
        BasedeDatos nombreBaseDatosOriginal = MainApplication.getDB();
        String nombreBaseDatosNueva = nuevonom;
        ExportarBD exportarBD = new ExportarBD();
        exportarBD.addruta();
        try (Connection connection = MainApplication.getDB().getConexion();
             Statement statement = connection.createStatement()) {
            try {
                exportarBD.exportBD();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String sqlCrear = "CREATE DATABASE " + nombreBaseDatosNueva;
            statement.executeUpdate(sqlCrear);

            MainApplication.deleteBD(nombreBaseDatosOriginal);

            BasedeDatos bd = new BasedeDatos(MainApplication.getDB().getIp(), MainApplication.getDB().getPuerto(), nombreBaseDatosNueva, MainApplication.getDB().getUsername(), MainApplication.getDB().getPasswrd(), MainApplication.getDB().getType());
            MainApplication.addBD(bd);
            MainApplication.deleteBD(MainApplication.getDB());
            MainApplication.setDB(bd);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No dispones de permisos suficientes para realizar esta acción");
            alert.showAndWait();
            return;
        }

        // Restaurar la base de datos desde la copia de seguridad
        try (Connection connection = MainApplication.getDB().getConexion();
             Statement statement = connection.createStatement()) {
            // Lee el archivo SQL de la copia de seguridad
            FileChooser fileChooser= new FileChooser();
            String sqlRestaurar = new String(Files.readAllBytes(fileChooser.getInitialDirectory().toPath()));

            // Ejecuta los comandos SQL de restauración
            statement.executeUpdate(sqlRestaurar);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cambio de nombre exitoso");
            alert.setContentText("La base de datos se ha cambiado de nombre correctamente");
            alert.showAndWait();
        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Ha ocurrido un error al restaurar la base de datos");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}

