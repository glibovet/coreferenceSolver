package textanalysis.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Request {

    private HashMap<String, String> params = new HashMap<String, String>();

    public Request(String[] rawHeaders, String body) {

        if (body.length() > 0) {
            this.parseQueryString(body);
        }

        String[] queryParams = rawHeaders[0].split(" ");
        if (queryParams.length > 1) {
            queryParams = queryParams[1].split("\\?");
            if (queryParams.length > 1) {
                this.parseQueryString(queryParams[1]);
            }
        }
    }

    private void parseQueryString(String q) {

        String[] queryParams = q.split("&");

        for (String it : queryParams) {

            String[] kv = it.split("=");

            String value = "";
            if (kv.length > 1) {
                try {
                    value = java.net.URLDecoder.decode(kv[1], "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            params.put(kv[0], value);
        }
    }

    public String param(String param) {
        return this.params.get(param);
    }

}
