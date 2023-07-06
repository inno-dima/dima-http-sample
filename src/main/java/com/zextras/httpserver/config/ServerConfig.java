package src.main.java.com.zextras.httpserver.config;

public class ServerConfig {
    public static String sitePath = "html";
    public static String serverName = "com.zextras.httpserver.SimpleHTTPServer 0.0.1";
    public static int PORT = 3000;

    public int getPort(){
        return PORT;
    }

    public int getThreadCount(){return 10;}

    public String getSitePath(){return sitePath;}
}
