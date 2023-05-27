package com.example.projectofinal;

import com.example.projectofinal.Clases.Funciones.Inicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DataMasterController {
    @FXML
    private ImageView infoo;

    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void crearBD()throws IOException{
        MainApplication.cambiarEscena("FXML/crearconectorBD.fxml");
    }

    /**
     * Cambia de una escena a otra, inicializa inicio y llama al metodo refresh
     * @throws IOException excepción al leer un fichero
     */

    public void visualizarBD() throws IOException {
        MainApplication.cambiarEscena("FXML/inicio.fxml");
        Inicio inicio = new Inicio();
        inicio.refresh();
    }

    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void importarBD() throws IOException{
        MainApplication.cambiarEscena("FXML/importarbd.fxml");
    }

    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void exportarBD() throws IOException{
        MainApplication.cambiarEscena("FXML/exportarbd.fxml");
    }

    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void comenzarbtn() throws IOException {
        visualizarBD();
    }

    /**
     * Cierra la app
     */
    public void salir() {
        MainApplication.getStage().close();
    }

    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void funciones()throws IOException {
        MainApplication.cambiarEscena("FXML/funciones.fxml");
    }
    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void disparadores() throws IOException{
        MainApplication.cambiarEscena("FXML/triggers.fxml");
    }
    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void moodstyle() throws IOException{
        MainApplication.cambiarEscena("FXML/cambiarstylocss.fxml");
    }
    /**
     * Cambia de una escena a otra
     * @throws IOException excepción al leer un fichero
     */
    public void importarCSS() throws IOException{
        MainApplication.cambiarEscena("FXML/importarcss.fxml");
    }
    /**
     * Muestra información sobre las escenas donde nos encontramos
     * @throws IOException excepción al leer un fichero
     */
    @FXML
    public void infoFXML() {
        Stage dialog = new Stage();
        dialog.setTitle("Información");
        dialog.setHeight(300);
        dialog.setWidth(400);
        Text tit = new Text();
        Text txt = new Text();
        VBox vBox = new VBox();


        switch (MainApplication.getFxml()){
            case "FXML/crearconectorBD.fxml":
                tit.setText("Añadir conector:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("Para añadir un conector, necesitamos una base de datos\n" +
                        "también un usuario y una contraseña. Además de saber nuestra ip, \n" +
                        "siendo esta 'localhost' si la BBDD esta en nuestro pc,\n" +
                        "y saber el puerto por el que la app gestora escucha peticiones.\n" +
                        "Además de especificar esta app, ya que la configuración para realizar\n" +
                        "las funciones CRUD varian.");
                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                break;
            case "FXML/exportarbd.fxml":
                tit.setText("Exportar:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("Para la exportación de datos, necesitamos primero saber que queremos exportar,\n" +
                        "también hay que saber de que BD hablamos. Para hacer una backup, no necesitaremos\n" +
                        "mas información que la ruta donde exportar el archivo, pero para poder exportar \n" +
                        "una tabla, si que necesitaremos que saber de que tabla se trata.");
                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                break;

            case "FXML/funciones.fxml":
                tit.setText("Funciones:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("Para la gestión de funciones en DataBaseMAster, hemos optado por\n" +
                        "la realización de la funcion CRUD:\n");
                HBox hBoxf = new HBox();
                ImageView imageViewf = new ImageView(MainApplication.class.getResource("IMG/basura.png").toExternalForm());
                Text txf = new Text("Eliminar: con este icono podemos eliminar connectores");
                imageViewf.setFitWidth(35);
                imageViewf.setFitHeight(35);
                HBox hBox1f = new HBox();
                ImageView imageView1f = new ImageView(MainApplication.class.getResource("IMG/boton-agregar.png").toExternalForm());
                Text tx1f = new Text("Añadir: con este icono podemos añadir una nueva funcion,\n" +
                        "el poder añadir una es un poco complejo, he dejado un ejemplo\n" +
                        "para que podais usar la misma sintaxi para crear vuestra funcion.");
                imageView1f.setFitWidth(35);
                imageView1f.setFitHeight(35);
                HBox hBox2f = new HBox();
                ImageView imageView2f = new ImageView(MainApplication.class.getResource("IMG/vision.png").toExternalForm());
                Text tx2f = new Text("Vizualizar: con este icono podemos visualizar las tablas \ny los valores de estos");
                imageView2f.setFitWidth(35);
                imageView2f.setFitHeight(35);
                HBox hBox3f = new HBox();
                ImageView imageView3f = new ImageView(MainApplication.class.getResource("IMG/editar.png").toExternalForm());
                Text tx3f = new Text("Editar: con este icono podemos editar el codigo de la funcion");
                imageView3f.setFitWidth(35);
                imageView3f.setFitHeight(35);
                HBox hBox4f = new HBox();
                ImageView imageView4f = new ImageView(MainApplication.class.getResource("IMG/tocar.png").toExternalForm());
                Text tx4f = new Text("Ejecutar: con este icono podemos ejecutar la función");
                imageView4f.setFitWidth(35);
                imageView4f.setFitHeight(35);

                hBoxf.getChildren().add(imageViewf);
                hBoxf.getChildren().add(txf);
                hBox1f.getChildren().add(imageView1f);
                hBox1f.getChildren().add(tx1f);
                hBox2f.getChildren().add(imageView2f);
                hBox2f.getChildren().add(tx2f);
                hBox3f.getChildren().add(imageView3f);
                hBox3f.getChildren().add(tx3f);
                hBox4f.getChildren().add(imageView4f);
                hBox4f.getChildren().add(tx4f);

                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                vBox.getChildren().add(hBox1f);
                vBox.getChildren().add(hBoxf);
                vBox.getChildren().add(hBox3f);
                vBox.getChildren().add(hBox2f);
                vBox.getChildren().add(hBox4f);

                break;

            case "FXML/importarbd.fxml":
                tit.setText("Importar:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("Para la importación de datos, necesitamos primero saber el fichero de\n" +
                        "importación, seguidamente indicamos que hacer con este fichero,\n" +
                        "si elegimos la pción de importar table nueva, tendremos que \n" +
                        "especificar el nombre, y si por el contrario importamos valores\n" +
                        "tenemos que especificar sobre que tabla los inertamos.");
                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                break;

            case "FXML/importarcss.fxml":
                tit.setText("Importar Estilo:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("Para importar un estilo, necesitamos un indicar que fichero queremos\n" +
                        "de nuestro sistema, una vez seleccionado, tenemos la opción de\n" +
                        "probar este estilo, para ver si es de nuestro agrado, también \n" +
                        "podemos cancelar o guardar este estilo en nuestro sistema.");
                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                break;

            case "FXML/inicio.fxml":
                tit.setText("Inicio de la app:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("En la escena actual, podemos ver la lista de connectores,\n" +
                        "en la parte superior tenemos la barra de navegación por la \n" +
                        "cual podemos movernos por la app, debajo, tenemos las opciones");
                HBox hBox = new HBox();
                ImageView imageView = new ImageView(MainApplication.class.getResource("IMG/basura.png").toExternalForm());
                Text tx = new Text("Eliminar: con este icono podemos eliminar connectores");
                imageView.setFitWidth(35);
                imageView.setFitHeight(35);
                HBox hBox1 = new HBox();
                ImageView imageView1 = new ImageView(MainApplication.class.getResource("IMG/boton-agregar.png").toExternalForm());
                Text tx1 = new Text("Añadir: con este icono podemos añadir connectores");
                imageView1.setFitWidth(35);
                imageView1.setFitHeight(35);
                HBox hBox2 = new HBox();
                ImageView imageView2 = new ImageView(MainApplication.class.getResource("IMG/vision.png").toExternalForm());
                Text tx2 = new Text("Vizualizar: con este icono podemos visualizar las tablas \ny los valores de estos");
                imageView2.setFitWidth(35);
                imageView2.setFitHeight(35);
                hBox.getChildren().add(imageView);
                hBox.getChildren().add(tx);
                hBox1.getChildren().add(imageView1);
                hBox1.getChildren().add(tx1);
                hBox2.getChildren().add(imageView2);
                hBox2.getChildren().add(tx2);

                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBox1);
                vBox.getChildren().add(hBox2);
                break;

            case "FXML/triggers.fxml":
                tit.setText("Disparadores:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("""
                        Los disparadores son funciones especificas de las tablas
                        que permiten la gestión de restricciones sobre las tablas.CRUD triggers :
                        """);
                HBox hBoxt = new HBox();
                ImageView imageViewt = new ImageView(MainApplication.class.getResource("IMG/basura.png").toExternalForm());
                Text txtt = new Text("Eliminar: con este icono podemos eliminar disparadores");
                imageViewt.setFitWidth(35);
                imageViewt.setFitHeight(35);
                HBox hBox1t = new HBox();
                ImageView imageView1t = new ImageView(MainApplication.class.getResource("IMG/boton-agregar.png").toExternalForm());
                Text tx1t = new Text("""
                        Añadir: con este icono podemos añadir un nuevo disparador,
                        el poder añadir una es un poco complejo, he dejado un ejemplo
                        para que podais usar la misma sintaxi para crear vuestro disparador.""");
                imageView1t.setFitWidth(35);
                imageView1t.setFitHeight(35);
                HBox hBox2t = new HBox();
                ImageView imageView2t = new ImageView(MainApplication.class.getResource("IMG/vision.png").toExternalForm());
                Text tx2t = new Text("Vizualizar: con este icono podemos visualizar el codigo");
                imageView2t.setFitWidth(35);
                imageView2t.setFitHeight(35);
                HBox hBox3t = new HBox();
                ImageView imageView3t = new ImageView(MainApplication.class.getResource("IMG/editar.png").toExternalForm());
                Text tx3t = new Text("Editar: con este icono podemos editar el codigo del disparador");
                imageView3t.setFitWidth(35);
                imageView3t.setFitHeight(35);

                hBoxt.getChildren().add(imageViewt);
                hBoxt.getChildren().add(txtt);
                hBox1t.getChildren().add(imageView1t);
                hBox1t.getChildren().add(tx1t);
                hBox2t.getChildren().add(imageView2t);
                hBox2t.getChildren().add(tx2t);
                hBox3t.getChildren().add(imageView3t);
                hBox3t.getChildren().add(tx3t);

                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                vBox.getChildren().add(hBoxt);
                vBox.getChildren().add(hBox1t);
                vBox.getChildren().add(hBox3t);
                vBox.getChildren().add(hBox2t);

                break;

            case "FXML/visualizarTabla.fxml":
                tit.setText("Visualizar Datos:\n");
                tit.setStyle("-fx-font-size: 20px;");
                Text titsub = new Text();
                titsub.setText("Sobre tablas:\n");
                titsub.setStyle("-fx-font-size: 16px;");
                txt.setText("Esta es la pantalla principal de DataBaseMAster, con la cual\n" +
                        "puedes gestionar la base de datos.");
                HBox hBoxv = new HBox();
                ImageView imageViewv = new ImageView(MainApplication.class.getResource("IMG/basura.png").toExternalForm());
                Text txtv = new Text("Eliminar: con este icono podemos eliminar una tabla");
                imageViewv.setFitWidth(35);
                imageViewv.setFitHeight(35);
                HBox hBox1v = new HBox();
                ImageView imageView1v = new ImageView(MainApplication.class.getResource("IMG/boton-agregar.png").toExternalForm());
                Text tx1v = new Text("Añadir: con este icono podemos añadir una nueva tabla a nuestra BBDD");
                imageView1v.setFitWidth(35);
                imageView1v.setFitHeight(35);
                HBox hBox2v = new HBox();
                ImageView imageView2v = new ImageView(MainApplication.class.getResource("IMG/vision.png").toExternalForm());
                Text tx2v = new Text("Vizualizar: con este icono podemos visualizar el contenido de la tabla");
                imageView2v.setFitWidth(35);
                imageView2v.setFitHeight(35);
                HBox hBox3v = new HBox();
                ImageView imageView3v = new ImageView(MainApplication.class.getResource("IMG/editar.png").toExternalForm());
                Text tx3v = new Text("Editar: con este icono podemos editar el nombre de las columnas");
                imageView3v.setFitWidth(35);
                imageView3v.setFitHeight(35);
                HBox hBox4v = new HBox();
                ImageView imageView4v = new ImageView(MainApplication.class.getResource("IMG/buscar.png").toExternalForm());
                Text tx4v = new Text("Consultar: con este icono podemos realizar una consulta sobre la BBDD");
                imageView4v.setFitWidth(35);
                imageView4v.setFitHeight(35);
                HBox hBox5v = new HBox();
                ImageView imageView5v = new ImageView(MainApplication.class.getResource("IMG/terminal.png").toExternalForm());
                Text tx5v = new Text("Terminal: con este icono podemos escribir directamente lo que queramos\n" +
                        "al terminal de la base de datos.");
                imageView5v.setFitWidth(35);
                imageView5v.setFitHeight(35);

                hBoxv.getChildren().add(imageViewv);
                hBoxv.getChildren().add(txtv);
                hBox1v.getChildren().add(imageView1v);
                hBox1v.getChildren().add(tx1v);
                hBox2v.getChildren().add(imageView2v);
                hBox2v.getChildren().add(tx2v);
                hBox3v.getChildren().add(imageView3v);
                hBox3v.getChildren().add(tx3v);
                hBox4v.getChildren().add(imageView4v);
                hBox4v.getChildren().add(tx4v);
                hBox5v.getChildren().add(imageView5v);
                hBox5v.getChildren().add(tx5v);


                Text titsub1 = new Text();
                titsub1.setText("Sobre valores:\n");
                titsub1.setStyle("-fx-font-size: 16px;");

                HBox hBoxvv = new HBox();
                ImageView imageViewvv = new ImageView(MainApplication.class.getResource("IMG/basura.png").toExternalForm());
                Text txtvv = new Text("Eliminar: con este icono podemos eliminar una fila de la tabla");
                imageViewvv.setFitWidth(35);
                imageViewvv.setFitHeight(35);
                HBox hBox1vv = new HBox();
                ImageView imageView1vv = new ImageView(MainApplication.class.getResource("IMG/boton-agregar.png").toExternalForm());
                Text tx1vv = new Text("Añadir: con este icono podemos añadir una nueva vila a nuestras tablas");
                imageView1vv.setFitWidth(35);
                imageView1vv.setFitHeight(35);

                hBoxvv.getChildren().add(imageViewvv);
                hBoxvv.getChildren().add(txtvv);
                hBox1vv.getChildren().add(imageView1vv);
                hBox1vv.getChildren().add(tx1vv);

                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                vBox.getChildren().add(titsub);
                vBox.getChildren().add(txtv);
                vBox.getChildren().add(hBoxv);
                vBox.getChildren().add(hBox1v);
                vBox.getChildren().add(hBox3v);
                vBox.getChildren().add(hBox2v);
                vBox.getChildren().add(titsub1);
                vBox.getChildren().add(hBoxvv);
                vBox.getChildren().add(hBox1vv);

                break;

            case "FXML/cambiarstylocss.fxml":
                tit.setText("Cambiar estilo:\n");
                tit.setStyle("-fx-font-size: 20px;");
                txt.setText("""
                        Puedes cambiar el estilo de la interfaz desde aqui, esto
                        permite personalizar la app a tu gusto, hay 6 diferentes estilos
                        preedefinidos, pero existe la posibilidad de importar el tuyo
                        peronalizado por ti.""");
                vBox.getChildren().add(tit);
                vBox.getChildren().add(txt);
                break;

        }
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainApplication.getEstiloActual());
        dialog.setScene(scene);
        dialog.setHeight(300);
        dialog.setWidth(500);
        dialog.show();
    }
    /**
     * Muestra información sobre los desarrolladores
     * @throws IOException excepción al leer un fichero
     */
    public void infoApp() {
        Stage st = new Stage();
        st.setTitle("Sobre nosotros");

        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        // Crear el contenido de la información
        Text title = new Text("DataBaseMaster");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Text developer1 = new Text("IES PUIG CASTELLAR, trabajo final CFGS DAM");
        Text developer2 = new Text("Aitor Aragó Janssens");
        Text developer3 = new Text("05/2023");

        // Agregar el contenido al VBox
        root.getChildren().addAll(title, developer1, developer2, developer3);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(root, 300, 200);
        st.setScene(scene);
        st.show();
    }

}