
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextToXml {

	public static void main(String args[]){
		
		String inputFile = JOptionPane.showInputDialog("Enter the full name of the text file");
		
		File file = new File(inputFile);
		if(!file.exists()){
			JOptionPane.showMessageDialog(null, "File does not exist");
			return;
		}
		try {
			//Read in the text file
	    	Scanner scanFile = new Scanner(new File(inputFile));
	    	
	    	
	    	/*Create the XML*/
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("graphic.xml"));
			
			Element rootElement = doc.createElement("vectors");
			
			doc.appendChild(rootElement);
		
			while (scanFile.hasNext()){
	    		String val = scanFile.next();
	    		Element valueElement = doc.createElement("point");
	    		Attr attr = doc.createAttribute("value");
	    		attr.setValue(val);
	    		valueElement.setAttributeNode(attr);
				//valueElement.appendChild(doc.createTextNode(val));
				rootElement.appendChild(valueElement);
	    	}
			
			transformer.transform(source, result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
}
