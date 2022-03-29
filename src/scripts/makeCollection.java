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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {


    private String data_path;
    public makeCollection(String path) {
        this.data_path=path;

    }

    public static File[] makeFileList(String path) {
		File dir=new File(path);
		return dir.listFiles();  
	}	
	
    public void makeXml() throws ParserConfigurationException, IOException, TransformerException {
        
        File file[] = null;
		try {
			file = makeFileList(data_path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        //docs 
		DocumentBuilderFactory docFactory= DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder= docFactory.newDocumentBuilder();
		
		Document doc= docBuilder.newDocument();
		
		Element docs=doc.createElement("docs");
		doc.appendChild(docs);
		
		
		for(int i=0; i<file.length; i++) {
			org.jsoup.nodes.Document html = Jsoup.parse(file[i],"UTF-8");
			Element _doc=doc.createElement("doc");
			docs.appendChild(_doc);
			_doc.setAttribute("id", String.valueOf(i));
			
			String titleData=html.title();
			Element title=doc.createElement("title");
			title.appendChild(doc.createTextNode(titleData));
			_doc.appendChild(title);
			
			String bodyData= html.body().text();
			Element body=doc.createElement("body");
			body.appendChild(doc.createTextNode(bodyData));
			_doc.appendChild(body);
			
			
		}
		
		//xml
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
		
		Transformer transformer=transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source=new DOMSource(doc);
																
		StreamResult result=new StreamResult(new FileOutputStream(new File("./collection.xml")));
		transformer.transform(source, result);


    }


    
}
