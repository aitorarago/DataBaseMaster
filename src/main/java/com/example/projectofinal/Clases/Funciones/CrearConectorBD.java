package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CrearConectorBD extends DataMasterController implements Initializable {
    @FXML
    private ImageView add;
    @FXML
    private RadioButton postgreid;
    @FXML
    private RadioButton mysqlid;
    @FXML
    private AnchorPane scenatot;
    @FXML
    private TextField ipid;
    @FXML
    private TextField nombredb;
    @FXML
    private TextField username;
    @FXML
    private TextField puerto;
    @FXML
    private PasswordField password;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    private int camposllenos=0;
    boolean t = false;

    /**
     *Función que obtiene los campos de texto rellenados y crea la clase BD, añadiendola a la lista del Main
     */
    public void save() {
        camposllenos = (!ipid.getText().trim().isEmpty()) ? camposllenos + 1 : camposllenos;
        camposllenos = (!nombredb.getText().trim().isEmpty()) ? camposllenos + 1 : camposllenos;
        camposllenos = (!username.getText().trim().isEmpty()) ? camposllenos + 1 : camposllenos;
        camposllenos = (!puerto.getText().trim().isEmpty()) ? camposllenos + 1 : camposllenos;
        camposllenos = (!password.getText().trim().isEmpty()) ? camposllenos + 1 : camposllenos;
        if(t=true)camposllenos+=1;
        System.out.println("save!"+camposllenos);
        if(camposllenos>=6){
            if(postgreid.isSelected()){
            BasedeDatos bd = new BasedeDatos(ipid.getText().trim(),puerto.getText().trim(),nombredb.getText().trim(),username.getText().trim(),password.getText().trim(),"postgresql");
            try {
                if (verificarBD(bd.getConexion())) {
                    MainApplication.addBD(bd);
                    System.out.println(bd);
                    MainApplication.Guardar();
                    Thread t = new Thread(()-> MainApplication.setDB(bd));
                    t.start();

                    Alert a = new Alert(Alert.AlertType.INFORMATION);

                    a.setTitle("Conexión establecida");
                    a.setContentText("""
                            Se ha añadido la conexión a la memoria de la app,
                            cada vez que quieras abrir esta conexión, 
                            ves a Visualizar mis BD que esta situada 
                            en el menu superior de la app. """);
                    a.setOnCloseRequest(dialogEvent -> {
                        try {
                            MainApplication.cambiarEscena("FXML/visualizarTabla.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    a.show();

                }
                else {
                    Alert inst = new Alert(Alert.AlertType.ERROR);
                    inst.setTitle("ERROR DE CONEXION ");
                    inst.setHeaderText("DATABASE MASTER");
                    inst.setContentText("""
    NO HA SIDO POSIBLE ACCEDER A LA BASE DE DATOS,\040
    POR FAVOR VERIFICA LA INFORMACIÓN Y
    VUELVE A INTENTARLO.""");
                    inst.show();
                    ipid.setText("");
                    password.setText("");
                    username.setText("");
                    nombredb.setText("");
                    puerto.setText("");
                    camposllenos=0;
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            }
            else {
                BasedeDatos bd = new BasedeDatos(ipid.getText().trim(),puerto.getText().trim(),nombredb.getText().trim(),username.getText().trim(),password.getText().trim(),"mysql");
                try {
                    if (verificarBD(bd.getConexion())) {
                        MainApplication.addBD(bd);
                        System.out.println(bd);
                        MainApplication.Guardar();
                        Thread t = new Thread(()-> MainApplication.setDB(bd));
                        t.start();

                        Alert a = new Alert(Alert.AlertType.INFORMATION);

                        a.setTitle("Conexión establecida");
                        a.setContentText("""
                            Se ha añadido la conexión a la memoria de la app,
                            cada vez que quieras abrir esta conexión,\040
                            ves a Visualizar mis BD que esta situada\040
                            en el menu superior de la app.\040""");
                        a.setOnCloseRequest(dialogEvent -> {
                            try {
                                MainApplication.cambiarEscena("FXML/visualizarTabla.fxml");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        a.show();

                    }
                    else {
                        Alert inst = new Alert(Alert.AlertType.ERROR);
                        inst.setTitle("ERROR DE CONEXION ");
                        inst.setHeaderText("DATABASE MASTER");
                        inst.setContentText("""
    NO HA SIDO POSIBLE ACCEDER A LA BASE DE DATOS,\040
    POR FAVOR VERIFICA LA INFORMACIÓN Y
    VUELVE A INTENTARLO.""");
                        inst.show();
                        ipid.setText("");
                        password.setText("");
                        username.setText("");
                        nombredb.setText("");
                        puerto.setText("");
                        camposllenos=0;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            }
        }

    /**
     * Cambia la escena
     * @throws IOException excepción al leer un fichero
     */
    public void cancelar() throws IOException {
        MainApplication.cambiarEscena("inicio.fxml");
    }

    /**
     * Función que verifica la conexion d a la BD
     * @param connection es la conexion que se va a verificar
     * @return devuelve si se ha establecido conexion o no
     * @throws SQLException excepcion del lenguaje SQL
     */
    public boolean verificarBD(Connection connection) throws SQLException {
        if(connection.isValid(500))return true;
        else return false;
    }

    /**
     * Función que crea una base de datos y le da permisos al Usuario
     */
    public void addBD() {
        Stage st = new Stage();
        st.setTitle("Crear nueva DataBase");

        VBox root = new VBox();
        root.setSpacing(10);

        Text tx = new Text();
        tx.setText("Nombre de la base de datos");
        TextField dbNameTextField = new TextField();

        // MenuItem para seleccionar el conector
        MenuButton connectorMenu = new MenuButton("Conector");

        for (BasedeDatos bd : MainApplication.getBduser()){
            MenuItem menuItem = new MenuItem(bd.toString());
            menuItem.setOnAction(event -> {
               MainApplication.setDB(MainApplication.getBD(menuItem.getText()));
            });
            connectorMenu.getItems().add(menuItem);
        }
        Button addButton = new Button("Agregar");
        if(connectorMenu.getItems().size()==0){
            dbNameTextField.setVisible(false);
            dbNameTextField.setDisable(true);

            tx.setText("No puedes crear una base de datos sin ningun Conector");
        }
       else {
            addButton.setOnAction(event -> {
                String dbName = dbNameTextField.getText();


                Connection con = MainApplication.getDB().getConexion();
                Statement statement = null;
                try {
                    statement = con.createStatement();
                    String sql = "create database " + dbName;
                    statement.executeUpdate(sql);

                    String grantPermissionsSQL = "GRANT ALL PRIVILEGES ON DATABASE " + dbName + " TO " + MainApplication.getDB().getUsername();
                    statement.executeUpdate(grantPermissionsSQL);

                    // Cerrar la conexión
                    con.close();


                    BasedeDatos bd = new BasedeDatos(MainApplication.getDB().getIp(), MainApplication.getDB().getPuerto(), dbName, MainApplication.getDB().getUsername(), MainApplication.getDB().getPasswrd(), MainApplication.getDB().getType());
                    if(bd.getConexion().isValid(5000)) {
                        MainApplication.addBD(bd);
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Error de connexión a la base de datos :"+bd.getNameBD());
                        alert.showAndWait();
                        st.close();
                    }
                    st.close();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Base de Datos Creada");
                    alert.setContentText("Para entrar en esta BD nueva, necesitas acceder con el usuario :"+MainApplication.getDB().getUsername()+" y su contraseña.");
                    alert.showAndWait();
                    alert.setOnCloseRequest(dialogEvent -> {
                        try {
                            MainApplication.cambiarEscena("FXML/inicio.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    addButton.setVisible(true);
                    addButton.setDisable(false);
                    dbNameTextField.setVisible(true);
                    dbNameTextField.setDisable(false);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("No dispones de permisos suficientes para realizar esta acción");
                    alert.showAndWait();
                    st.close();
                }
                st.close();
            });
       }
       root.getChildren().addAll(tx,dbNameTextField, connectorMenu, addButton);

        Scene scene = new Scene(root, 300, 200);
        st.setScene(scene);
        st.show();
    }

    /**
     * Función implementada por la interface Initializable, lo que hace que se ejecute este metodo nadamas empezar
     * @param url no lo uso
     * @param resourceBundle no lo uso
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        mysqlid.setToggleGroup(toggleGroup);
        postgreid.setToggleGroup(toggleGroup);

        //añadir BD
        String at = "Crear Base de Datos vacia";
        Tooltip tooltip1 = new Tooltip(at);
        tooltip1.setShowDuration(Duration.millis(5000));
        tooltip1.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(add, tooltip1);
    }
}
