import java.io.Serializable;
import java.util.Vector;

public class ResponsePackage implements Serializable{
    private String type = "";
    private Vector<String> content = new Vector<String>();
    private Vector<String> affixInfo = new Vector<String>();

    public String           getType()                   { return this.type; }
    public Vector<String>   getContent()                { return this.content; }
    public Vector<String>   getAffixInfo()              { return this.affixInfo; }
    public void             setType(String type)        { this.type = type; }
    public void             addResponse(String res)     { this.content.add(res); }
    public void             addAffixInfo(String info)   { this.affixInfo.add(info); }

}
