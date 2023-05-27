package com.example.projectofinal.Clases.Personalizacion;

import com.example.projectofinal.DataMasterController;
import com.example.projectofinal.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;

public class ImportCss extends DataMasterController{
    @FXML
    private Text ruta;
    private File archivoEstilo;

    /**
     * Función que permite importar un fichero con filechooser
     */
    public void importarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo de estilo");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos CSS (*.css)", "*.css");
        fileChooser.getExtensionFilters().add(extFilter);


        archivoEstilo = fileChooser.showOpenDialog(null);

        if (archivoEstilo != null) {
            ruta.setText("ruta: "+archivoEstilo.getAbsolutePath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Archivo importado");
            alert.setContentText("El archivo de estilo ha sido importado correctamente.");
            alert.showAndWait();
        }
    }

    /**
     * Función que permite poder probar el estilo antes de guardarlo
     */
    public void probarEstilo() {
        if (archivoEstilo != null) {
            String estilo = archivoEstilo.toURI().toString();
            MainApplication.getPantallaprincipal().getScene().getStylesheets().add((estilo));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Estilo aplicado");
            alert.setContentText("El estilo ha sido aplicado correctamente para la escena actual.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Archivo no seleccionado");
            alert.setContentText("No se ha seleccionado ningún archivo de estilo.");
            alert.showAndWait();
        }
    }

    /**
     * Función que guarda el archivo css en el sistema(llama a copyFile)
     */
    public void guardarEstilo() {
        if (archivoEstilo != null) {
            File directorioApp = new File(System.getProperty("user.dir"));
            String nombreArchivo = "src/main/resources/com/example/projectofinal/CSS/"+archivoEstilo.getName();
            File archivoDestino = new File(directorioApp, nombreArchivo);

            try {
                copyFile(archivoEstilo, archivoDestino,archivoEstilo.getName());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Estilo guardado");
                alert.setContentText("El archivo de estilo ha sido guardado correctamente en la aplicación.");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al guardar el estilo");
                alert.setContentText("Se ha producido un error al guardar el archivo de estilo.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Archivo no seleccionado");
            alert.setContentText("No se ha seleccionado ningún archivo de estilo.");
            alert.showAndWait();
        }
    }

    /**
     * Función que realiza la logica de almacenar en el sistema el fichero
     * @param sourceFile Ruta al fichero
     * @param destFile Ruta de destino del fichero
     * @param name nombre
     * @throws IOException Excepción al leer un fichero
     */
    private void copyFile(File sourceFile, File destFile,String name) throws IOException {
        try (InputStream is = new FileInputStream(sourceFile);
             OutputStream os = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            MainApplication.cambiarEstilo("CSS/"+name);
        }
    }

    /**
     * Función que permite cambiar la escena
     * @throws IOException Excepción al leer un fichero
     */
    public void cancelar() throws IOException {
        MainApplication.cambiarEscena("cambiarstylocss.fxml");
    }
}
