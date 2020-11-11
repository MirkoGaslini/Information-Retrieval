/*
 * @file	LuceneTester.java
 * 
 * Main class of the project.
 * This class allows to create the indexer, the searcher and calculate the performance.
 */

package assignment1Lucene;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LuceneTester {
	
	 String indexDir = "src\\index";
	 String data_xml = "src\\Posts.xml";
	 //String data_xml = "src\\Posts_big.xml";
	 Indexer indexer;
	 Searcher searcher;
	 PerformanceTester pTester;
	 
	 public static void main(String[] args) {
		 LuceneTester tester;      
	      
	      try {
	         tester = new LuceneTester();
	         NodeList nList = tester.readXML();
	         tester.createIndex(nList);
	         
	         Scanner input = new Scanner(System.in);
	    	 System.out.println("\nDo you want calcuate the performance or do a query?\nInsert P for performance or Q for a query");
	    	 String choice = input.nextLine();
	    	 
	    	 switch (choice)
	    	 {
	    	      case "P","p":
	    	    	tester.calculatePerformance(nList);
	    	      	break;
	    	      
	    	      case "Q","q":
	    	    	  System.out.println("\nInsert the query");
	    	          String querystr = input.nextLine();
	    	          tester.search(querystr);
	    	          break;
		    		  
	    	      default:
	    	    	  System.out.print("Error: you have not chosen an available option.");
	    	 }
	    	 
	      } catch (Exception e) {
	         e.printStackTrace();}
	 }
	 
	 /*
	  * Private method to read a XML file.
	  * 
	  * @return a NodeList where each element is a post of the dataset.
	  */
	 private NodeList readXML() throws ParserConfigurationException, SAXException, IOException {
		 File inputFile = new File(data_xml);
	     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder dBuilder;
	     dBuilder = dbFactory.newDocumentBuilder();
	     org.w3c.dom.Document doc = dBuilder.parse(inputFile);
	     doc.getDocumentElement().normalize();
	    
	     System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
	     NodeList nList = doc.getElementsByTagName("row");
	     
	     return nList;
	 }
	 
	 /*
	  * Private method to create the indexes.
	  * 
	  * It creates an Indexer object with the directory's path we chose to store the indexes.
	  * After the creation of the indexes it prints the number of file indexed.
	  * 
	  * @param NodeList of the posts
	  */
	 private void createIndex(NodeList nList) throws IOException, ParserConfigurationException, SAXException { 
		 indexer = new Indexer(indexDir);
	     int numIndexed = indexer.createIndex(nList);
	     indexer.close();
	     System.out.println(numIndexed + " File indexed");		
	 }
	 
	 /*
	  * Private method to search a query.
	  * 
	  * It creates a Searcher object with the directory's path we chose to store the indexes.
	  * It calls the queryParser function to search the query.
	  * 
	  * @param query string to search
	  */
	 private void search(String searchQuery) throws IOException, ParseException {
	      searcher = new Searcher(indexDir);
	      searcher.queryParser(searchQuery);
	 }
	 
	 /*
	  * Private method to calculate the performance of the code.
	  * 
	  * It creates a PerformanceTester object with the directory's path we chose to store the indexes and the NodeList of the posts.
	  * It calls the testPerformance function to test the performance of the code.
	  * 
	  * @param NodeList of the posts
	  */
	 private void calculatePerformance(NodeList nList) throws IOException, ParseException {
		 pTester = new PerformanceTester(nList, indexDir);
		 pTester.testPerformance();
	 }
	 
}
