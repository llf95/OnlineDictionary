
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class OnlineFetch {
    private String word = "";

    private String baidu = "http://www.iciba.com/";
    private String youdao = "http://dict.youdao.com/w/eng/";
    private String bing = "http://cn.bing.com/dict/search?q=";

    private String html_baidu = "";
    private String html_youdao = "";
    private String html_bing = "";


    private Vector<String> trans_baidu = new Vector<>();
    private Vector<String> trans_youdao = new Vector<>();
    private Vector<String> trans_bing = new Vector<>();
    private String pron_BR = "";
    private String pron_US = "";

    private void fetchHtml() throws Exception {
        URL url_baidu = new URL(baidu.trim());
        URL url_youdao = new URL(youdao.trim());
        URL url_bing = new URL(bing.trim());

        Scanner sc_baidu = new Scanner(url_baidu.openStream());
        Scanner sc_youdao = new Scanner(url_youdao.openStream());
        Scanner sc_bing = new Scanner(url_bing.openStream());

        while(sc_baidu.hasNextLine()) html_baidu += sc_baidu.nextLine() + '\n';
        while(sc_bing.hasNextLine()) html_bing += sc_bing.nextLine() + '\n';
        while(sc_youdao.hasNextLine()) html_youdao += sc_youdao.nextLine() + '\n';

        sc_baidu.close();
        sc_bing.close();
        sc_youdao.close();
    }
    private void fetchMeaningBaidu() throws Exception{
        Pattern pattern = Pattern.compile("(?s)<ul class=\"base-list switch_part\".*?>(.*?)</ul>");
        Matcher matcher = pattern.matcher(html_baidu);
        if (matcher.find()) {
            String means = matcher.group(1);
            Pattern getChinese = Pattern.compile("(?s)<li class=\"clearfix\">(.*?)<p>(.*?)</p>.*?</li>");
            Matcher m = getChinese.matcher(means);
            while (m.find()){
                String prop = m.group(1);
                String trans = m.group(2);
                Pattern p1 = Pattern.compile("(?m)<span class=\"prop\">(.*?)</span>");
                Matcher m1 = p1.matcher(prop);
                if(m1.find()) prop = m1.group(1);
                Pattern p2 = Pattern.compile("(?m)<span>(.*?)</span>");
                Matcher m2 = p2.matcher(trans);
                trans = "";
                while(m2.find()){
                    trans = trans + m2.group(1);
                }
                trans_baidu.add(prop + " " + trans);
            }

        }
    }
    private void fetchMeaningBing() throws Exception{
        Pattern pattern = Pattern.compile("(?s)<div class=\"qdef\">.*?<ul>.*?</ul>");
        Matcher matcher = pattern.matcher(html_bing);
        if (matcher.find()) {
            String means = matcher.group();
            Pattern getChinese = Pattern.compile("(?m)<li>.*?" +
                    "<span class=\"pos\">(.*?)</span>.*?" +
                    "<span class=\"def\">.*?" +
                    "<span>(.*?)</span>.*?" +
                    "</span>.*?" +
                    "</li>");
            Matcher m = getChinese.matcher(means);
            while (m.find()) {
                trans_bing.add(m.group(1) + " " + m.group(2));
            }
        }
    }
    private void fetchMeaningYoudao() throws Exception{
        Pattern pattern = Pattern.compile("(?s)<div class=\"trans-container\">.*?<ul>.*?</ul>.*?</div>");
        Matcher matcher = pattern.matcher(html_youdao);
        if (matcher.find()) {
            String means = matcher.group();
            Pattern getChinese = Pattern.compile("(?m)<li>(.*?)</li>");
            Matcher m = getChinese.matcher(means);
            while (m.find()) {
                if('<' == m.group(1).charAt(0)) return;
                trans_youdao.add(m.group(1));
            }
        }
    }
    private void fetchPron() throws Exception {
        Pattern pattern = Pattern.compile("(?s)<div class=\"baav\">.*?</h2>");
        Matcher matcher = pattern.matcher(html_youdao);
        if (matcher.find()) {
            String pron = matcher.group();
            Pattern getPron = Pattern.compile("(?m)<span class=\"phonetic\">(.*?)</span>");
            Matcher m = getPron.matcher(pron);
            for(int i = 0; m.find(); i++){
                if(i == 0) pron_BR = m.group(1);
                else if(i == 1) pron_US = m.group(1);
                else break;
            }
        }
    }

    OnlineFetch(String word) throws Exception {
        word = word.trim();
        word = word.replace(" ", "%20");
        this.word = word;
        baidu += word;
        youdao += word;
        bing += word;
        fetchHtml();
        fetchMeaningBaidu();
        fetchMeaningBing();
        fetchMeaningYoudao();
        fetchPron();
    }

    public Vector<String> getTranslationYoudao() { return this.trans_youdao; }
    public Vector<String> getTranslationBaidu() { return this.trans_baidu; }
    public Vector<String> getTranslationBing() { return this.trans_bing; }
    public String getPronBR() { return this.pron_BR; }
    public String getPronUS() { return this.pron_US; }
    public String getPron() { return "英：" + this.pron_BR + " 美：" + this.pron_US; }

    public static void main(String[] args) throws Exception{
        OnlineFetch online_fetch = new OnlineFetch("home");
        Vector<String> baidu = online_fetch.getTranslationBaidu();
        Vector<String> youdao = online_fetch.getTranslationYoudao();
        Vector<String> bing = online_fetch.getTranslationBing();
        System.out.println("************pron*************");
        System.out.println(online_fetch.getPron());
        System.out.println("************baidu************");
        for(String s : baidu){
            System.out.println(s);
        }
        System.out.println("************youdao***********");
        for (String s : youdao) {
            System.out.println(s);
        }
        System.out.println("************bing*************");
        for (String s : bing) {
            System.out.println(s);
        }
    }


}
