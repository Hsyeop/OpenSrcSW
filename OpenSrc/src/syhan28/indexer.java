package syhan28;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class indexer {
	
	public indexer(String args) throws IOException, Exception {
		String filepath = "C:\\Users\\tmddu\\opensrcSW\\SimpleIR\\OpenSrc";
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(args);
		
		NodeList docTagList = document.getElementsByTagName("doc");
		NodeList bodyTagList = document.getElementsByTagName("body");
		
		double n = docTagList.getLength();
		
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		
		FileOutputStream fileStream = new FileOutputStream(filepath + "\\index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		HashMap<String, ArrayList<String>> indexMap = new HashMap();
		
		for (int i = 0; i < docTagList.getLength(); i++) {
			String idNum = docTagList.item(i).getAttributes().getNamedItem("id").getNodeValue();
			String content = bodyTagList.item(i).getTextContent();
			
			String strClassy[] = content.split("#");
			arrayList.add(strClassy);
			
		}
		
		for (int i = 0; i < arrayList.size(); i++) {
			
			String strArray[] = arrayList.get(i);
			
			for (int j = 0; j < strArray.length; j++) {
				String keyWord = strArray[j];
				ArrayList<String> indexAndW = new ArrayList<String>();
				
				String divStr[] = keyWord.split(":");
				String key = divStr[0];
				
				double tf = Integer.parseInt(divStr[1]);
				double df = getDF(arrayList, key); 
				double w = makeIDF(tf, df, n);

				if(indexMap.containsKey(key)) {
					indexAndW = indexMap.get(key);
					indexAndW.add(Integer.toString(i));
					indexAndW.add(Double.toString(w));
				}
				else {
					indexAndW.add(Integer.toString(i));
					indexAndW.add(Double.toString(w));
				}
				indexMap.put(key, indexAndW);
				
			}
			
		}
		
		objectOutputStream.writeObject(indexMap);
		objectOutputStream.close();
		
		printHash(filepath);
		
	}
	
	
	public double getDF(ArrayList<String[]> arrayList, String str) {
		int count= 0;
		
		for (int i = 0; i < arrayList.size(); i++) {
			String strArr[] = arrayList.get(i);
			for (int j = 0; j < strArr.length; j++) {
				String keyWord[] = strArr[j].split(":");
				if(keyWord[0].equals(str)) {
					count++;
					break;
				}
				
			}

		}
		
		return count;
	}
	
	
	public double makeIDF(double tf, double df, double n) {
		
		double w = tf*Math.log(n / df);
		
		return Math.round(w*100)/100.0;
	}
	
	
	public void printHash(String filepath) throws Exception {
		
		FileInputStream fileStream = new FileInputStream(filepath + "\\index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어온 객체의 type -> " + object.getClass());
		
		HashMap hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			ArrayList<String> value = new ArrayList<String>();
			value = (ArrayList<String>) hashMap.get(key);
			System.out.println(key + " -> " + value);
		}
		
	}

}
