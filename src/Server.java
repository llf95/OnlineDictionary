import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tony Jiang on 2016/11/13.
 */
public class Server extends Thread {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    private void reqLogin(String username, String password){
        System.out.println("Username: " + username + "\nPassword: " + password);
        out.println("echo:login");
        if(username.equals("jxy")) {
            out.println("succeed");
            out.flush();
        }
        else {
            out.println("fail");
            out.flush();
        }
    }

    public Server(Socket client) throws IOException{
        this.client = client;

    }

    public void run() {

        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());
            while(true) {
                String str = in.readLine();
                if ("req:login".equals(str))
                    reqLogin(in.readLine(), in.readLine());
            }
        }
        catch (IOException ioe){
        }
        finally {
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(1637);
        while(true) {
            Server s = new Server(server.accept());
            s.start();
        }
    }
}
