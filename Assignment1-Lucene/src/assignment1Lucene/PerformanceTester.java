/*
 * @file	PerformanceTester.java
 * 
 * This class allows to calculate the performance of the code.
 */

package assignment1Lucene;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class PerformanceTester {
	
	private List<String> titles;
	IndexSearcher searcher;
	IndexReader reader;

	/*
	 * Constructor of the class PerformanceTester.
	 * 
	 * It creates the objects that will be used to calculate the performance and creates an ArrayList of all the posts's titles. 
	 * 
	 * @param NodeList of the nodes
	 * @param indexes directory's path
	 */
	public PerformanceTester(NodeList nList, String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
		reader = DirectoryReader.open(indexDirectory);
	    searcher = new IndexSearcher(reader);
		
	    titles = new ArrayList();
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				titles.add(eElement.getAttribute("Title"));
	        }
	     }
	}
	
	/*
	 * Private function to check if the title searched is in the first ten results.
	 * 
	 * @param title to search
	 * @return value that indicates if the title has been found in the first ten results
	 */
	private int queryParser(String querystr) throws IOException, ParseException {
		int hitsPerPage = 10;
		int ans = 0;
		Boolean check = false;
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		//EnglishAnalyzer analyzer = new EnglishAnalyzer();
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		String[] fields = {"body", "tags"};
		
		Query query = new MultiFieldQueryParser(fields, analyzer).parse(QueryParser.escape
				(querystr.replaceAll("OR","or").replaceAll("AND","and").replaceAll("NOT", "not")));
	    
		searcher.search(query, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    List <String> results = new ArrayList();
	    
	    for(int i=0;i<hits.length;++i) {
	      ans = 0;
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      results.add(d.get("title"));
	    }
	    
	    check = results.contains(querystr);
	      
	    if(check) {
	    	ans=1;
	    }
	    else {
	    	ans=0;
	    }
	    
	    return ans;
	}
	
	
	/*
	 * Function to test the performance of the code.
	 * 
	 * It prints the number of times the titles of the posts have been found in the search.
	 */
	public void testPerformance() throws IOException, ParseException {
		String title;
	    int totRight = 0;
	    int tot = 0;
	    
	    for(int temp = 0; temp<titles.size(); temp++) {
	    	title = titles.get(temp);
	    	if(!title.equals("") && !title.isBlank()) {
	    		tot = tot + 1;
	    		totRight += queryParser(title);
	    	}
	    }
	    
	    System.out.print(totRight + " on " + tot);
	}
	
}
