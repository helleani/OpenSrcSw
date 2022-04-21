import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MidTerm{
    
    private String path;
    private String query;

    public MidTerm(String path, String args) {
        this.path=path;
        this.query=args;
    }

    public void showSnippet(){
        File file=new File(path);
        DocumentBuilderFactory docFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder= docFactory.newDocumentBuilder();
		Document doc= docBuilder.newDocument();
		Element docs=doc.createElement("docs");
		

        org.jsoup.nodes.Document xmlf = Jsoup.parse(file,"UTF-8" ,"", Parser.xmlParser());
		Elements bodyele = xmlf.select("body");
		Elements titlele=xmlf.select("title");
		Elements docele=xmlf.select("doc");
		int numsize=docele.size();

    }


}