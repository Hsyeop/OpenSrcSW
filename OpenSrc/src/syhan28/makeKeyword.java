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

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class makeKeyword {
	
	public makeKeyword(String args) throws Exception {
		String filepath = "C:\\Users\\tmddu\\opensrcSW\\SimpleIR\\OpenSrc";
		
		KeywordExtractor ke = new KeywordExtractor();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(args);
		
		Document docu = docBuilder.newDocument();
		
		Element docs = docu.createElement("docs");
		docu.appendChild(docs);
		
		NodeList titleTagList = document.getElementsByTagName("title");
		NodeList bodyTagList = document.getElementsByTagName("body");
		
		for(int i = 0; i < titleTagList.getLength(); i++) {
			String titleName = titleTagList.item(i).getTextContent();
			String content = bodyTagList.item(i).getTextContent();
			
			String newContent = "";
			
			KeywordList kl = ke.extractKeyword(content, true);
			
			for (int k = 0; k < kl.size(); k++) {
				Keyword kwrd = kl.get(k);
				newContent += kwrd.getString() + ":" + kwrd.getCnt() + "#";
				System.out.println(kwrd.getString() + "\t" + kwrd.getCnt());
			}
			
			Element doc = docu.createElement("doc");
			Element title = docu.createElement("title");
			Element body = docu.createElement("body");
			
			docs.appendChild(doc);

			String idNum = Integer.toString(i);
			doc.setAttribute("id", idNum);

			title.appendChild(docu.createTextNode(titleName));
			doc.appendChild(title);

			body.appendChild(docu.createTextNode(newContent));
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
		StreamResult result = new StreamResult(new FileOutputStream(new File(filepath + "\\index.xml")));

		transformer.transform(source, result);
		
	}

}
