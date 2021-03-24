package syhan28;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	
	public makeCollection(String args) throws Exception {

		File dir = new File(args);
		String[] filenames = dir.list((f, name) -> name.endsWith(".html"));

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document docu = docBuilder.newDocument();
		
		Element docs = docu.createElement("docs");
		docu.appendChild(docs);
		
		for (int i = 0; i < filenames.length; i++) {

			File input = new File(args + "\\" + filenames[i]);
			
			org.jsoup.nodes.Document documents = Jsoup.parse(input, "UTF-8");
			
			Elements titles = documents.select("title");
			String titlename = titles.text();
			
			Elements contents = documents.select("p");
			String content = contents.text();
			
			Element doc = docu.createElement("doc");
			Element title = docu.createElement("title");
			Element body = docu.createElement("body");
			
			docs.appendChild(doc);

			String idNum = Integer.toString(i);
			doc.setAttribute("id", idNum);

			title.appendChild(docu.createTextNode(titlename));
			doc.appendChild(title);

			body.appendChild(docu.createTextNode(content));
			doc.appendChild(body);	
	
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		DOMSource source = new DOMSource(docu);
		StreamResult result = new StreamResult(new FileOutputStream(new File(args + "\\collection.xml")));

		transformer.transform(source, result);
	}

}
