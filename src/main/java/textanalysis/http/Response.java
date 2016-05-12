package textanalysis.http;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Response {

    private int code;
    private String data;

    public Response(String data) {
        this.init(data, 200);
    }

    public Response() {
        this.init("", 200);
    }

    public Response(String data, int code) {
        this.init(data, code);
    }

    public Response(int code) {
        this.init("", code);
    }

    private void init(String data, int code) {
        this.data = data;
        this.code = code;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {

        return "HTTP/1.1 "+this.code+" OK\r\n"
                + "Cache-Control: no-cache, private\r\n"
                + "Content-type: text/html;  charset=utf-8\r\n"
                + "Date: "+java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")))+"\r\n\n"
                + this.data
                + "\r\n\r\n";
    }

}
