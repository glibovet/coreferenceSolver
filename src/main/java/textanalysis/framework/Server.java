package textanalysis.framework;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    boolean started = false;

    static HashMap<String, Route> routes = new HashMap<String, Route>();
    private HashMap<String, String> options = new HashMap<String, String>();

    public Server(int port) throws Exception {

        this.options.put("port", String.valueOf(port));

        this.register(new Route("e404", (Client c) -> {
            return "404 Not Found";
        }));
    }
    
    
    public static String readFile(String path) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return String.join("\n", lines);
    }
    
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(this.options.get("port")));) {
            this.started = true;
            while (true) {
                Client client = new Client(new BigInteger(130, new SecureRandom()).toString(32), serverSocket.accept());

                Thread requestHandler = new Thread(new RequestHandler(client));
                requestHandler.start();
            }

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("simple server successfully started");
    }

    public void register(Route uH) throws Exception {
        if (this.started) {
            throw new Exception("Can't register uri handler after start");
        }

        String routeName = (uH.getMethod() + uH.getUri()).toLowerCase();
       // System.out.println("registered a route {{" + routeName + "}}");
        routes.put(routeName, uH);
    }

    public void stop() {
        this.started = false;
        System.out.println("simple server service stopped");
    }

}
