package textanalysis.framework;

import java.util.concurrent.Callable;

public class Route {

    public static final String DEFAULT_METHOD = "GET";

    private String method;
    private String uri;
    private RouteHandler callback;

    public Route(String uri,RouteHandler cb) {
        this.Register(uri,Route.DEFAULT_METHOD,cb);
    }
    
    public Route(String uri,String method,RouteHandler cb) {
        this.Register(uri,method,cb);
    }
    
    
    private void Register(String uri,String m,RouteHandler callback) {
        this.method = m;
        this.uri = uri;
        this.callback = callback;
    }
    

    public String getMethod() {
        return this.method;
    }

    public String getUri() {
        return this.uri;
    }
    
    public RouteHandler getCallback() {
        return this.callback;
    }
    
}
