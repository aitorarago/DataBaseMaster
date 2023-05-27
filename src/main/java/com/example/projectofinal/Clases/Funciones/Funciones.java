package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.Clases.Personalizacion.ItemList;
import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class Funciones extends DataMasterController implements Initializable {

    @FXML
    private SplitMenuButton bdid;
    @FXML
    private ListView functionsis;
    @FXML
    private ImageView addid;
    @FXML
    private ImageView dropid;
    @FXML
    private ImageView editid;
    @FXML
    private ImageView searchid;
    @FXML
    private ImageView startid;
    @FXML
    private TextArea searchfunctionid;
    @FXML
    private Button saveid;
    int index = 0;

    /**
     * Función implementada por la interface Initializable, lo que hace que se ejecute este metodo nadamas empezar
     * @param url no lo uso
     * @param resourceBundle no lo uso
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarBD();
        anadirToastIconos();
        index = functionsis.getSelectionModel().getSelectedIndex();

    }

    /**
     * Función que añade información a los iconos
     */
    private void anadirToastIconos() {
        //añadir funcion
        String at = "Crear función";
        Tooltip tooltip1 = new Tooltip(at);
        tooltip1.setShowDuration(Duration.millis(5000));
        tooltip1.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(addid, tooltip1);
        //elimiar funcion
        String a = "Eliminar funcióm";
        Tooltip tooltip = new Tooltip(a);
        tooltip.setShowDuration(Duration.millis(5000));
        tooltip.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(dropid, tooltip);
        //editar funcion
        String att = "Editar función";
        Tooltip tooltip2 = new Tooltip(att);
        tooltip2.setShowDuration(Duration.millis(5000));
        tooltip2.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(editid, tooltip2);
        //Visualizar BD
        String ar = "Ver el Codigo función";
        Tooltip tooltipp = new Tooltip(ar);
        tooltipp.setShowDuration(Duration.millis(5000));
        tooltipp.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(searchid, tooltipp);
        //Ejecutar funcion
        String aff = "Ejecutar Función";
        Tooltip tooltipf = new Tooltip(aff);
        tooltipf.setShowDuration(Duration.millis(5000));
        tooltipf.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(startid, tooltipf);
    }

    /**
     * Función para añadir las BD a SplitMenuButton
     */
    private void cargarBD() {
        bdid.setText("Selecciona una opción");
        MainApplication.getBduser().forEach(basedeDatos -> bdid.getItems().add(new MenuItem(basedeDatos.toString())));
        bdid.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
            MainApplication.setDB(MainApplication.getBD(menuItem.getText()));
            bdid.setText("Base de datos seleccionada: " + menuItem.getText()); // Actualiza el título con el valor seleccionado
            try {
                visualizarfunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Función para obtener las funciones asignadas a una BD
     * @throws SQLException excepción del lenguaje SQL
     */
    private void visualizarfunctions() throws SQLException {
        functionsis.getItems().clear();
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();
        List<String> list = new ArrayList<>();
        if(MainApplication.getDB().getType().equals("postgresql")){
        functionsis.getItems().clear();
        index = functionsis.getSelectionModel().getSelectedIndex();

        String sql = "SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public';";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getString("routine_name"));
        }
        }
        else {
            String sql = "SHOW FUNCTION STATUS WHERE Db = '"+MainApplication.getDB().getNameBD()+"';";
                ResultSet resultSet = st.executeQuery(sql);
                while (resultSet.next()) {
                    String functionName = resultSet.getString("Name");
                    list.add(functionName);
           }
        }
        functionsis.getItems().addAll(list);
        functionsis.setCellFactory(stringListView -> new ItemList());
        functionsis.refresh();
        functionsis.setOnMouseClicked(event -> {
            index = functionsis.getSelectionModel().getSelectedIndex();
        });
        functionsis.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                index = functionsis.getSelectionModel().getSelectedIndex();
                searchfunctionid.clear();
                try {
                    searchfunction();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Función para crear una nueva función
     */
    public void addfunction() {
        searchfunctionid.clear();
        searchfunctionid.setEditable(true);
        saveid.setDisable(false);
        saveid.setVisible(true);
        saveid.setOnAction(actionEvent -> {
            Connection conn = MainApplication.getDB().getConexion();
            PreparedStatement stmt = null;
            try {
                System.out.println(searchfunctionid.getText());
                stmt = conn.prepareStatement(searchfunctionid.getText());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchfunctionid.setEditable(false);
            saveid.setDisable(true);
            saveid.setVisible(false);
            searchfunctionid.clear();
            try {
                visualizarfunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Función para eliminar una función
     * @throws SQLException excepción del lenguaje SQL
     */
    public void dropfunction() throws SQLException {
        searchfunctionid.setEditable(false);
        saveid.setDisable(true);
        saveid.setVisible(false);
        searchfunctionid.clear();
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();
        String sql = "Drop function " + functionsis.getItems().get(index).toString();
        st.executeUpdate(sql);
        saveid.setOnAction(actionEvent -> {
            Connection conn = MainApplication.getDB().getConexion();
            PreparedStatement stmt = null;
            try {
                System.out.println(searchfunctionid.getText());
                stmt = conn.prepareStatement(searchfunctionid.getText());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchfunctionid.setEditable(false);
            saveid.setDisable(true);
            saveid.setVisible(false);
            searchfunctionid.clear();
            functionsis.getItems().remove(index);
            functionsis.refresh();
            try {
                visualizarfunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Función para poder editar el codigo de una función
     * @throws SQLException excepción del lenguaje SQL
     */
    public void editfunction() throws SQLException {
        index = functionsis.getSelectionModel().getSelectedIndex();
        String codigo="";
        searchfunction();
        searchfunctionid.setEditable(true);
        saveid.setDisable(false);
        saveid.setVisible(true);
        saveid.setOnAction(actionEvent -> {

            Connection conn = MainApplication.getDB().getConexion();
            PreparedStatement stmt = null;
            try {
                String resp = searchfunctionid.getText();
                dropfunction();
                stmt = conn.prepareStatement(resp);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            searchfunctionid.setEditable(false);
            saveid.setDisable(true);
            saveid.setVisible(false);
            searchfunctionid.clear();
            functionsis.getItems().clear();
            try {
                visualizarfunctions();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Función para poder visualizar el codigo de una función
     * @throws SQLException excepción del lenguaje SQL
     */
    public void searchfunction() throws SQLException {
        searchfunctionid.setEditable(false);
        saveid.setDisable(true);
        saveid.setVisible(false);
        searchfunctionid.clear();
        index = functionsis.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                    no se ha seleccionado ninguna
                    base de datos, por favor seleccione
                    una para poder continuar.""");
            alert.show();
        }
        else{
            Connection con = MainApplication.getDB().getConexion();
            Statement st = con.createStatement();
            if(MainApplication.getDB().getType().equals("mysql")){
                String functionName = functionsis.getItems().get(index).toString();
                String sql = "SHOW CREATE FUNCTION " + functionName;
                ResultSet rs = st.executeQuery(sql);
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(rs.getString("Create Function"));
                }
                searchfunctionid.setText(sb.toString());
        }
        else {
            String sql = "SELECT pg_get_functiondef('" + functionsis.getItems().get(index).toString() + "'::regproc);";
            ResultSet sr = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (sr.next()) {
                sb.append(sr.getString(1));
            }
            searchfunctionid.setText(sb.toString());
        }
        }
    }

    /**
     * Función que permite ejecutar una función
     * @throws SQLException excepción del lenguaje SQL
     */
    public void runfunction() throws SQLException{
        if(MainApplication.getDB().getType().equals("mysql")){
            searchfunctionid.setEditable(false);
            saveid.setDisable(true);
            saveid.setVisible(false);
            searchfunctionid.clear();

            Connection con = MainApplication.getDB().getConexion();
            Statement statement = con.createStatement();
            String sql = "SELECT PARAMETER_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_NAME = '" + functionsis.getItems().get(index).toString() + "'";
            ResultSet rs = statement.executeQuery(sql);
            List<String> args = new ArrayList<>();
            while (rs.next()) {
                String paramName = rs.getString("PARAMETER_NAME");
                String paramType = rs.getString("DATA_TYPE");

                args.add(paramName + " " + paramType);
            }

            Stage dialog = new Stage();
            dialog.setTitle("Llamar función");
            VBox root = new VBox();
            // Crear etiquetas y campos de texto para los argumentos
            List<Label> argLabels = new ArrayList<>();
            List<TextField> argFields = new ArrayList<>();
            for (String arg : args) {
                String[] parts = arg.split(" ");
                String argName = parts[0];
                String argType = parts[1];

                Label label = new Label(argName + " (" + argType + "):");
                TextField field = new TextField();

                if(argName.equals("null")){continue;}
                argLabels.add(label);
                argFields.add(field);
                root.getChildren().addAll(label);
                root.getChildren().addAll(field);
            }

            // Crear el botón
            Button button = new Button("Ejecutar");
            button.setOnAction(event -> {
                StringBuilder callString = new StringBuilder("SELECT " + functionsis.getItems().get(index) + "(");
                for (int i = 0; i < argFields.size(); i++) {
                    if (i == argFields.size() - 1) {
                        callString.append(argFields.get(i).getText());
                    } else {
                        callString.append(argFields.get(i).getText()).append(",");
                        System.out.println(argLabels.get(i).getText());
                    }
                }
                callString.append(");");
                try (CallableStatement cs = con.prepareCall(callString.toString())) {
                    boolean hasResult = cs.execute();
                    if (hasResult) {
                        ResultSet result = cs.getResultSet();
                        if (result.next()) {
                            String output = result.getString(1);
                            root.getChildren().add(new Text("Resultado: " + output));
                        }
                    } else {
                        root.getChildren().add(new Text("Función ejecutada correctamente!"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            root.getChildren().add(button);

            // Establecer el tamaño del Stage
            dialog.setWidth(400);
            dialog.setHeight(300);

            // Crear el layout para los elementos de la interfaz de usuario
            root.setSpacing(10);
            root.setPadding(new Insets(10));

            // Crear la escena y agregarla al Stage
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();
        }
        else {
        searchfunctionid.setEditable(false);
        saveid.setDisable(true);
        saveid.setVisible(false);
        searchfunctionid.clear();

        Connection con = MainApplication.getDB().getConexion();
        Statement statement = con.createStatement();
        String sql = "SELECT p.proname as function_name,pg_catalog.pg_get_function_arguments(p.oid) as arguments FROM pg_catalog.pg_proc p WHERE p.proname = '"+functionsis.getItems().get(index).toString()+"';";
        ResultSet rs = statement.executeQuery(sql);
        List<String> args = new ArrayList<>();
        while (rs.next()){
            String arguments = rs.getString("arguments");
            arguments = arguments.replaceAll("\\)", ""); // eliminar parentesis finales
            String[] argArray = arguments.split(",");
            for (String arg : argArray) {
                String[] parts = arg.trim().split(" ");
                String argName = parts[0];
                String argType = parts[1];

                args.add(argName + " " + argType);
            }
        }

        Stage dialog = new Stage();
        dialog.setTitle("LLamar funcion");
        VBox root = new VBox();
        // Crear etiquetas y campos de texto para los argumentos
        List<Label> argLabels = new ArrayList<>();
        List<TextField> argFields = new ArrayList<>();
        for (String arg : args) {
            String[] parts = arg.split(" ");
            String argName = parts[0];
            String argType = parts[1];

            Label label = new Label(argName + " (" + argType + "):");
            TextField field = new TextField();

            argLabels.add(label);
            argFields.add(field);
            root.getChildren().addAll(label);
            root.getChildren().addAll(field);
        }

        // Crear el botón
        Button button = new Button("Ejecutar");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder callString = new StringBuilder("SELECT " + functionsis.getItems().get(index) + "(");
                for (int i = 0; i < argFields.size(); i++) {
                    if (i == argFields.size() - 1) {
                        callString.append(argFields.get(i).getText());
                    } else {
                        callString.append(argFields.get(i).getText()).append(",");
                        System.out.println(argLabels.get(i).getText());
                    }
                }
                callString.append(");");
                CallableStatement cs;
                System.out.println(callString);
                boolean trr = false;
                String resp = null;
                try {
                    cs = con.prepareCall(callString.toString());
                    ResultSet rs = cs.executeQuery();
                    while (rs.next()) {
                        trr = true;
                        resp = rs.getString(1);
                    }
                    if (trr) {
                        root.getChildren().add(new Text("Resultado: "+resp));
                    } else {
                        root.getChildren().add(new Text("Funcion ejecutada correctamente!"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        root.getChildren().add(button);

        // Establecer el tamaño del Stage
        dialog.setWidth(400);
        dialog.setHeight(300);

        // Crear el layout para los elementos de la interfaz de usuario
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        // Crear la escena y agregarla al Stage
        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.show();
        }
    }
}