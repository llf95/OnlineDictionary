/**
 * Created by Tony Jiang on 2016/11/2.
 */
import java.io.File;
import java.util.*;



public class Dictionary {
    private class _DicStruct{
        public String _explanation = new String();
        public String _pronouncement = new String();
        public int _number;
    }
    private HashMap<String, _DicStruct> _dictionary = new HashMap<String, _DicStruct>();
    private String _filepath = new String();
    private HashMap<Integer, String> _num2word = new HashMap<Integer, String>();

    Dictionary(){}
    Dictionary(String filepath) throws Exception{
        _filepath = filepath;
        File targetFile = new File(_filepath);
        loadDictionary(targetFile);
    }

    boolean loadDictionary(File file) throws Exception{
        if(!file.exists()) return false;
        Scanner dic = new Scanner(file);
        while(dic.hasNextLine()){
            String str = dic.nextLine();
            String[] tokens = str.split("\t");
            _DicStruct temp = new _DicStruct();
            if(3 == tokens.length){
                temp._explanation = tokens[2];
                temp._number = Integer.valueOf(tokens[0]);
                temp._pronouncement = null;
            }
            else if(4 == tokens.length){
                temp._explanation = tokens[3];
                temp._number = Integer.valueOf(tokens[0]);
                temp._pronouncement = tokens[2];
            }
            else if(5 == tokens.length){
                tokens[3] = tokens[3].replaceAll("\"", "");
                tokens[4] = tokens[4].replaceAll("\"", "");
                temp._explanation = tokens[1] + " " + tokens[3] + ": " + tokens[4];
                temp._number = Integer.valueOf(tokens[0]);
                temp._pronouncement = tokens[2];
            }
            tokens[1] = tokens[1].toLowerCase();
            tokens[1] = trim(tokens[1]);
            this._dictionary.put(tokens[1], temp);
            this._num2word.put(Integer.valueOf(tokens[0]), tokens[1]);
        }
        dic.close();
        return true;
    }

    public String searchExplanation(String word){
        if(word == null) return null;
        word = word.toLowerCase();
        word = trim(word);
        if(_dictionary.get(word) != null){
            return _dictionary.get(word)._explanation;
        }
        else {
            return null;
        }
    }
    public String searchPronouncement(String word){
        if(word == null) return null;
        word = word.toLowerCase();
        word = trim(word);
        if (_dictionary.get(word) != null)
            return _dictionary.get(word)._pronouncement;
        else
            return null;
    }
    public int searchNumber(String word){
        if(word == null) return 0;
        word = word.toLowerCase();
        word = trim(word);
        if (_dictionary.get(word) != null)
            return _dictionary.get(word)._number;
        else
            return 0;
    }
    public String searchByNumber(int num){
        return _num2word.get(num);
    }
    public ArrayList<String> similarWord(String word){
        if(word == null) return null;
        word = word.toLowerCase();
        word = trim(word);
        ArrayList<String> res= new ArrayList<>();
        HashMap<String, Integer> edit_length = new HashMap<String, Integer>();
        Iterator iter = this._dictionary.entrySet().iterator();
        while(iter.hasNext()){
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            String temp = (String)entry.getKey();
            int e = calEditLength(word, temp);
            edit_length.put(temp, e);
        }
        List<Map.Entry<String, Integer>> infoIds =
                new ArrayList<Map.Entry<String, Integer>>(edit_length.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()) - (o2.getValue());
            }
        });
		/*
		for (int i = 0; i < 5; i++) {
			int j;
			if(infoIds.get(0).getValue() == 0) j = i + 1;
			else j = i;
			if(infoIds.get(j).getValue() >= 5) break;
		    res[i] = infoIds.get(j).getKey();
		}
		*/

        //return res;
        for(int i = 0;i < infoIds.size(); i ++){
            res.add(infoIds.get(i).getKey());
        }
        return res;
    }
    public int size(){
        return _num2word.size();
    }

    private static int calEditLength(String x, String y){
        int[][] edit = new int[3][100];
        int xlen = x.length();
        int ylen = y.length();
        int i = 0, j = 0;

        for(j = 0; j <= ylen; j++){
            edit[0][j] = j;
        }
        for(i = 1; i <= xlen; i++ ){
            edit[i % 3][0] = edit[(i - 1) % 3][0] + 1;
            for(j = 1; j <= ylen; j++){
                if(x.charAt(i - 1) == y.charAt(j - 1)){
                    edit[i % 3][j] = Math.min(Math.min(edit[i % 3][j - 1] + 1, edit[(i - 1) % 3][j] + 1), edit[(i - 1) % 3][j - 1]);
                }else{
                    if(i >= 2 && j >= 2 && x.charAt(i - 2) == y.charAt(j - 1) && x.charAt(i - 1) == y.charAt(j - 2)){
                        edit[i % 3][j] = Math.min(Math.min(edit[i % 3][j - 1] + 1, edit[(i - 1) % 3][j] + 1), Math.min(edit[(i-1)%3][j-1] + 1, edit[(i-2)%3][j-2] + 1));
                    }else{

                        edit[i % 3][j] = Math.min(Math.min(edit[i % 3][j - 1] + 1, edit[(i - 1) % 3][j] + 1), edit[(i - 1) % 3][j - 1] + 1);
                    }
                }
            }
        }
        return edit[(i - 1) % 3][j - 1];
    }
    private static int getBlankNumber(String s, int index) {
        if (index < s.length()) {
            if (s.charAt(index) == ' ') {
                return getBlankNumber(s, index + 1) + 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
    private static String mergeBlank(String s) {
        int numberBlank = 0;
        String a1;
        String a2;
        for (int index = 0; index < s.length(); index++) {
            numberBlank = getBlankNumber(s, index);
            if (numberBlank >= 2) {
                a1 = s.substring(0, index);
                a2 = s.substring(index + numberBlank - 1, s.length());
                s = a1 + a2;
            }
        }
        return s;
    }
    private static String trim(String s){
        String temp = mergeBlank(s);
        if (temp.charAt(0) == ' ') {
            temp = temp.substring(1, temp.length());
        }
        if (temp.charAt(temp.length() - 1) == ' ') {
            temp = temp.substring(0, temp.length() - 1);
        }
        return temp;
    }
}