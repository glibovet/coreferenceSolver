/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalysis.framework;

import textanalysis.http.Request;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashSet;

public class Client {

    private Socket clientSocket;
    public Request Request = null;

    private long connected;
    private String uid;

    public Client(String uid, Socket clientSocket) {
        this.connected = (new Date()).getTime();
        this.uid = uid;
        this.clientSocket = clientSocket;
    }

    public void setRawRequest(String[] headers, String body) {
        this.Request = new Request(headers, body);
    }

    public String getUid() {
        return this.uid;
    }

    public Socket getSocket() {
        return this.clientSocket;
    }

    public void sendMessage(String data) {
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(this.clientSocket.getOutputStream(),Charset.forName("UTF-8"));
            outStream.write("\n" + data);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
