package com.example.projectofinal;

import com.example.projectofinal.Clases.Funciones.BasedeDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainApplication extends Application {
        private static Stage pantallaprincipal;
        private static BasedeDatos basedeDatos;
        private static String tabla;

        private static List<BasedeDatos> bdlist;

    public static String getEstiloActual() {
        return estiloActual;
    }

    private static String estiloActual = "CSS/azul.css";


    public static String getFxml() {
        return fxml;
    }

    private static String fxml;

    public static Stage getPantallaprincipal() {
        return pantallaprincipal;
    }

    public static void setDB(BasedeDatos bd) {
        basedeDatos=bd;
    }

    public static BasedeDatos getDB() {
        return basedeDatos;
    }

    public static void cambiarEstilo(String s) {
        pantallaprincipal.getScene().getStylesheets().clear();
        pantallaprincipal.getScene().getStylesheets().add((MainApplication.class.getResource(s)).toExternalForm());
        estiloActual=s;
    }
    public static void deleteStyle(){
        estiloActual="CSS/sinstylo.css";
        pantallaprincipal.getScene().getStylesheets().clear();
    }
    public static void deleteBD(BasedeDatos bd){
        bdlist.remove(bd);
        try {
            guardarConexiones();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
        public void start(Stage stage) throws IOException {
            pantallaprincipal=stage;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("FXML/Bienvenido.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("DataBaseMaster");
            stage.show();
            bdlist=new ArrayList<>();
        }

        public static void main(String[] args) {
            launch();
        }

    public static void cambiarEscena(String escena) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(escena));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add((MainApplication.class.getResource(estiloActual)).toExternalForm());
        pantallaprincipal.setScene(scene);
        fxml = escena;
    }
    public static void Guardar() throws IOException {
           guardarConexiones();
    }
    public static Stage getStage(){
            return pantallaprincipal;
    }

    public static String getTabla(){
        return tabla;
    }
    public static void setTabla(String t) {
        tabla=t;
    }
    public static void addBD(BasedeDatos bd){
        bdlist.add(bd);
    }




    public static List<BasedeDatos> getBduser() {
        return bdlist;
    }

    public static void inicio() throws IOException, ClassNotFoundException {
        bdlist.clear();
        File file = new File(MainApplication.class.getResource("connections.by").getFile());
        FileInputStream fis = new FileInputStream(file);
        if(file.length()!=0){
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println(1);

            Object objetoLeido = ois.readObject();

            if (objetoLeido instanceof List) {
                List<BasedeDatos> listaConexion = (List<BasedeDatos>) objetoLeido;
                listaConexion.forEach(basedeDatos -> bdlist.add(basedeDatos));
            }

            ois.close();
            fis.close();
        }
    }
    public static void guardarConexiones() throws IOException {
        File file = new File(MainApplication.class.getResource("connections.by").getFile());
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bdlist);
        oos.close();
        fos.close();
    }
    public static BasedeDatos getBD(String s) {
        Optional<BasedeDatos> bd = bdlist.stream()
                .filter(basedeDatos -> basedeDatos.toString().equals(s))
                .findFirst();
        System.out.println(bd.toString());
        return bd.orElse(null);
    }

}