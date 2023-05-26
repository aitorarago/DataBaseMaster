package com.example.projectofinal.Clases.Funciones;

import com.example.projectofinal.MainApplication;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BasedeDatos implements Serializable {
    private final String url;
    private final String username;
    private final String passwrd;
    private transient Connection conexion;
    private final String nameBD;
    private String puerto;
    private String ip;
    private String type;
    public BasedeDatos(String ip, String puerto, String nomBd, String username, String passwrd,String type){
        this.type=type;
        this.url="jdbc:"+type+"://"+ip+":"+puerto+"/"+nomBd;
        this.username=username;
        this.passwrd=passwrd;
        nameBD=nomBd;
        conexion=null;
        this.puerto=puerto;
        this.ip=ip;
    }
    public String getType() {
        return type;
    }

    public Connection getConexion() {
        if (conexion == null) {
            if(type.equals("postgresql")){
            try {
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(url, username, passwrd);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        else {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conexion = DriverManager.getConnection(url, username, passwrd);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
        }
    }
        return conexion;
    }
    public void closeConection(){
        try {
            conexion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<String> getTablas() throws SQLException {
        Statement stmt = getConexion().createStatement();
        List<String> tables = new ArrayList<>();
        if (type.equals("postgresql")) {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String tableName = rs.getString(1);
                System.out.println(tableName);
                tables.add(tableName);
            }
            rs.close();
            stmt.close();

        } else {
            ResultSet resultSet = stmt.executeQuery("SHOW TABLES");

            System.out.println("Tablas en la base de datos:");
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                System.out.println(tableName);
                tables.add(tableName);
            }


        }
        return tables;
    }

    @Override
    public String toString(){
        return nameBD+" "+username;
    }

    public List<String[]> getColumnasTabla(String string) throws SQLException {
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '"+string+"'";
        Statement stmt = getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<String[]> columns = new ArrayList<>();
        while (rs.next()) {
            String columnName = rs.getString(1);
            String dataType = rs.getString(2);
            System.out.println(columnName + " (" + dataType + ")");
            columns.add(new String[] {columnName, dataType});
        }
        rs.close();
        stmt.close();
        return columns;
    }
    public String getTypeColumn(String colum) throws SQLException{
        String sql = "SELECT data_type FROM information_schema.columns WHERE table_name = '"+ MainApplication.getTabla() +"' AND column_name = '"+colum+"';";
        Statement stmt = getConexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()){
            return rs.getString(1);
        }
        return null;
    }


    public void exportBD(String path) throws IOException {
        String ruta = path + "/" + nameBD + ".sql";
        List<String> commands = new ArrayList<>();
        if (type.equals("postgresql")) {
            commands.add("pg_dump");
            commands.add("-h"); //database server host
            commands.add(ip);
            commands.add("-p"); //database server port number
            commands.add(puerto);
            commands.add("-U"); //connect as specified database user
            commands.add(username);
            commands.add("-f"); //output file or directory name
            commands.add(ruta);
            commands.add("-d"); //database name
            commands.add(nameBD);
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.environment().put("PGPASSWORD", passwrd);

            Process process = pb.start();

            try (BufferedReader buf = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line = buf.readLine();
                while (line != null) {
                    System.err.println(line);
                    line = buf.readLine();
                }
            }

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            process.destroy();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación correcta");
            alert.setContentText("Se ha exportado correctamente la BBDD");
            alert.setOnCloseRequest(dialogEvent -> {
                try {
                    MainApplication.cambiarEscena("inicio.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        }
        else{
            commands.add("mysqldump");
            commands.add("--host=" + ip); // database server host
            commands.add("--port=" + puerto); // database server port number
            commands.add("--user=" + username); // connect as specified database user
            commands.add("--password=" + passwrd); // password for database user
            commands.add("--result-file=" + ruta); // output file name
            commands.add("--databases"); // export specific databases
            commands.add(nameBD); // database name

            ProcessBuilder pb = new ProcessBuilder(commands);

            Process process = pb.start();

            try (BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line = buf.readLine();
                while (line != null) {
                    System.err.println(line);
                    line = buf.readLine();
                }
            }

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            process.destroy();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación correcta");
            alert.setContentText("Se ha exportado correctamente la base de datos");
            alert.setOnCloseRequest(dialogEvent -> {
                try {
                    MainApplication.cambiarEscena("inicio.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

    public void importBD(String path) throws IOException {
            String ruta = path;
            List<String> commands = new ArrayList<>();

            if (type.equals("postgresql")) {
                commands.add("psql");
                commands.add("-h"); // database server host
                commands.add(ip);
                commands.add("-p"); // database server port number
                commands.add(puerto);
                commands.add("-U"); // connect as specified database user
                commands.add(username);
                commands.add("-d"); // database name
                commands.add(nameBD);
                commands.add("-f");
                commands.add(ruta);
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.environment().put("PGPASSWORD", passwrd);
                Process process = null;
                try {
                    process = pb.start();
                    try (BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = buf.readLine()) != null) {
                            System.err.println(line);
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                process.destroy();
            } else {
                commands.add("mysql");
                commands.add("--host=" + ip); // database server host
                commands.add("--port=" + puerto); // database server port number
                commands.add("--user=" + username); // connect as specified database user
                commands.add("--password=" + passwrd); // password for database user
                commands.add(nameBD); // database name
                commands.add("-e"); // execute SQL statements
                commands.add("source " + ruta); // path to the SQL file

                ProcessBuilder pb = new ProcessBuilder(commands);
                Process process = pb.start();

                try (BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = buf.readLine()) != null) {
                        System.err.println(line);
                    }
                }

                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                process.destroy();
            }




        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Importación correcta");
        alert.setContentText("Se ha importado correctamente la base de datos");
        alert.setOnCloseRequest(dialogEvent -> {
            try {
                MainApplication.cambiarEscena("inicio.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    });
    }

    public String getNameBD(){
        return nameBD;
    }

    public String getPasswrd() {
        return passwrd;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public String getPuerto() {
        return puerto;
    }
}
