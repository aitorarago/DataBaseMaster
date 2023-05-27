package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.Clases.Personalizacion.ItemList;
import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VisualizarDB extends DataMasterController implements Initializable {

    @FXML
    private ImageView terminalid;
    @FXML
    private ImageView consultaid;

    @FXML
    private ImageView siguientebtn;
    @FXML
    private ImageView eliminarid;
    @FXML
    private ImageView insertrow;
    @FXML
    private ImageView deleterow;
    @FXML
    private ImageView nuevatabla;
    @FXML
    private ImageView editartablas;

    @FXML
    private ListView<String> tablasDB;
    @FXML
    private AnchorPane scenatot;
    @FXML
    private TableView tableviewid;
    private BasedeDatos bd;
    private String itemselected;
    private ObservableList<ObservableList> data;

    /**
     * Función que actualiza la lista de las tablas
     * @throws SQLException
     */
    public void refresh() throws SQLException {
        ObservableList<String> basedeDatosObservableList = tablasDB.getItems();
        List<String> list = bd.getTablas();
        basedeDatosObservableList.clear();
        basedeDatosObservableList.addAll(list);
        tablasDB.setCellFactory(stringListView -> new ItemList());
        tablasDB.refresh();


        tablasDB.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                itemselected = tablasDB.getSelectionModel().getSelectedItem();
                tableviewid.getColumns().clear();
                setTablasDB();
            }
        });
    }

    /**
     * Función implementada por la interface Initializable, lo que hace que se ejecute este metodo nadamas empezar
     * @param url no lo uso
     * @param resourceBundle no lo uso
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bd = MainApplication.getDB();
        try {
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        anadirToastImagenes();
        tablasDB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                itemselected = tablasDB.getSelectionModel().getSelectedItem();
                MainApplication.setTabla(itemselected);
            }
        });

    }

    /**
     * Función que añade información a los iconos
     */
    private void anadirToastImagenes() {
        //eliminar tabla
        String bt = "Eliminar Tabla";
        Tooltip tooltip = new Tooltip(bt);
        tooltip.setShowDuration(Duration.millis(5000));
        tooltip.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(eliminarid, tooltip);
        //añadir tabla
        String at = "Crear Tabla";
        Tooltip tooltip1 = new Tooltip(at);
        tooltip1.setShowDuration(Duration.millis(5000));
        tooltip1.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(nuevatabla, tooltip1);
        //editar tabla
        String et = "Editar Tabla";
        Tooltip tooltip2 = new Tooltip(et);
        tooltip2.setShowDuration(Duration.millis(5000));
        tooltip2.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(editartablas, tooltip2);
        //visualizar tabla
        String vt = "Visualizar Tabla";
        Tooltip tooltip3 = new Tooltip(vt);
        tooltip3.setShowDuration(Duration.millis(5000));
        tooltip3.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(siguientebtn, tooltip3);
        //consultar tabla
        String ct = "Realizar Consulta";
        Tooltip tooltip4 = new Tooltip(ct);
        tooltip4.setShowDuration(Duration.millis(5000));
        tooltip4.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(consultaid, tooltip4);
        //terminal tabla
        String tt = "Terminal";
        Tooltip tooltip5 = new Tooltip(tt);
        tooltip5.setShowDuration(Duration.millis(5000));
        tooltip5.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(terminalid, tooltip5);//añadir tabla
        String ef = "Eliminar fila";
        Tooltip tooltip6 = new Tooltip(ef);
        tooltip6.setShowDuration(Duration.millis(5000));
        tooltip6.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(deleterow, tooltip6);//añadir tabla
        String af = "Añadir fila";
        Tooltip tooltip7 = new Tooltip(af);
        tooltip7.setShowDuration(Duration.millis(5000));
        tooltip7.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(insertrow, tooltip7);


    }

    /**
     * Función que al clicar muestra el contenido de la tabla seleccionada
     * @param actionEvent
     */
    public void buscartablas(MouseEvent actionEvent) {
        // Supongamos que tienes una ListView<String> llamada myListView

        // Haz algo con el elemento seleccionado
        if (itemselected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                    no ha sido posible acceder a esta tabla,
                    intentalo en 1 minuto.""");
            alert.show();
        } else {
            MainApplication.setTabla(itemselected);
            tableviewid.getColumns().clear();
            setTablasDB();
            tableviewid.refresh();
        }

    }

    /**
     * Función que muestra el contenido de la tabla en una tableview, y añade la logica de la edición (luego llama a actualizarValor))
     */
    public void setTablasDB() {
        Connection connection = MainApplication.getDB().getConexion();
        String nombreTabla = MainApplication.getTabla();


        //SQL FOR SELECTING ALL OF CUSTOMER
        String SQL = "SELECT * from " + nombreTabla + ";";
        //ResultSet
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        data = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        Object value = param.getValue().get(j);
                        String stringValue = (value != null) ? value.toString() : "";
                        return new SimpleStringProperty(stringValue);
                    }
                });

                tableviewid.getColumns().addAll(col);
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String value = rs.getString(i);
                    row.add(value != null ? value : "");
                }
                data.add(row);
            }
            //FINALLY ADDED TO TableView
            tableviewid.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        tableviewid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int selectedIndex = tableviewid.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < tableviewid.getItems().size()) {
                        // Get the selected cell
                        TablePosition<ObservableList, Object> pos = (TablePosition<ObservableList, Object>) tableviewid.getSelectionModel().getSelectedCells().get(0);
                        int row = pos.getRow();
                        TableColumn col = pos.getTableColumn();

                        // Make the cell editable
                        col.setCellFactory(TextFieldTableCell.forTableColumn());
                        col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Object, String>>() {
                            @Override
                            public void handle(TableColumn.CellEditEvent<Object, String> t) {
                                // Obtener el nuevo valor editado por el usuario
                                String newValue = t.getNewValue();

                                TablePosition<ObservableList, ?> pos = (TablePosition<ObservableList, ?>) tableviewid.getSelectionModel().getSelectedCells().get(0);
                                int colIndex = pos.getColumn();
                                TableColumn<ObservableList, ?> prevCol = (TableColumn<ObservableList, ?>) tableviewid.getColumns().get(colIndex);
                                String prevColValue = String.valueOf(prevCol.getCellData(pos.getRow()));
                                try {
                                    actualizarValor(newValue, col.getText(), prevColValue);
                                } catch (SQLException e) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error!");
                                    alert.setContentText(e.toString());
                                }
                                // Actualizar el objeto asociado a la fila
                                t.getTableView().getItems().get(t.getTablePosition().getRow());

                            }
                        });

                        tableviewid.setEditable(true);
                        tableviewid.edit(row, col);
                    } else {
                        try {
                            addNewRow();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

    }

    /**
     * Función que permite la eliminación de una tabla
     */
    public void eliminartabla() {
        if (itemselected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                    no ha sido posible acceder a esta tabla,
                    intentalo en 1 minuto.""");
            alert.show();
        } else {
            // Crear una nueva ventana de diálogo
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Confirmar Eliminación");

            // Crear etiquetas y botones
            Label label = new Label("¿Estás seguro de que quieres eliminar este elemento?");
            Button deleteButton = new Button("Eliminar");
            Button cancelButton = new Button("Cancelar");

            // Configurar los botones
            deleteButton.setOnAction(e -> {
                Connection connection = MainApplication.getDB().getConexion();
                Statement st;
                try {
                    st = connection.createStatement();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String sql = "DROP TABLE " + itemselected;
                try {
                    st.executeUpdate(sql);
                    tablasDB.getItems().remove(itemselected);
                    refresh();
                } catch (SQLException ex) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Error");
                   alert.setContentText("Error al eliminar la tabla, otras tablas dependen de esta");
                   alert.show();
                }
                dialog.close();
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

    /**
     * Función que permite al usuario añadir nuevas filas en una tabla (llama al metodo insertarfilas)
     * @throws SQLException Excepción del lenguaje SQL
     */
    public void addNewRow() throws SQLException {
        Stage dialog = new Stage();
        dialog.setTitle("Insertar nuevos valores en la tabla");
        Label lb = new Label();
        TextField txt = new TextField();
        lb.setText("Introduce la cantidad de filas a añadir");
        Button btncolumnas = new Button("continuar");
        AtomicInteger filasnuevas = new AtomicInteger();
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(lb,txt,btncolumnas);
        final boolean[] bool = {true};
        Button guardarButton = new Button("Guardar");
        List<String[]> nombrecolumas = MainApplication.getDB().getColumnasTabla(MainApplication.getTabla());
        btncolumnas.setOnAction(actionEvent1 -> {
            if(bool[0]){
                if(txt.getText().length()>0){
                    int col = Integer.parseInt(txt.getText());
                    filasnuevas.set(col);
                    for (int i=0;i<col;i++){
                        // Crear un TextField para el nombre de la columna
                        HBox hbox = new HBox(10);
                        for (int j = 0; j < nombrecolumas.size(); j++) {
                            Label label = new Label();
                            label.setText(nombrecolumas.get(j)[0]);
                            TextField txtNombreColumna = new TextField();
                            // Crear una fila para la columna (nombre y tipo de dato)

                            hbox.getChildren().addAll(
                                    label,
                                    txtNombreColumna
                            );
                        }
                        vbox.getChildren().add(hbox);

                    }   bool[0] =false;
                    HBox btns = new HBox();
                    btns.getChildren().addAll(guardarButton);
                    vbox.getChildren().add(btns);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("El numero de columnas debe estar especificado");
                    alert.show();
                    txt.clear();
                    dialog.close();
                }
            }
        });
        guardarButton.setOnAction(event -> {
            boolean camposCompletos = true;
            String[][] datos = new String[filasnuevas.get()][nombrecolumas.size()];
            int i=0;
            for (Node node : vbox.getChildren()) {
                if (i < filasnuevas.get()) {
                    if (node instanceof HBox) {
                        HBox fila = (HBox) node;
                        int j = 1; // empezar en la segunda posición del HBox, que corresponde al primer TextField
                        while (j < fila.getChildren().size()) {
                            TextField nombre = (TextField) fila.getChildren().get(j);
                            if (nombre.getText().isEmpty()) {
                                camposCompletos = false;
                                break;
                            }
                            datos[i][j/2] = nombre.getText(); // dividir entre dos para obtener la posición correspondiente en el array de datos
                            j += 2; // saltar a la siguiente posición de TextField
                        }
                        i++;
                    }
                }
            }

            if (camposCompletos) {
                // Llamar al método crearTabla() con los valores ingresados
                insertarFilas(datos,nombrecolumas);
                dialog.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Por favor, complete todos los campos.");
                alert.show();
            }
        });
        Scene scene = new Scene(vbox,500,400);
        dialog.setScene(scene);
        dialog.show();

    }

    /**
     * Función que permite eliminar filas en una tabla
     */
    public void deleterow() {
        if (tableviewid.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("""
                    no ha sido posible eliminar la fila,
                    intentalo en 1 minuto.""");
            alert.show();
        } else {
            // Crear una nueva ventana de diálogo
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Confirmar Eliminación");

            // Crear etiquetas y botones
            Label label = new Label("¿Estás seguro de que quieres eliminar la fila?");
            Button deleteButton = new Button("Eliminar");
            Button cancelButton = new Button("Cancelar");

            // Configurar los botones
            deleteButton.setOnAction(e -> {
                Connection connection = MainApplication.getDB().getConexion();
                Statement st;
                try {
                    st = connection.createStatement();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String[] row = tableviewid.getSelectionModel().getSelectedItems().get(0).toString().split(",");
                TableColumn firstColumn = (TableColumn) tableviewid.getColumns().get(1);
                String columnName = firstColumn.getText();

                String value = row[1].trim().replace("]", ""); // Eliminar el carácter "]"

                String sql = "DELETE FROM " + MainApplication.getTabla() + " WHERE " + columnName + "='" + value + "';";
                try {
                    System.out.println(sql);
                    st.executeUpdate(sql);
                    tableviewid.getItems().remove(tableviewid.getSelectionModel().getSelectedIndex());
                    tableviewid.refresh();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.close();
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

    /**
     * Función que realiza la logica de editar un elemento en una tabla
     * @param nvalor valor antiguo
     * @param columnname nombre de la columna
     * @param valor2 valor nuevo
     * @throws SQLException Excepción del lenguaje SQL
     */
    public void actualizarValor(String nvalor, String columnname,String valor2)throws SQLException{
        Connection con = MainApplication.getDB().getConexion();
        String datatype = MainApplication.getDB().getTypeColumn(columnname);
        String sql = "UPDATE "+MainApplication.getTabla()+" SET "+columnname+" = ? WHERE "+columnname+" = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        switch (datatype) {
            case "integer" -> {
                if (nvalor.isEmpty()) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, Integer.parseInt(nvalor));
                }
                if (valor2.isEmpty()) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, Integer.parseInt(valor2));
                }
                ps.executeUpdate();
            }
            case "float" -> {
                ps.setFloat(1, Float.parseFloat(nvalor));
                ps.setFloat(2, Float.parseFloat(valor2));
                ps.executeUpdate();
            }
            case "boolean" -> {
                ps.setBoolean(1, Boolean.parseBoolean(nvalor));
                ps.setBoolean(2, Boolean.parseBoolean(valor2));
                ps.executeUpdate();
            }
            case "date" -> {
                ps.setDate(1, Date.valueOf(nvalor));
                ps.setDate(2, Date.valueOf(valor2));
                ps.executeUpdate();
            }
            case "double precision" -> {
                ps.setDouble(1, Double.parseDouble(nvalor));
                ps.setDouble(2, Double.parseDouble(valor2));
                ps.executeUpdate();
            }
            default -> {
                ps.setString(1, nvalor);
                ps.setString(2, valor2);
                ps.executeUpdate();
            }
        }
        tableviewid.getColumns().clear();
        setTablasDB();
    }

    /**
     * Función que crea una escena en la qual el usuario puede establecer la logica de la tabla a creaar(luego llama a crearTabla)
     */
    public void createTable() {
        System.out.println("newtable");
        // Crear el diálogo para la creación de la tabla
        Stage dialog = new Stage();
        dialog.setTitle("Crear tabla");

        Label lb = new Label();
        Label lb1 = new Label();
        TextField txt = new TextField();
        lb.setText("Introduce la cantidad de columnas que tendrá la tabla");
        TextField txt1 = new TextField();
        lb1.setText("Introduce el nombre de la tabla");
        Button btncolumnas = new Button("continuar");
        AtomicInteger columnas = new AtomicInteger();
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(lb, txt, lb1, txt1, btncolumnas);
        final boolean[] bool = { true };
        Button guardarButton = new Button("Guardar");
        ToggleGroup toggleGroup = new ToggleGroup(); // Group para permitir solo una selección

        btncolumnas.setOnAction(actionEvent1 -> {
            if (bool[0]) {
                if (txt.getText().length() > 0) {
                    int col = Integer.parseInt(txt.getText());
                    columnas.set(col);
                    List<RadioButton> checkBoxes = new ArrayList<>(); // Lista para almacenar los CheckBox

                    for (int i = 0; i < col; i++) {
                        TextField txtNombreColumna = new TextField();
                        ChoiceBox<String> choiceBoxTipoDato = new ChoiceBox<>();
                        choiceBoxTipoDato.getItems().addAll("INTEGER", "BOOLEAN", "VARCHAR", "FLOAT");
                        txtNombreColumna.setPromptText("Nombre de la columna " + (i + 1));

                        RadioButton checkBoxClavePrimaria = new RadioButton("Clave primaria"); // CheckBox para marcar la columna como clave primaria
                        checkBoxClavePrimaria.setToggleGroup(toggleGroup); // Agregar el CheckBox al ToggleGroup

                        HBox hbox = new HBox(10);
                        hbox.getChildren().addAll(
                                txtNombreColumna,
                                choiceBoxTipoDato,
                                checkBoxClavePrimaria // Agregar el CheckBox a la HBox
                        );

                        vbox.getChildren().add(hbox);
                        checkBoxes.add(checkBoxClavePrimaria); // Agregar el CheckBox a la lista
                    }
                    bool[0] = false;
                    HBox btns = new HBox();
                    btns.getChildren().addAll(guardarButton);
                    vbox.getChildren().add(btns);

                    guardarButton.setOnAction(event -> {
                        boolean camposCompletos = true;
                        String[][] datos = new String[columnas.get()][2];
                        int i = 0;
                        for (Node node : vbox.getChildren()) {
                            if (i < columnas.get()) {
                                if (node instanceof HBox) {
                                    HBox fila = (HBox) node;
                                    TextField nombre = (TextField) fila.getChildren().get(0);
                                    ChoiceBox<String> tipoDato = (ChoiceBox<String>) fila.getChildren().get(1);
                                    RadioButton checkBox = checkBoxes.get(i); // Obtener el CheckBox correspondiente

                                    if (nombre.getText().isEmpty() || tipoDato.getValue() == null) {
                                        camposCompletos = false;
                                        break;
                                    }

                                    datos[i][0] = nombre.getText();
                                    datos[i][1] = tipoDato.getValue();

                                    if (checkBox.isSelected()) {
                                        // Marcar la columna como clave primaria
                                        datos[i][1] += " PRIMARY KEY";
                                    }

                                    i++;
                                }
                            }
                        }

                        if (camposCompletos) {
                            try {
                                crearTabla(datos, txt1.getText());
                                dialog.close();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Por favor, complete todos los campos.");
                            alert.show();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("El número de columnas debe estar especificado");
                    alert.show();
                    txt.clear();
                    dialog.close();
                }
            }
        });

        Scene scene = new Scene(vbox, 500, 400);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Función que realiza la logica de crear la tabla
     * @param datos array bidimensional que contiene los nombres de la tabla nueva y los tipos de datos
     * @param tablename nombre de la tabla nueva
     * @throws SQLException Excepción del lenguaje SQL
     */
    private void crearTabla(String[][] datos,String tablename) throws SQLException{
        StringBuilder sql = new StringBuilder("create table " + tablename + "(");
        if(MainApplication.getDB().getType().equals("mysql")){
            for (int i = 0; i < datos.length; i++) {
                sql.append(datos[i][0]).append(" ");
            if(datos[i][1].equals("FLOAT"))sql.append("DECIMAL(8,2)");
            else if(datos[i][1].equals("VARCHAR"))sql.append("VARCHAR(40)");
            else {sql.append(datos[i][1]);}
                if (i != datos.length - 1) {
                    sql.append(",");
                } else {
                    sql.append(");");
                }
            }
            System.out.println(sql);

            Connection co = MainApplication.getDB().getConexion();
            Statement st = co.createStatement();
            st.executeUpdate(sql.toString());
        }
        else {
            for (int i = 0; i < datos.length; i++) {
            sql.append(datos[i][0]).append(" ").append(datos[i][1]);
            if (i != datos.length - 1) {
                sql.append(",");
            } else {
                sql.append(");");
            }
        }
        System.out.println(sql);

        Connection co = MainApplication.getDB().getConexion();
        Statement st = co.createStatement();
        st.executeUpdate(sql.toString());
        }
        tablasDB.getItems().add(tablename);
        refresh();
        System.out.println("fin");
    }

    /**
     * Funciñon que realiza la logica de la inserción de datos en una tabla
     * @param data array bidimensional que contiene los nombres de la tabla nueva y los tipos de datos
     * @param infotabla lista de array de strings que contiene la información de la tabla
     */
    private void insertarFilas(String[][] data, List<String[]> infotabla) {
        Connection con = MainApplication.getDB().getConexion();
        String tabla = MainApplication.getTabla();
        StringBuilder ppp = new StringBuilder("INSERT INTO " + tabla + " (");
        for (int i = 0; i < infotabla.size(); i++) {
            ppp.append(infotabla.get(i)[0]);
            if (i != infotabla.size() - 1) {
                ppp.append(", ");
            }
        }
        ppp.append(") VALUES (?");
        for (int i = 0; i < infotabla.size()-1; i++) {
            ppp.append(",?");
        }
        ppp.append(")");
        System.out.println(ppp.toString());
        try {
            PreparedStatement ps = con.prepareStatement(ppp.toString());
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    String tipoDato = infotabla.get(j)[1];
                    switch (tipoDato) {
                        case "integer":
                            ps.setInt(j + 1, Integer.parseInt(data[i][j]));
                            break;
                        case "float":
                            ps.setFloat(j + 1, Float.parseFloat(data[i][j]));
                            break;
                        case "boolean":
                            ps.setBoolean(j + 1, Boolean.parseBoolean(data[i][j]));
                            break;
                        case "date":
                            ps.setDate(j + 1, Date.valueOf(data[i][j]));
                            break;
                        case "double precision":
                            ps.setDouble(j + 1, Double.parseDouble(data[i][j]));
                            break;
                        default:
                            ps.setString(j + 1, data[i][j]);
                            break;
                    }
                }
                ps.executeUpdate();
                System.out.println(ps.toString());
            }
        } catch (SQLException e) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setContentText("error :"+e);
           alert.show();
        }
        tableviewid.getColumns().clear();
        setTablasDB();
    }

    /**
     * Función que permite editar el nombre de una columna y añadir o eliminar columnas
      */
    public void editartabla() {
        // Obtener la información de la tabla
        List<String[]> infotabla = null;
        try {
            infotabla = MainApplication.getDB().getColumnasTabla(MainApplication.getTabla());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Crear la ventana de edición de tabla
        Stage stage = new Stage();
        stage.setTitle("Editar tabla");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        List<CheckBox> checkBoxes = new ArrayList<>();
        // Agregar componentes a la ventana de edición de tabla
        List<TextField> nombreColumnaTextFields = new ArrayList<>();
        List<ChoiceBox<String>> tipoDatoChoiceBoxes = new ArrayList<>();
        for (int i = 0; i < infotabla.size(); i++) {
            Label columnaLabel = new Label("Columna " + (i + 1));
            grid.add(columnaLabel, 0, i);
            TextField nombreColumnaTextField = new TextField(infotabla.get(i)[0]);
            ChoiceBox<String> tipoDatoChoiceBox = new ChoiceBox<>();
            tipoDatoChoiceBox.getItems().addAll("INTEGER", "BOOLEAN", "VARCHAR", "FLOAT");
            grid.add(nombreColumnaTextField, 1, i);
            CheckBox eliminarCheckBox = new CheckBox("Eliminar");
            checkBoxes.add(eliminarCheckBox);
            grid.add(eliminarCheckBox, 3, i);
            grid.add(tipoDatoChoiceBox, 2, i);
            nombreColumnaTextFields.add(nombreColumnaTextField);
            tipoDatoChoiceBoxes.add(tipoDatoChoiceBox);
        }

        // Agregar botones de añadir columna, eliminar columna, guardar y cancelar
        Button agregarColumnaButton = new Button("Añadir columna");
        Button eliminarColumnaButton = new Button("Eliminar columna");
        Button guardarButton = new Button("Guardar cambios");
        Button cancelarButton = new Button("Cancelar");
        HBox botonesHBox = new HBox();
        botonesHBox.setAlignment(Pos.CENTER_RIGHT);
        botonesHBox.setSpacing(10);
        botonesHBox.getChildren().addAll(agregarColumnaButton, eliminarColumnaButton, guardarButton, cancelarButton);
        grid.add(botonesHBox, 0, infotabla.size());

        // Configurar acción del botón de añadir columna
        agregarColumnaButton.setOnAction(actionEvent -> {
            int nuevaColumnaIndex = nombreColumnaTextFields.size();
            Label nuevaColumnaLabel = new Label("Columna " + (nuevaColumnaIndex + 1));
            TextField nuevaColumnaTextField = new TextField();
            ChoiceBox<String> nuevoTipoDatoChoiceBox = new ChoiceBox<>();
            nuevoTipoDatoChoiceBox.getItems().addAll("INTEGER", "BOOLEAN", "VARCHAR", "FLOAT");
            grid.add(nuevaColumnaLabel, 0, nuevaColumnaIndex);
            grid.add(nuevaColumnaTextField, 1, nuevaColumnaIndex);
            grid.add(nuevoTipoDatoChoiceBox, 2, nuevaColumnaIndex);
            nombreColumnaTextFields.add(nuevaColumnaTextField);
            tipoDatoChoiceBoxes.add(nuevoTipoDatoChoiceBox);
        });

        // Configurar acción del botón de eliminar columna
        List<String[]> finalInfotabla1 = infotabla;
        eliminarColumnaButton.setOnAction(actionEvent -> {
            List<CheckBox> checkBoxesSeleccionados = checkBoxes.stream()
                    .filter(CheckBox::isSelected)
                    .collect(Collectors.toList());

            for (CheckBox checkBox : checkBoxesSeleccionados) {
                int columnaIndex = GridPane.getRowIndex(checkBox);

                // Obtener información de la columna seleccionada
                String[] columnaSeleccionada = finalInfotabla1.get(columnaIndex);

                // Eliminar la columna de la base de datos
                try {
                    Connection con = MainApplication.getDB().getConexion();
                    String sql = "ALTER TABLE " + MainApplication.getTabla() + " DROP COLUMN " +  columnaSeleccionada[0];
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // Eliminar los componentes asociados a la columna del GridPane
                grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == columnaIndex);
            }
        });


        // Configurar acción del botón de guardar
        List<String[]> finalInfotabla = infotabla;
        guardarButton.setOnAction(e -> {
            // Recorrer los componentes de la ventana de edición de tabla para obtener los nuevos valores de nombres de columnas y tipos de datos
            List<String[]> nuevasColumnas = new ArrayList<>();
            for (int i = 0; i < nombreColumnaTextFields.size(); i++) {
                String[] columna = new String[2];
                columna[0] = nombreColumnaTextFields.get(i).getText();
                columna[1] = tipoDatoChoiceBoxes.get(i).getValue();
                nuevasColumnas.add(columna);
            }

            // Actualizar la estructura de la tabla en la base de datos
            try {
                modificarTabla(MainApplication.getTabla(), nuevasColumnas, finalInfotabla);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            // Cerrar la ventana de edición de tabla
            stage.close();
        });

        // Configurar acción del botón de cancelar
        cancelarButton.setOnAction(e -> {
            // Cerrar la ventana de edición de tabla
            stage.close();
        });

        // Mostrar la ventana de edición de tabla
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Función que realiza la logica de editar la tabla
     * @param tabla nombre de la tabla
     * @param nuevasColumnas columnas modificadas
     * @param anteriores columnas anteriores
     * @throws SQLException array bidimensional que contiene los nombres de la tabla nueva y los tipos de datos
     */
    private void modificarTabla(String tabla, List<String[]> nuevasColumnas,List<String[]> anteriores) throws SQLException{
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();
        if(MainApplication.getDB().getType().equals("postgresql")){
                for (int i = 0; i < nuevasColumnas.size(); i++) {
            if(i<anteriores.size()){
            if(nuevasColumnas.get(i)[0].equals(anteriores.get(i)[0])){
            }
            else{
                String sql ="ALTER TABLE "+tabla+" RENAME COLUMN "+anteriores.get(i)[0]+" TO "+nuevasColumnas.get(i)[0];
                st.executeUpdate(sql);
            }

            }
            else {
                String sql ="ALTER TABLE "+tabla+" ADD "+nuevasColumnas.get(i)[0]+" "+nuevasColumnas.get(i)[1];
                st.executeUpdate(sql);
            }
        }
        }
        else {
                for (int i = 0; i < nuevasColumnas.size(); i++) {
                    if (i < anteriores.size()) {
                        if (nuevasColumnas.get(i)[0].equals(anteriores.get(i)[0])) {
                        } else {
                            String sql = "ALTER TABLE " + tabla + " RENAME COLUMN `" + anteriores.get(i)[0] + "` TO `" + nuevasColumnas.get(i)[0] + "`";
                            st.executeUpdate(sql);
                        }
                    } else {
                        String sql = "ALTER TABLE " + tabla + " ADD `" + nuevasColumnas.get(i)[0] + "` " + nuevasColumnas.get(i)[1];
                        st.executeUpdate(sql);
                    }
                }

        }
        tableviewid.getColumns().clear();
        setTablasDB();
    }

    /**
     * Función que permite al usuario realizar consultas
     */
    public void consultar() {
        Stage dialog = new Stage();
        dialog.setTitle("Consultar");

        VBox root = new VBox();

        // Crear etiquetas y campos de texto para los argumentos
        Label columnaLabel = new Label("Columna:");
        TextField columnaTextField = new TextField();

        Label valorLabel = new Label("Valor:(si el valor es un texto introducelo entre ' ' )");
        TextField valorTextField = new TextField();

        // Crear el botón
        Button button = new Button("Ejecutar");
        button.setOnAction(event -> {
            String columna = columnaTextField.getText();
            String valor = valorTextField.getText();
            String consulta = "SELECT * FROM "+itemselected+" WHERE " + columna + " = " + valor + "";
            ejecutarConsulta(consulta, dialog, root);
        });

        root.getChildren().addAll(columnaLabel, columnaTextField, valorLabel, valorTextField, button);

        // Crear la escena y agregarla al Stage
        Scene scene = new Scene(root);
        dialog.setHeight(200);
        dialog.setWidth(300);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Función que realiza la logica de la ejecución de la consulta
     * @param consulta consulta
     * @param dialog dialogo donde interactua el usuario y la app
     * @param root VerticalBox que muestra la escena
     */
    private void ejecutarConsulta(String consulta, Stage dialog, VBox root) {

      try {
          Connection connection = MainApplication.getDB().getConexion();
          Statement st = connection.createStatement();
          ResultSet rs =st.executeQuery(consulta);

          StringBuilder sb = new StringBuilder();
          ResultSetMetaData rsMetaData = rs.getMetaData();

          sb.append("Resultados de la consulta:\n");
          int columnCount = rsMetaData.getColumnCount();

          while (rs.next()) {
              for (int i = 1; i <= columnCount; i++) {
                  Object value = rs.getObject(i);
                  sb.append(value).append(" ");
              }
              sb.append("\n");
          }

          // Crear el TextArea para mostrar los resultados
          TextArea resultTextArea = new TextArea(sb.toString());
          resultTextArea.setEditable(false);

          // Reemplazar el VBox anterior con el TextArea de resultados
          root.getChildren().clear();
          root.getChildren().addAll(resultTextArea);

          // Establece el nuevo tamaño del Stage para ajustarse al contenido
          dialog.sizeToScene();
      }
      catch (SQLException e){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("ERROR");
          alert.setContentText("La consulta introducida no es correcta");
          alert.show();
      }
    }

    /**
     * Función que permite al usuario ejecutar comandos directamente (llama a executeCommand)
     */
    public void terminal(){

        Connection connection = null;
        connection = MainApplication.getDB().getConexion();

        // Configurar la interfaz de usuario
        TextArea terminalOutput = new TextArea();
            terminalOutput.setEditable(false);
            terminalOutput.setPrefRowCount(10);


        TextField commandInput = new TextField();
            commandInput.setPromptText("Ingresa un comando SQL");

        Button executeButton = new Button("Ejecutar");
        Connection finalConnection = connection;
        executeButton.setOnAction(e -> executeCommand(finalConnection, terminalOutput, commandInput));

        VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.getChildren().addAll(terminalOutput, commandInput, executeButton);

        Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Terminal de Base de Datos");
            stage.show();
    }

    /**
     * Función que realiza la logica de la ejecución del comando
     * @param connection connexión a la BD
     * @param terminalOutput Area de texto donde se añadirá la salida del comando
     * @param commandInput Area de texto donde el usuario añade los comandos
     */
    private void executeCommand(Connection connection, TextArea terminalOutput, TextField commandInput) {
        String command = commandInput.getText().trim();

        try (Statement statement = connection.createStatement()) {
            boolean hasResultSet = statement.execute(command);

            if (hasResultSet) {
                ResultSet resultSet = statement.getResultSet();
                printResultSet(resultSet, terminalOutput);
            } else {
                int updateCount = statement.getUpdateCount();
                terminalOutput.appendText("Se han actualizado " + updateCount + " registros.\n");
            }
        } catch (SQLException e) {
            terminalOutput.appendText("Error al ejecutar el comando: " + e.getMessage() + "\n");
        }

        commandInput.clear();
    }

    /**
     * Función que realiza la logica de mostrar la salida del comando
     * @param resultSet resultado del comando
     * @param terminalOutput Area de texto donde se mostrará
     * @throws SQLException Excepción del lenguaje SQL
     */
    private void printResultSet(ResultSet resultSet, TextArea terminalOutput) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            StringBuilder rowBuilder = new StringBuilder();

            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                rowBuilder.append(value).append("\t");
            }

            terminalOutput.appendText(rowBuilder + "\n");
        }
    }
}

