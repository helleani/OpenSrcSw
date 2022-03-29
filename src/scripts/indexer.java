package scripts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class indexer {

    private String indexpath;

    public indexer(String string) {
        this.indexpath=string;
    }

    public void makepost() throws IOException{
    	
        File file = null;
        try {
        	file = new File(indexpath);
        }
        catch (Exception e) {
			e.printStackTrace();
		}
		org.jsoup.nodes.Document xmlfile = Jsoup.parse(file,"UTF-8" ,"", Parser.xmlParser());
		Elements bodyelement = xmlfile.select("body");
		Elements docelement=xmlfile.select("doc");
        int size=docelement.size();
        

         //df make
         HashMap<String,Integer>dfmap=new HashMap<>();
         for(int k=0; k<size; k++){
             String bodyData= bodyelement.get(k).text();
             String str=bodyData;
             String[] list=str.split("#");
             for(String s: list){
                 String[] word=s.split(":");
                 if(!dfmap.containsKey(word[0])){
                     dfmap.put(word[0], 1);
                 }
                 else{
                     dfmap.put(word[0],dfmap.get(word[0])+1);
                 }
             }
             
         }
         
         HashMap<String,String> finalmap=new HashMap<>();
         //tf make
        for(int i=0; i<size; i++){
            HashMap<String,Integer> map=new HashMap<>();
			String bodyData= bodyelement.get(i).text();
            String str=bodyData;
            String[] list=str.split("#");
            for(String s : list){
                String[] word=s.split(":");
                if(!map.containsKey(word[0])){
                    map.put(word[0], Integer.parseInt(word[1]));
                }
                else{
                    int oldval=map.get(word[0]);
                    int newval=oldval+Integer.parseInt(word[1]);
                    map.put(word[0],newval);
                }
                double df=dfmap.get(word[0]);
                double weight=map.get(word[0])*Math.log(size/df);
                String screen=String.valueOf(i)+" "+String.format("%.2f", weight);

                if(finalmap.containsKey(word[0])){
                    finalmap.put(word[0],finalmap.get(word[0])+" "+screen);
                }
                else{
                    finalmap.put(word[0],screen);
                }

            }  
            
            Set<String> pzero =dfmap.keySet();
            for(String j: pzero) {
            	if(!map.containsKey(j)) {
            		if(finalmap.containsKey(j))
            			finalmap.put(j,finalmap.get(j)+" "+String.valueOf(i)+" "+"0.0");
            		else 
            			finalmap.put(j, String.valueOf(i)+" "+"0.0");
            	}
            	
            }
            
        }
        Iterator<String> output=finalmap.keySet().iterator();
        while(output.hasNext()){
        	String sr=output.next();
        	System.out.println(sr+"->"+finalmap.get(sr));
        }
       

    }
    
    
}
