package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ExportarBD extends DataMasterController implements Initializable {
    @FXML
    private Text rutaa;
    @FXML
    private Group radiobuttons;
    @FXML

    private SplitMenuButton selecttb;
    @FXML

    private SplitMenuButton selectbd;
    @FXML

    private Button exportid;
    @FXML

    private Button ruta;
    @FXML
    private RadioButton tbid;
    @FXML
    private RadioButton bdid;
    private int item;
    private boolean ee;
    private String path;

    /**
     * Función implementada por la interface Initializable, lo que hace que se ejecute este metodo nadamas empezar
     * @param url no lo uso
     * @param resourceBundle no lo uso
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectbd.setVisible(false);
        selecttb.setVisible(false);
        logicaCheckbox();
    }

    /**
     * Establece la logica de los RadioButtons
     */
    public void logicaCheckbox(){
        ToggleGroup group = new ToggleGroup();
        bdid.setToggleGroup(group);
        tbid.setToggleGroup(group);
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
                if (bdid.isSelected()) {
                    item=1;
                    bdid.setVisible(true);
                    rellenarItemBD();
                } else if (tbid.isSelected()) {
                    ee = true;
                    item = 2;
                    tbid.setVisible(true);
                    rellenarItemBD();
                }
            }
        });
    }

    /**
     * Rellena SplitMenuButton añadiendole las BD
     */
    private void rellenarItemBD() {
        selectbd.setVisible(true);
        selectbd.setText("Selecciona una opción");
        MainApplication.getBduser().forEach(basedeDatos -> selectbd.getItems().add(new MenuItem(basedeDatos.toString())));
        selectbd.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
            MainApplication.setDB(MainApplication.getBD(menuItem.getText()));
            selectbd.setText("Seleccionada: " + menuItem.getText()); // Actualiza el título con el valor seleccionado

            if(ee){
                rellenarItemTabla();
            }
        }));
    }

    /**
     *  Rellena SplitMenuButton añadiendole las Tablas de la BD
     */
    private void rellenarItemTabla() {
        selecttb.setVisible(true);
        selecttb.setText("Selecciona una opción");
        try {
            MainApplication.getDB().getTablas().forEach(s -> {
                MenuItem menuItem = new MenuItem(s);
                menuItem.setOnAction(actionEvent -> {
                    MainApplication.setTabla(menuItem.getText());
                    selecttb.setText("Tabla: "+menuItem.getText());
                });
                selecttb.getItems().add(menuItem);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Función que realiza la logica de las exportaciones
     * @throws SQLException excepción del lenguaje SQL
     * @throws IOException excepción al leer un fichero
     */
    public void exportBD() throws SQLException, IOException {
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();


        if(ee){
            ResultSet resultSet = st.executeQuery("SELECT * FROM " + MainApplication.getTabla());
            ResultSetMetaData metaData = resultSet.getMetaData();
            // Abrir el archivo de salida y escribir la cabecera
            BufferedWriter writer = new BufferedWriter(new FileWriter(path+"/"+MainApplication.getTabla()+".csv"));
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                writer.write(metaData.getColumnName(i));
                if (i < metaData.getColumnCount()) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Escribir los datos de la tabla
            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object value = resultSet.getObject(i);
                    if (value != null) {
                        writer.write(value.toString());
                    }
                    if (i < metaData.getColumnCount()) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            // Cerrar el archivo de salida y la conexión a la base de datos
            writer.close();
            st.close();
        }
        else {
            MainApplication.getDB().exportBD(path);
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Exportación realizada");
            al.setContentText("Se ha realizado correctamente la exportación a:\nruta: "+path);
            al.setOnCloseRequest(dialogEvent -> {
                try {
                    MainApplication.cambiarEscena("FXML/visualizarTabla.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            al.show();

        }
    }

    /**
     * Función que permite al usuario navegar por su Sistema para obtener una ruta
     */
    public void addruta() {
        Stage st = new Stage();
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio");
        File selectedDirectory = fileChooser.showDialog(st);
        path=selectedDirectory.getPath();
        if(rutaa!=null)rutaa.setText("ruta: "+path);
    }
}

