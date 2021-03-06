package pack;

import java.io.Serializable;
import java.util.Vector;

public class RequestPackage implements Serializable {
    private String type = "";
    private Vector<String> content = new Vector<String>();

    public void setType(String type) {
        this.type = type;
    }
    public void addRequest(String req) {
        this.content.add(req);
    }
    public String getType() {
        return this.type;
    }
    public Vector<String> getRequest() {
        return this.content;
    }



}