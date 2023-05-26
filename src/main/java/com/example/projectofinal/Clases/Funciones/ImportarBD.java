package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ImportarBD extends DataMasterController implements Initializable {
@FXML
    private Text txtpath;
    @FXML
    private RadioButton bdid;
    @FXML
    private Group groupid;
    @FXML
    private RadioButton imtb;
    @FXML
    private RadioButton imvl;
    @FXML
    private SplitMenuButton itemtablaid;
    @FXML
    private Button continuaridtbn;
    @FXML
    private SplitMenuButton itembdid;

    private String path;
    private int item;
    private boolean ee;

    //logica de subir el fitchero para importarlo
    public void importarfichero(ActionEvent actionEvent) {
            // Crear un objeto FileChooser
            FileChooser fileChooser = new FileChooser();

            // Establecer un título para el diálogo de selección de archivo
            fileChooser.setTitle("Seleccionar archivo");

            // Mostrar el diálogo de selección de archivo y esperar a que el usuario seleccione un archivo
            File selectedFile = fileChooser.showOpenDialog(MainApplication.getStage());

            // Si el usuario seleccionó un archivo, guardar su ruta en una variable
            if (selectedFile != null) {
                groupid.setVisible(true);
                path=selectedFile.getPath();
                logicaradiobuttons();
                txtpath.setText("ruta: "+path);
            }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupid.setVisible(false);
        itemtablaid.setVisible(false);
        itembdid.setVisible(false);
    }

    public void importarFicheroBD(String path) {
        try {
            MainApplication.getDB().importBD(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //logica de los radiobuttons
    public void logicaradiobuttons(){
        ToggleGroup group = new ToggleGroup();
        bdid.setToggleGroup(group);
        imtb.setToggleGroup(group);
        imvl.setToggleGroup(group);
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
                if (imtb.isSelected()) {
                    ee=true;
                    item=2;
                    itembdid.setVisible(true);
                    rellenarItemBD();
                } else if (imvl.isSelected()) {
                    ee=false;
                    item=3;
                    itembdid.setVisible(true);
                    rellenarItemBD();
                }
                else {
                    item=1;
                    itembdid.setVisible(true);
                    rellenarItemBD();
                }
            }
        });
    }

    //rellena los items del splimenubutton bases de datos

    public void rellenarItemBD() {
        itembdid.setText("Selecciona una opción");
        MainApplication.getBduser().forEach(basedeDatos -> itembdid.getItems().add(new MenuItem(basedeDatos.toString())));
        itembdid.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
            MainApplication.setDB(MainApplication.getBD(menuItem.getText()));
            itembdid.setText("Base de datos seleccionada: " + menuItem.getText()); // Actualiza el título con el valor seleccionado
            rellenarItemTabla();
        }));
    }


    //rellena los items del splimenubutton tablas
    public void rellenarItemTabla(){
        if(item==1){
        }
        else if(ee){
            MainApplication.setTabla(crearDialogoNombreTabla());
        }
        else {
            itemtablaid.setVisible(true);
            itemtablaid.setText("Selecciona una opción");
            try {
                MainApplication.getDB().getTablas().forEach(s -> itemtablaid.getItems().add(new MenuItem(s)));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            itemtablaid.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
                MainApplication.setTabla(menuItem.getText());
            itemtablaid.setText("Tabla: "+menuItem.getText());
            }));
        }
    }

    //crea la tabla y añade los valores a esta
    private void importarficheroCTabla(String tabla,String path) throws SQLException, IOException {
        if(MainApplication.getDB().getType().equals("mysql")){
            Connection con = MainApplication.getDB().getConexion();
            File file = new File(path);
            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader bf = new BufferedReader(fr);

            // Leer la primera línea para obtener los nombres de las columnas
            String cabecera = null;
            try {
                cabecera = bf.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] columnas = cabecera.split(",");

            // Crear la consulta de creación de la tabla
            StringBuilder createQuery = new StringBuilder();
            createQuery.append("CREATE TABLE ");
            createQuery.append(tabla);
            createQuery.append(" (");
            for (int i = 0; i < columnas.length; i++) {
                createQuery.append(columnas[i]);
                createQuery.append(" VARCHAR(255)");
                if (i < columnas.length - 1) {
                    createQuery.append(", ");
                }
            }
            createQuery.append(")");

            // Ejecutar la consulta de creación de la tabla
            Statement createStatement = con.createStatement();
            createStatement.executeUpdate(createQuery.toString());

            // Crear la consulta de inserción de los datos
            StringBuilder insertQuery = new StringBuilder();
            insertQuery.append("INSERT INTO ");
            insertQuery.append(tabla);
            insertQuery.append(" (");
            for (int i = 0; i < columnas.length; i++) {
                insertQuery.append(columnas[i]);
                if (i < columnas.length - 1) {
                    insertQuery.append(", ");
                }
            }
            insertQuery.append(") VALUES (");
            for (int i = 0; i < columnas.length; i++) {
                insertQuery.append("?");
                if (i < columnas.length - 1) {
                    insertQuery.append(", ");
                }
            }
            insertQuery.append(")");

            // Preparar la consulta de inserción
            PreparedStatement insertStatement = con.prepareStatement(insertQuery.toString());

            // Leer las líneas restantes del archivo y realizar las inserciones
            String line;
            while ((line = bf.readLine()) != null) {
                String[] datos = line.split(",");
                for (int i = 0; i < columnas.length; i++) {
                    insertStatement.setString(i + 1, datos[i]);
                }
                insertStatement.executeUpdate();
            }

            // Cerrar recursos y mostrar mensaje de éxito
            insertStatement.close();
            createStatement.close();
            bf.close();
            fr.close();

            System.out.println("Tabla creada y datos insertados exitosamente.");
        }
        else{
            File file = new File(path);
            FileReader fr;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader bf = new BufferedReader(fr);

            try {
                // Leer la primera línea para obtener el número de columnas y los nombres
                String cabecera = bf.readLine();
                // Crear la consulta de creación de la tabla de manera dinámica
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE ");
                sb.append(tabla);
                sb.append(" (");
                String[] columnas = cabecera.split(",");
                int numColumnas = columnas.length;
                for (int i = 0; i < numColumnas; i++) {
                    sb.append(columnas[i]);
                    sb.append(" VARCHAR(255)");
                    if (i < columnas.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
                String createQuery = sb.toString();

                // Ejecutar la consulta de creación de la tabla
                Connection conn = MainApplication.getDB().getConexion();
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(createQuery);


                // Crear la consulta de inserción de manera dinámica
                sb = new StringBuilder();
                sb.append("INSERT INTO ");
                sb.append(tabla);
                sb.append(" (");
                for (int i = 0; i < numColumnas; i++) {
                    sb.append(columnas[i]);
                    if (i < numColumnas - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(") VALUES (");
                for (int i = 0; i < numColumnas; i++) {
                    sb.append("?");
                    if (i < numColumnas - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
                String insertQuery = sb.toString();
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);

                // Recorrer las líneas del archivo y asignar los valores a la consulta de inserción
                String line = bf.readLine();
                while (line != null) {
                    String[] datos = line.split(",");
                    for (int i = 0; i < numColumnas; i++) {
                        pstmt.setString(i + 1, datos[i]);
                    }
                    pstmt.executeUpdate();
                    line = bf.readLine();
                }

                pstmt.close();
                conn.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tabla creada");
                alert.setContentText("La tabla se ha creado existosamente");
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    //al pulsar el boton se iniciara el proceso de importacion dependiendo del radiobutton elegido
    public void realizaraccionimportar() throws IOException {
        Inicio inicio = new Inicio();
        if(item==1){
            importarFicheroBD(path);
        }
        else if(item==2){
            try {
                importarficheroCTabla(MainApplication.getTabla(),path);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            MainApplication.cambiarEscena("FXML/inicio.fxml");
            inicio.refresh();

        }
        else if (item==3) {
            insertaritems(MainApplication.getTabla(),path);
            MainApplication.cambiarEscena("FXML/inicio.fxml");
            inicio.refresh();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("no has seleccionado el modo de importaciòn");
            alert.show();
        }
    }

    //crea dialogo para que el usuario ponga nombre a la tabla nueva que creaara
    public String crearDialogoNombreTabla() {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Crear nueva tabla");
        dialogo.setHeaderText("Introduzca un nombre para la nueva tabla");
        dialogo.setContentText("Nombre:");

        Optional<String> resultado = dialogo.showAndWait();

        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            return null;
        }
    }


    //inserta valores en una tabla existente
    private void insertaritems(String tabla, String path) {
        Connection c = MainApplication.getDB().getConexion();
        List<String[]> columnas;
        try {
            columnas = MainApplication.getDB().getColumnasTabla(tabla);
            StringBuilder ppp = new StringBuilder("INSERT INTO " + tabla + " (");
            for (int i = 0; i < columnas.size(); i++) {
                ppp.append(columnas.get(i)[0]);
                if (i != columnas.size() - 1) {
                    ppp.append(", ");
                }
            }
            ppp.append(") VALUES (?");
            for (int i = 0; i < columnas.size() - 1; i++) {
                ppp.append(",?");
            }
            ppp.append(")");

            PreparedStatement ps = c.prepareStatement(ppp.toString());

            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                String[] valores = line.split(",");
                for (int i = 0; i < valores.length; i++) {
                    String tipoDato = columnas.get(i)[1];
                    switch (tipoDato) {
                        case "integer":
                            ps.setInt(i + 1, Integer.parseInt(valores[i]));
                            break;
                        case "float":
                            ps.setFloat(i + 1, Float.parseFloat(valores[i]));
                            break;
                        case "boolean":
                            ps.setBoolean(i + 1, Boolean.parseBoolean(valores[i]));
                            break;
                        case "date":
                            ps.setDate(i + 1, Date.valueOf(valores[i]));
                            break;
                        case "double precision":
                            ps.setDouble(i + 1, Double.parseDouble(valores[i]));
                            break;
                        default:
                            ps.setString(i + 1, valores[i]);
                            break;
                    }
                }
                ps.executeUpdate();
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}

