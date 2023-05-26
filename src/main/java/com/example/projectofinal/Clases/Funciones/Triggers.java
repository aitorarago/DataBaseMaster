package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.Clases.Personalizacion.ItemList;
import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Triggers extends DataMasterController implements Initializable {
    @FXML
    private ImageView delete;
    @FXML
    private ImageView addid;
    @FXML
    private ImageView editid;
    @FXML
    private ImageView showid;
    @FXML
    private SplitMenuButton bdid;
    @FXML
    private SplitMenuButton tbid;
    @FXML
    private ListView listid;
    @FXML
    private TextArea txtid;
    @FXML
    private Button saveid;
    private int index;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarBD();
        anadirToastIcono();
        tbid.setVisible(false);
        index = listid.getSelectionModel().getSelectedIndex();
    }

    private void anadirToastIcono() {
        //añadir funcion
        String at = "Crear Disparador";
        Tooltip tooltip1 = new Tooltip(at);
        tooltip1.setShowDuration(Duration.millis(5000));
        tooltip1.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(addid, tooltip1);
        //elimiar funcion
        String a = "Eliminar Disparador";
        Tooltip tooltip = new Tooltip(a);
        tooltip.setShowDuration(Duration.millis(5000));
        tooltip.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(delete, tooltip);
        //editar funcion
        String att = "Editar Disparador";
        Tooltip tooltip2 = new Tooltip(att);
        tooltip2.setShowDuration(Duration.millis(5000));
        tooltip2.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(editid, tooltip2);
        //Visualizar BD
        String ar = "Ver el Codigo Disparador";
        Tooltip tooltipp = new Tooltip(ar);
        tooltipp.setShowDuration(Duration.millis(5000));
        tooltipp.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        Tooltip.install(showid, tooltipp);
    }

    private void cargarBD() {
        bdid.setText("Selecciona una opción");
        MainApplication.getBduser().forEach(basedeDatos -> bdid.getItems().add(new MenuItem(basedeDatos.toString())));
        bdid.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
            MainApplication.setDB(MainApplication.getBD(menuItem.getText()));
            bdid.setText("Base de datos seleccionada: " + menuItem.getText()); // Actualiza el título con el valor seleccionado
            visualizartablas();
        }));
    }

    private void visualizartablas() {
        tbid.setVisible(true);
        tbid.setText("Selecciona una opción");
        try {
            MainApplication.getDB().getTablas().forEach(s -> tbid.getItems().add(new MenuItem(s)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tbid.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> {
            MainApplication.setTabla(menuItem.getText());
            tbid.setText("Tabla: " + menuItem.getText());
            try {
                anadirListaTriggers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private void anadirListaTriggers() throws SQLException {
        if (MainApplication.getDB().getType().equals("postgresql")) {
            listid.getItems().clear();
            Connection con = MainApplication.getDB().getConexion();
            // Suponiendo que ya tienes una conexión a la base de datos establecida
            String query = "SELECT tgname FROM pg_trigger WHERE tgrelid = '" + MainApplication.getTabla() + "'::regclass;";
            CallableStatement stmt = con.prepareCall(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String triggerName = rs.getString("tgname");
                if (!rs.getString("tgname").contains("RI_ConstraintTrigger")) {
                    listid.getItems().add(triggerName);
                }

            }

            listid.setCellFactory(stringListView -> new ItemList());
            listid.setOnMouseClicked(event -> {
                index = listid.getSelectionModel().getSelectedIndex();
            });
            listid.refresh();
        } else {
            listid.getItems().clear();
            Connection con = MainApplication.getDB().getConexion();
// Suponiendo que ya tienes una conexión a la base de datos establecida
            String query = "SELECT trigger_name FROM information_schema.triggers WHERE event_object_table = '" + MainApplication.getTabla() + "'";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String triggerName = rs.getString("trigger_name");
                    listid.getItems().add(triggerName);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            listid.setCellFactory(stringListView -> new ItemList());
            listid.setOnMouseClicked(event -> {
                index = listid.getSelectionModel().getSelectedIndex();
            });
            listid.refresh();
            listid.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    index = listid.getSelectionModel().getSelectedIndex();
                    txtid.clear();
                    try {
                        visualizartriggeer();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

    }

    public void deletetrigger() throws SQLException {
        if(MainApplication.getDB().getType().equals("postgresql")) {
            Connection con = MainApplication.getDB().getConexion();
            Statement st = con.createStatement();
            String dropTriggerSql = "DROP TRIGGER " + listid.getItems().get(index) + " ON " + MainApplication.getTabla() + ";";
            st.executeUpdate(dropTriggerSql);
            anadirListaTriggers();
        }
        else {
            Connection con = MainApplication.getDB().getConexion();
            Statement st = con.createStatement();
            String dropTriggerSql2 = "DROP TRIGGER IF EXISTS " + listid.getItems().get(index);
            st.executeUpdate(dropTriggerSql2);
            anadirListaTriggers();

        }
    }

    public void addtrigger(MouseEvent mouseEvent) throws SQLException {
        txtid.setEditable(true);
        saveid.setDisable(false);
        saveid.setVisible(true);
        if(MainApplication.getDB().getType().equals("postgresql")) {

            txtid.clear();
            String sql = "CREATE OR REPLACE FUNCTION ejemplo() RETURNS TRIGGER AS $$ " +
                    "BEGIN " +
                    "NEW.modified = now(); " +
                    "RETURN NEW; " +
                    "END; " +
                    "$$ LANGUAGE 'plpgsql';";
            txtid.setText(sql);
        }
        else {
            String sql = "CREATE TRIGGER validar_precio_pizza\n" +
                    "BEFORE INSERT ON pizzas\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    IF NEW.precio < 12 THEN\n" +
                    "        SIGNAL SQLSTATE '45000'\n" +
                    "        SET MESSAGE_TEXT = 'El precio de la pizza no puede ser inferior a 12';\n" +
                    "    END IF;\n" +
                    "END;";
            txtid.setText(sql);
        }
        saveid.setOnAction(actionEvent -> {
            Connection conn = MainApplication.getDB().getConexion();
            PreparedStatement stmt = null;
            try {
                System.out.println(txtid.getText());
                stmt = conn.prepareStatement(txtid.getText());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            txtid.setEditable(false);
            saveid.setDisable(true);
            saveid.setVisible(false);
            txtid.clear();
            listid.getItems().clear();
            try {
                anadirListaTriggers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void editTrigger(MouseEvent mouseEvent) throws SQLException {
        visualizartriggeer();
        txtid.setEditable(true);
        saveid.setDisable(false);
        saveid.setVisible(true);

        if(MainApplication.getDB().getType().equals("mysql")){
        saveid.setOnAction(actionEvent -> {
            Connection conn = MainApplication.getDB().getConexion();
            Statement stmt = null;
            try {
                String triggerName = listid.getItems().get(index).toString();
                deletetrigger(triggerName); // Eliminar el trigger existente

                String triggerSql = txtid.getText(); // Obtener el nuevo código del trigger

                stmt = conn.createStatement();
                stmt.executeUpdate(triggerSql); // Crear el nuevo trigger

                txtid.setEditable(false);
                saveid.setDisable(true);
                saveid.setVisible(false);
                txtid.clear();
                listid.getItems().clear();
                anadirListaTriggers();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        }
        else {
            saveid.setOnAction(actionEvent -> {
                Connection conn = MainApplication.getDB().getConexion();
                PreparedStatement stmt = null;
                try {
                    System.out.println(txtid.getText());
                    stmt = conn.prepareStatement(txtid.getText());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                txtid.setEditable(false);
                saveid.setDisable(true);
                saveid.setVisible(false);
                txtid.clear();
                listid.getItems().clear();
                try {
                    anadirListaTriggers();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void deletetrigger(String triggerName) throws SQLException{
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();
        String dropTriggerSql2 = "DROP TRIGGER IF EXISTS " + triggerName;
        st.executeUpdate(dropTriggerSql2);
        anadirListaTriggers();
    }

    public void visualizartriggeer() throws SQLException {
        txtid.setEditable(false);
        saveid.setDisable(true);
        saveid.setVisible(false);
        txtid.clear();
        Connection con = MainApplication.getDB().getConexion();
        Statement st = con.createStatement();
        if (MainApplication.getDB().getType().equals("postgresql")) {
            String sql = "SELECT pg_get_triggerdef(" + listid.getItems().get(index).toString() + ") FROM information_schema.triggers WHERE event_object_table = '" + MainApplication.getTabla() + "';";
            ResultSet sr = st.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            while (sr.next()) {
                sb.append(sr.getString(1));
            }
            txtid.setText(sb.toString());
        } else {
            String triggerName = listid.getItems().get(index).toString();
            String tableName = MainApplication.getTabla();

            String sql = "SELECT action_timing, event_manipulation, action_statement FROM information_schema.triggers WHERE trigger_name = '" + triggerName + "' AND event_object_table = '" + tableName + "'";
            ResultSet rs = st.executeQuery(sql);

            StringBuilder sb = new StringBuilder();
            if (rs.next()) {
                String actionTiming = rs.getString("action_timing");
                String eventManipulation = rs.getString("event_manipulation");
                String actionStatement = rs.getString("action_statement");

                sb.append("CREATE TRIGGER ").append(triggerName).append("\n");
                sb.append(actionTiming).append(" ").append(eventManipulation).append(" ON ").append(tableName).append("\n");
                sb.append("BEGIN\n");
                sb.append(actionStatement).append("\n");
                sb.append("END;\n");
            }

            txtid.setText(sb.toString());
        }
    }
}