
import javax.servlet.jsp.jstl.sql.Result;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server extends Thread {
    private DbConnection database;
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Vector<BufferedImage> image_pool = new Vector<>();

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
            if(password.equals(real_psw)){
                database.update("update user set online = 1 where username=\"" + username + "\";");
                resPack.addResponse("Successfully login.");
            }
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
        Result rs = database.query("select * from dictionary where word=\"" + word + "\";");

        OnlineFetch fetch = new OnlineFetch(word);
        String exp_baidu = "", exp_bing = "", exp_youdao = "";
        for (String s : fetch.getTranslationBaidu()) {
            exp_baidu += s + "\n";
        }
        for (String s : fetch.getTranslationBing()) {
            exp_bing += s + "\n";
        }
        for (String s : fetch.getTranslationYoudao()) {
            exp_youdao += s + "\n";
        }
        String pron_BR = fetch.getPronBR();
        String pron_US = fetch.getPronUS();
        resPack.addResponse(exp_baidu);
        resPack.addResponse(exp_bing);
        resPack.addResponse(exp_youdao);
        resPack.addResponse(pron_BR);
        resPack.addResponse(pron_US);
        if(rs.getRowCount() == 0) {
            database.update("insert into dictionary values (\"" + word + "\",\"\",\"\",\"\",0,0,0,\"\",\"\");");
            resPack.addResponse(Integer.toString(0));
            resPack.addResponse(Integer.toString(0));
            resPack.addResponse(Integer.toString(0));
            resPack.addAffixInfo("baidu");
            resPack.addAffixInfo("bing");
            resPack.addAffixInfo("youdao");
        }
        else {
            SortedMap<String, Object> map = (SortedMap<String, Object>) rs.getRows()[0];
            int likebaidu = (int)map.get("like_baidu");
            int likebing = (int)map.get("like_bing");
            int likeyoudao = (int)map.get("like_youdao");
            resPack.addResponse(Integer.toString(likebaidu));
            resPack.addResponse(Integer.toString(likebing));
            resPack.addResponse(Integer.toString(likeyoudao));
        }
        return resPack;
    }
    private ResponsePackage reqLike(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:like");
        String word = reqPack.getRequest().elementAt(0);
        String whichDict = reqPack.getRequest().elementAt(1);
        Result rs = database.query("select * from dictionary where word=\"" + word + "\";");
        SortedMap<String, Object>[] map = (SortedMap<String, Object>[]) rs.getRows();
        if(rs.getRowCount() == 0) return resPack;
        if("baidu".equals(whichDict)){
            database.update("update dictionary set like_baidu = like_baidu+1 where word=\"" + word + "\";");
            int likenum = (int)map[0].get("like_baidu");
            resPack.addResponse(Integer.toString(likenum+1));
        }
        else if("bing".equals(whichDict)){
            database.update("update dictionary set like_bing = like_bing+1 where word=\"" + word + "\";");
            int likenum = (int)map[0].get("like_bing");
            resPack.addResponse(Integer.toString(likenum+1));
        }
        else if("youdao".equals(whichDict)){
            database.update("update dictionary set like_youdao = like_youdao+1 where word=\"" + word + "\";");
            int likenum = (int)map[0].get("like_youdao");
            resPack.addResponse(Integer.toString(likenum+1));
        }
        return resPack;
    }
    private ResponsePackage reqLogout(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:logout");
        String username = reqPack.getRequest().elementAt(0);
        database.update("update user set online=0 where username=\"" + username + "\";");
        return resPack;
    }
    private ResponsePackage reqOnline(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:online");
        Result rs = database.query("select * from user where online=1;");
        SortedMap<String, String>[] map = (SortedMap<String, String>[]) rs.getRows();
        for(SortedMap<String, String> m : map) {
            resPack.addResponse(m.get("username"));
        }
        return resPack;
    }
    private ResponsePackage reqSend(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:send");
        String receiver = reqPack.getRequest().elementAt(0);
        String sender = reqPack.getRequest().elementAt(1);
        String serializableImage = reqPack.getRequest().elementAt(2);
        byte[] bytes = Base64.getDecoder().decode(serializableImage);
        InputStream in = new ByteArrayInputStream(bytes);
        database.updateWithPictures("insert into wordcards(receiver,sender,pic) values (\"" + receiver
                + "\",\"" + sender + "\", ?);", in);
        in.close();
        return resPack;
    }
    private ResponsePackage reqShow(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:show");
        String idstr = reqPack.getRequest().elementAt(0);
        Result rs = database.query("select * from wordcards where id=" + idstr + ";");
        SortedMap<String, Object>[] map = (SortedMap<String, Object>[]) rs.getRows();
        byte[] bytes = (byte[]) map[0].get("pic");
        String str = Base64.getEncoder().encodeToString(bytes);
        resPack.addResponse(str);
        return resPack;
    }
    private ResponsePackage reqMails(RequestPackage reqPack) throws Exception{
        ResponsePackage resPack = new ResponsePackage();
        resPack.setType("res:mails");
        String receiver = reqPack.getRequest().elementAt(0);
        Result rs = database.query("select * from wordcards where receiver = '" + receiver + "';");
        SortedMap<String, Object>[] map = (SortedMap<String, Object>[]) rs.getRows();
        for(SortedMap<String, Object> indimap : map){
            String sender = (String) indimap.get("sender");
            int id = (int) indimap.get("id");
            String message = "ID: " + id + "\t From: " + sender;
            resPack.addResponse(message);
        }

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
                else if("req:like".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqLike(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:logout".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqLogout(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:online".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqOnline(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:send".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqSend(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:show".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqShow(reqPack);
                    out.writeObject(resPack);
                    out.flush();
                }
                else if("req:mails".equals(reqPack.getType())) {
                    ResponsePackage resPack = reqMails(reqPack);
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
