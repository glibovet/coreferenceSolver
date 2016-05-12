package textanalysis.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import textanalysis.http.Response;

public class RequestHandler implements Runnable {

    private Socket client = null;
    ServerSocket server = null;
    private Client clientObj = null;

    String uid;

    public RequestHandler(Client client) {
        this.client = client.getSocket();
        this.uid = client.getUid();
        this.clientObj = client;
    }

    @Override
    public void run() {

        try {
            PrintWriter outStream = new PrintWriter(client.getOutputStream());
            BufferedReader inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));

            char[] cbfr = new char[1024 * 100]; // 100 kb body      

            inStream.read(cbfr);
            String[] splitRequest = String.valueOf(cbfr).split("\r\n\r\n");
            String requestHeaders = splitRequest[0];

            String[] headers = requestHeaders.split("\n");

            String body = "";

            if (splitRequest.length > 1) {
                body = splitRequest[1].trim();
            }

            this.clientObj.setRawRequest(headers, body);

            String[] requestMethodUri = headers[0].split(" ");
            String uri = "";

            if (requestMethodUri.length > 1) {
                uri = requestMethodUri[1].split("\\?")[0];
            }

            String routeName = (requestMethodUri[0] + uri).toLowerCase();

            if (Server.routes.containsKey(routeName)) {
                this.writeResponse(new Response(Server.routes.get(routeName).getCallback().handle(this.clientObj)));

            } else {
                try {
                    // response = Server.routes.get("e404").getCallback().handle(this.clientObj);
                    this.writeResponse(new Response(404));
                } catch (Exception e) {
                    // System.out.println("error while going to execute route " + routeName);
                    outStream.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.clientObj.sendMessage(e.getMessage());
        }

    }

    public void writeResponse(Response httpMessage) {
        try {
            this.clientObj.sendMessage(httpMessage.toString());
            this.clientObj.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
