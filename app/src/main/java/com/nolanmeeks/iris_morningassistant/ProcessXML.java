package com.nolanmeeks.iris_morningassistant;

/**
 * Created by wip on 3/2/17.
 * Used for gathering and processing data from Yahoo Weather Services.
 * Sends YQL queries, and processes data with XML.
 */
import java.io.*;
import java.util.*;

import java.net.URL;
import javax.xml.parsers.*;
//import javax.xml.transform.*;
import org.w3c.dom.*;
//import java.net.URLEncoder;


public class ProcessXML {
  public static void main(String[] args) {
    try {
      Document doc = gatherXML("fairfax,VA");
      HashMap<String, String> data = getData(doc, 0);
      
      System.out.println(String.format("Condition: %s", data.get("condition")));
      System.out.println(String.format("High: %s", data.get("high")));
      System.out.println(String.format("Low: %s", data.get("low")));
      
    } catch(IOException e) {
      System.out.println(e);
    }
  }
  
  
    public static Document gatherXML(String loc) throws IOException {
        String url;
        // creating the URL
        //String query = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s,%s\")",
                //city, state);
        String query = String.format("select+*+from+weather.forecast+where+woeid+in+(select+woeid+from+geo.places(1)+where+text=\"%s\")",
                loc);
        url = "https://query.yahooapis.com/v1/public/yql?q=" + query;
        //url = URLEncoder.encode(url, "utf-8");
        
        URL xmlUrl = new URL(url);
        InputStream in = xmlUrl.openStream();
        //System.out.println(convertStreamToString(in));
        
        // parsing the XmlUrl
        return parse(in);
    }

    public static String convertStreamToString(InputStream is) {
      Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
    }
    
    public static Document parse(InputStream is) {
      Document doc = null;
      DocumentBuilderFactory domFactory;
      DocumentBuilder builder;
      
      try {
        domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(false);
        domFactory.setNamespaceAware(false);
        builder = domFactory.newDocumentBuilder();
        
        doc = builder.parse(is);
      } catch (Exception ex) {
        System.out.println("unable to load XML: " + ex);
      }
      return doc;
    }
    
    public static HashMap<String, String> getData(Document doc, int day) {
      HashMap<String,String> ret = new HashMap<String, String>();
      NodeList item = 
        doc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(12).getChildNodes();
      NamedNodeMap curDay = item.item(6+day).getAttributes();

      ret.put("condition", curDay.getNamedItem("text").getNodeValue()); //item 5
      ret.put("high", curDay.getNamedItem("high").getNodeValue()); //item 0
      ret.put("low", curDay.getNamedItem("low").getNodeValue());
      ret.put("date", curDay.getNamedItem("date").getNodeValue());
      ret.put("day", curDay.getNamedItem("day").getNodeValue());

      return ret;
    }
}
