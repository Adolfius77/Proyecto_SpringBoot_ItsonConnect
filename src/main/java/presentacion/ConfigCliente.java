package presentacion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ConfigCliente {
    
    public static String BASE_URL;
    public static String WS_URL;

    static {
        Properties prop = new Properties();
        String ip = "localhost";
        String port = "8080";

        try {
         
            prop.load(new FileInputStream("config.properties"));
            ip = prop.getProperty("server.ip", "localhost");
            port = prop.getProperty("server.port", "8080");
        } catch (IOException ex) {
            System.out.println("No se encontró config.properties, usando localhost.");
        }
        
        BASE_URL = "http://" + ip + ":" + port;
        WS_URL = BASE_URL + "/itson-connect-ws";
        System.out.println("Conectando a: " + BASE_URL);
    }
}