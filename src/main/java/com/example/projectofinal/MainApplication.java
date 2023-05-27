package com.example.projectofinal;

import com.example.projectofinal.Clases.Funciones.BasedeDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        private static String estiloActual = "CSS/azul.css";
        private static String fxml;

    /**
     * Obtener el Estilo (CSS) de la Escena
     * @return estilo actual es la ruta al fichero css
     */
    public static String getEstiloActual() {
        return estiloActual;
    }

    /**
     * Obtener la escena en la que estamos (para la ayuda al usuario)
     * @return fxml es la ruta a la escena
     */
    public static String getFxml() {
        return fxml;
    }


    /**
     * Obtener la escena
     * @return pantalla principal es la escena principal
     */
    public static Stage getPantallaprincipal() {
        return pantallaprincipal;
    }

    /**
     * Guardamos la base de datos como variable estatica para que no se pierda entre escenas
     * @param bd es la base de datos para que la guardemos en estatico
     */
    public static void setDB(BasedeDatos bd) {
        basedeDatos=bd;
    }

    /**
     * Devuelve la base de datos
     * @return devuelve la base de datos
     */
    public static BasedeDatos getDB() {
        return basedeDatos;
    }

    /**
     * Función que cambia el estilo de la app
     * @param s es la ruta al fichero css para cargarlo
     */
    public static void cambiarEstilo(String s) {
        pantallaprincipal.getScene().getStylesheets().clear();
        pantallaprincipal.getScene().getStylesheets().add((MainApplication.class.getResource(s)).toExternalForm());
        estiloActual=s;
    }

    /**
     * Función que elimina los estilos de la app
     */
    public static void deleteStyle(){
        estiloActual="CSS/sinstylo.css";
        pantallaprincipal.getScene().getStylesheets().clear();
    }

    /**
     * Función que elimina una base de datos de la lista y guarda los cambios.
     * @param bd es la base de datos a eliminar
     */
    public static void deleteBD(BasedeDatos bd){
        bdlist.remove(bd);
        try {
            guardarConexiones();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Metodo principal de la app, que hace que se lance la escena y podamos verla.
     * @param stage es la escena principal
     * @throws IOException excepción al cargar un fichero
     */
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

    /**
     * Metodo que hace que se lance la aplicación.
     * @param args
     */
        public static void main(String[] args) {
            launch();
        }

    /**
     * Función que hace que se cambie la escena que vemos (navegación)
     * @param escena
     * @throws IOException
     */
    public static void cambiarEscena(String escena) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(escena));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add((MainApplication.class.getResource(estiloActual)).toExternalForm());
        pantallaprincipal.setScene(scene);
        fxml = escena;
    }

    /**
     * Función que guarda la lista de BD
     * @throws IOException expeción al leer de un fichero
     */
    public static void Guardar() throws IOException {
           guardarConexiones();
    }

    /**
     * Devuelve la escena principal
     * @return la escena principal
     */
    public static Stage getStage(){
            return pantallaprincipal;
    }

    /**
     * Devuelve el nombre de la tabla
     * @return tabla
     */
    public static String getTabla(){
        return tabla;
    }

    /**
     * Guarda el nombre de la tabla para que no se pierda al cambiar de escena
     * @param t es el nombre de la tabla
     */
    public static void setTabla(String t) {
        tabla=t;
    }

    /**
     * Función para añadir una base de datos a la lista
     * @param bd añade una base de datos a la lista
     */
    public static void addBD(BasedeDatos bd){
        bdlist.add(bd);
    }

    /**
     * Función que devuelve la lista de bases de datos
     * @return devuelve la lista de Bases de Datos
     */

    public static List<BasedeDatos> getBduser() {
        return bdlist;
    }

    /**
     * Metodo que lee el fichero para obtener los datos de las connexiones anteriores
     * @throws IOException excepción al leer un fichero
     * @throws ClassNotFoundException excepción al leer una clase
     */
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

    /**
     * Función que guarda en el fichero connections.by las connexiones
     * @throws IOException excepcion al leer fichero
     */
    public static void guardarConexiones() throws IOException {
        File file = new File(MainApplication.class.getResource("connections.by").getFile());
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bdlist);
        oos.close();
        fos.close();
    }

    /**
     * Función para obtener una Base de Datos a traves de un String
     * @param s toString de la Base de Datos
     * @return bd donde s y su metodo @toString sea igual
     */
    public static BasedeDatos getBD(String s) {
        Optional<BasedeDatos> bd = bdlist.stream()
                .filter(basedeDatos -> basedeDatos.toString().equals(s))
                .findFirst();
        System.out.println(bd.toString());
        return bd.orElse(null);
    }

}