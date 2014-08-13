package com.ecml;

 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import android.app.Activity;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
 
public class ReadXMLFile {
 
	

    public ArrayList<String> read( Activity act){
    	ArrayList<String> list = new ArrayList<String>();
    	try{
    		
    		InputStream f = act.getAssets().open("SequenceOfActivity.xml");
	//File fXmlFile = new File(path.getPath());
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(f);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	//doc.getDocumentElement().normalize();
 
	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
	NodeList nList = doc.getElementsByTagName("activity");
 
	//System.out.println("----------------------------");
 
	for (int temp = 0; temp < nList.getLength(); temp++) {
 
		Node nNode = nList.item(temp);
 
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
			Element eElement = (Element) nNode;
 
			System.out.println("nom de l activité  : " + eElement.getAttribute("type"));
			list.add(eElement.getAttribute("type"));

		}
	}
    	}
    	 catch (Exception e) {
    			e.printStackTrace();
    		    }
    	
    	 return list;
    }
   
  
  }

