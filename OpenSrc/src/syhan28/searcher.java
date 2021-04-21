package syhan28;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class searcher {
	
	public searcher(String args, String query) throws Exception {
		
		String filepath = "C:\\Users\\tmddu\\opensrcSW\\SimpleIR\\OpenSrc";
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(filepath + "\\collection.xml");
		
		NodeList titleTagList = document.getElementsByTagName("title");
		
		HashMap<String, Double> ans = CalcSim(args, query, titleTagList);
		
		List<String> keySet = new ArrayList<>(ans.keySet());
		Collections.sort(keySet);
		Collections.sort(keySet, (o1, o2) -> (ans.get(o2).compareTo(ans.get(o1))));
		
		int temp = 0;
		
		for (String key : keySet) {
			if (temp == 3) break;
			System.out.println(temp + 1 + "위 : " + key);
			temp++;
		}
		
	}
	
	
	public HashMap<String, Double> CalcSim(String args, String query, NodeList titleTagList) throws Exception {
		
		ArrayList<String> words = getKey(query);
		
		HashMap hashMap = new HashMap();
		hashMap = getHM(args, hashMap);
		
		ArrayList<ArrayList<String>> values = getValue(hashMap, words);
		
		HashMap<String, Double> simsHashMap = new HashMap<String, Double>();
		
		for (int i = 0; i < titleTagList.getLength(); i++) {
			double sim = 0;
			double squaredSim = 0;
			double norm = 0;
			double cosSim = 0;
			String docTitle = titleTagList.item(i).getTextContent();
			
			for (int j = 0; j < words.size(); j++) {
				ArrayList<String> value = values.get(j);
				if (value == null) {
					value = new ArrayList<String>();
				}
//				System.out.println(words.get(j) + " : " + value);
				String docIdNum = Integer.toString(i);
				if (value.contains(docIdNum)) {
					int index = value.indexOf(docIdNum);
					sim += Double.parseDouble(value.get(index + 1));
					squaredSim += Math.pow(Double.parseDouble(value.get(index + 1)), 2);
				}
			}
			
			norm = Math.sqrt(words.size())*Math.sqrt(squaredSim);
			cosSim = Math.round((sim / norm)*100)/100.0;
			simsHashMap.put(docTitle, cosSim);
			System.out.println("코사인 유사도 : " + cosSim);
		}
		
		return simsHashMap;
	}
	
	
	public ArrayList<String> getKey(String query) {
		
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		
		ArrayList<String> words = new ArrayList<String>();
		for (int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			words.add(kwrd.getString());
//			System.out.println(kwrd.getString() + "\t" + kwrd.getCnt());
		}
		
		return words;
		
	}
	
	
	public HashMap getHM(String path, HashMap hm) throws Exception {
		
		FileInputStream fileStream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		hm = (HashMap)object;
		
		return hm;
		
	}
	
	
	public ArrayList<ArrayList<String>> getValue(HashMap hm, ArrayList<String> al) {
		
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < al.size(); i++) {
			ArrayList<String> value = (ArrayList<String>)hm.get(al.get(i));
			values.add(value);
		}
		
		return values;
		
	}
	
}
