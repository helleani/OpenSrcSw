package scripts;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher {
    private String path;
    private String query;

    public searcher(String path,String query) {
        this.path=path;
        this.query=query;
        
    }

    public double mulitsize(ArrayList<Double>quvec,double resvec[] ){
        double first=0;
        double second=0;

        for(double w1:quvec){
            first+=w1*w1;
        }
        for(double w2:resvec){
            second+=w2*w2;
        }

        return Math.sqrt(first)*Math.sqrt(second);

    }
    
    public void CalcSim() throws IOException, ClassNotFoundException{
        
        FileInputStream fs = new FileInputStream(path);
		ObjectInputStream os = new ObjectInputStream(fs);
        Object ob=os.readObject();
        os.close();
        HashMap<String,String> map=(HashMap<String,String>)ob;
        Set<String> kset=map.keySet();
        
        KeywordExtractor ke=new KeywordExtractor();
        KeywordList kl=ke.extractKeyword(query,true);
        int keysize;
        keysize=kl.size();

        ArrayList<String> arrkey=new ArrayList<>();
        ArrayList<Double> quvec=new ArrayList<>();

        for(Keyword k: kl){
            arrkey.add(k.getString());
            quvec.add((double)k.getCnt());
        }

        File file=new File("index.xml");
		org.jsoup.nodes.Document xmlf = Jsoup.parse(file,"UTF-8" ,"", Parser.xmlParser());
		Elements titlele=xmlf.select("title");
		int idsize=titlele.size();
        double[][] resvec=new double[idsize][keysize];
        for(int i=0; i<keysize; i++){
            if(!kset.contains(arrkey.get(i))){
                for(int j=0; j<idsize; j++){
                    resvec[j][i]=0;
                }
                continue;
            }
            String store[]=map.get(arrkey.get(i)).split(" ");
            for(int j=0; j<idsize; j++){
                double weight=Double.parseDouble(store[j*2+1]);
                resvec[j][i]=weight;
            }
        }

        double[] sort= new double[idsize];
        for(int i=0; i<idsize; i++){
            double sum=0;
            //Inner product
            for(int j=0; j<keysize; j++){
                sum+=quvec.get(j)*resvec[i][j];
            }
            sort[i]=sum;
        } 

        double[] finalres= new double[idsize];

        //calc
        for(int i=0; i<idsize; i++){
            double store=mulitsize(quvec,resvec[i]);

            if(store==0){
                finalres[i]=0;
            }
            else{
                finalres[i]=sort[i]/store;
            }
            
        }
        
        //order
        for(int i=0; i<3; i++){
            int max=0;
            for(int j=0; j<finalres.length; j++){
                if(finalres[j]>finalres[max]){
                    max=j;
                }
            }
            System.out.println("문서:"+titlele.get(max).text()+" "+"유사도:"+finalres[max]);
            finalres[max]=-1;
        }      

    }
    

}
