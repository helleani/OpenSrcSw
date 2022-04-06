package scripts;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class kuir {
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	

		String command=args[0];
		String path=args[1];

		if(command.equals("-c")){
			makeCollection collection=new makeCollection(path);
			collection.makeXml();
		}
		else if(command.equals("-k")){
			makeKeyword keyword= new makeKeyword(path);
			keyword.convertXml();
		}
		else if(command.equals("-i")){
			indexer ix=new indexer(path);
			ix.makepost();

		}	
		else if(command.equals("-s")){
		searcher sr= new searcher(path, args[3]);
		sr.CalcSim();
		}
		
				
	}
	
}
