package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeKeyword {
    
   	private String xmlflpath;

    public makeKeyword(String path) {

        this.xmlflpath=path;
    }
    
    public void convertXml() throws ParserConfigurationException, IOException, TransformerException {
        File file=new File(xmlflpath);
        DocumentBuilderFactory docFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder= docFactory.newDocumentBuilder();
		Document doc= docBuilder.newDocument();
		Element docs=doc.createElement("docs");
		doc.appendChild(docs);
		
		org.jsoup.nodes.Document xmlf = Jsoup.parse(file,"UTF-8" ,"", Parser.xmlParser());
		Elements bodyele = xmlf.select("body");
		Elements titlele=xmlf.select("title");
		Elements docele=xmlf.select("doc");
		int numsize=docele.size();

		for(int i=0; i<numsize; i++){

			Element _doc=doc.createElement("doc");
			docs.appendChild(_doc);
			_doc.setAttribute("id", String.valueOf(i));

			String titleData=titlele.get(i).text();
			Element title=doc.createElement("title");
			title.appendChild(doc.createTextNode(titleData));
			_doc.appendChild(title);
			

			String bodyData= bodyele.get(i).text();
			KeywordExtractor ke=new KeywordExtractor();
			KeywordList kl=ke.extractKeyword(bodyData,true);
			bodyData="";
			for (Keyword k : kl) {
				bodyData+=k.getString()+":"+k.getCnt()+"#";
			}
			Element body=doc.createElement("body");
			body.appendChild(doc.createTextNode(bodyData));
			_doc.appendChild(body);

			
		}
		

		//index xml
		TransformerFactory transformerFactory=TransformerFactory.newInstance();		
		Transformer transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source=new DOMSource(doc);
		StreamResult result=new StreamResult(new FileOutputStream(new File("./index.xml")));
		transformer.transform(source, result);

    }
		
}
