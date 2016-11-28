import javax.servlet.jsp.jstl.sql.Result;
import javax.sql.RowSet;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.SortedMap;
import java.util.Vector;

/**
 * Created by Tony Jiang on 2016/11/13.
 */
public class Server extends Thread {
    private DbConnection database;
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ResponsePackage reqLogin(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:login");
        String username = reqPack.getRequest().elementAt(0);
        String password = reqPack.getRequest().elementAt(1);
        if(username == null || password == null) {
            resPack.addResponse("Failed to login.");
            return resPack;
        }
        Result rs = database.query("select * from user where username=\"" + username + "\";");
        if(rs.getRowCount() == 0) {
            resPack.addResponse("Username not exists.");
            return resPack;
        }
        else {
            SortedMap<String, String> map = (SortedMap<String, String>) rs.getRows()[0];
            String real_psw = map.get("password");
            if(password.equals(real_psw)) resPack.addResponse("Successfully login.");
            else resPack.addResponse("Wrong password.");
            return resPack;
        }

    }
    private ResponsePackage reqRegister(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:register");
        String username = reqPack.getRequest().elementAt(0);
        String password = reqPack.getRequest().elementAt(1);
        if(username == null || username.length() < 4) {
            resPack.addResponse("Invalid username.");
            return resPack;
        }
        if(password == null || password.length() < 4) {
            resPack.addResponse(("Invalid password."));
            return resPack;
        }
        Result rs = database.query("select * from user where username=\"" + username + "\";");
        if(rs.getRowCount() != 0) {
            resPack.addResponse("Username occupied.");
            return resPack;
        }
        else {
            database.update("insert into user values (\"141220048\",\"141220048\",0);");
            resPack.addResponse("Successfully registered.");
            return resPack;
        }

    }
    private ResponsePackage reqSearch(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:search");
        String word = reqPack.getRequest().elementAt(0);
        OnlineFetch fetch = new OnlineFetch(word);
        String exp_baidu = "", exp_bing = "", exp_youdao = "";
        for(String s : fetch.getTranslationBaidu()) {
            exp_baidu += s + "\n";
        }
        for(String s : fetch.getTranslationBing()) {
            exp_bing += s + "\n";
        }
        for(String s : fetch.getTranslationYoudao()) {
            exp_youdao += s + "\n";
        }
        String pron_BR = fetch.getPronBR();
        String pron_US = fetch.getPronUS();
        resPack.addResponse(exp_baidu);
        resPack.addResponse(exp_bing);
        resPack.addResponse(exp_youdao);
        resPack.addResponse(pron_BR);
        resPack.addResponse(pron_US);
        return resPack;
    }

    public Server(Socket client) throws IOException{
        this.client = client;
        this.database = new DbConnection("jdbc:mysql://localhost/dict", "TonyJiang", "1874");
    }

    public void run() {

        try{
            in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            out = new ObjectOutputStream(client.getOutputStream());
            while(true) {
                RequestPackage reqPack = (RequestPackage) in.readObject();
                if ("req:login".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqLogin(reqPack);
                    System.out.println("login here");
                    out.writeObject(resPack);
                    out.flush();

                }
                else if ("req:register".equals(reqPack.getType())) {
                    System.out.println("Request received!!!");
                    ResponsePackage resPack = reqRegister(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:search".equals(reqPack.getType())) {
                    System.out.print("received:::::");
                    ResponsePackage resPack = reqSearch(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
            }
        } catch (Exception e){
            System.out.print(e.getMessage());
        } finally {
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
