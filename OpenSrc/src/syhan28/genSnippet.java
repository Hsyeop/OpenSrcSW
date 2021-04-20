package syhan28;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class genSnippet {
	
	public genSnippet(String args, String query) throws Exception {
		
		String filepath = "C:\\Users\\tmddu\\opensrcSW\\SimpleIR\\OpenSrc";
		
		String[] inputs = query.split(" ");
		
		File file = new File(filepath +"\\" + args);
		
		FileInputStream fileStream = new FileInputStream(filepath + "\\" + args);
		
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		int s = fileStream.read();
		
		System.out.println(s);
		


		
		


		
		
	}

}
