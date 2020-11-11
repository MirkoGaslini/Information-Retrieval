/*
 * @file	Searcher.java
 * 
 * This class allows to search in the documents a specific query.
 */

package assignment1Lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class Searcher {

	IndexSearcher searcher;
	IndexReader reader;
	
	/*
	 * Constructor of the class Searcher.
	 * 
	 * It creates the objects that will be used to search the query. 
	 * 
	 * @param indexes directory's path
	 */
	public Searcher(String indexDirectoryPath) throws IOException {
		Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
	    reader = DirectoryReader.open(indexDirectory);
	    searcher = new IndexSearcher(reader);
	}
	
	/*
	 * Method to search the query in the documents.
	 * 
	 * It finds the first ten documents that match with the searched query.
	 * 
	 * @param query string to search
	 */
	public void queryParser(String querystr) throws ParseException, IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		String[] fields = {"body", "tags"};
		
		Query query = new MultiFieldQueryParser(fields, analyzer).parse(QueryParser.escape
				(querystr.replaceAll("OR","or").replaceAll("AND","and").replaceAll("NOT", "not")));
		
		int hitsPerPage = 10;
		 
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		displayResults(hits);
	}
	
	/*
	 * Method to display the first ten documents found.
	 * 
	 * @param ScoreDoc object of the ten documents 
	 */
	private void displayResults(ScoreDoc[] hits) throws IOException {
		List <String> results = new ArrayList();
		System.out.println("\nFound " + hits.length + " hits.\n");
		
		for(int i=0; i < hits.length; ++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    results.add(d.get("title"));
		    System.out.println("id: " + docId + " title: \"" + results.get(i) + "\"");
		}
	}
	   
}
