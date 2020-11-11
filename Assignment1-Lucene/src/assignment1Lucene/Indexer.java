/*
 * @file	Indexer.java
 * 
 * This class allows to create the indexes of the documents.
 */

package assignment1Lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;


public class Indexer {
	
	private IndexWriter w; 
	
	/*
	 * Constructor of the class Indexer.
	 * 
	 * It creates the objects that will be used to create the indexes. 
	 * 
	 * @param indexes directory's path chosen
	 */
	public Indexer(String indexDirectoryPath) throws IOException {
		 Path path= Paths.get(indexDirectoryPath);
		 Directory indexDirectory =  FSDirectory.open(path);
		 
		 StandardAnalyzer analyzer = new StandardAnalyzer();
		 //EnglishAnalyzer analyzer = new EnglishAnalyzer();
		 IndexWriterConfig config = new IndexWriterConfig(analyzer);
		 w = new IndexWriter(indexDirectory, config);
		 w.deleteAll();
	     w.commit();
		 
	}
	
	/*
	 * Function to close the object IndexWriter
	 */
	public void close() throws IOException{
	      w.close();
	}
	
	/*
	 * Private function to index the documents.
	 * 
	 * @param NodeList of the posts
	 */
	private void indexFile(NodeList nList) throws ParserConfigurationException, SAXException, IOException {
		
	    for (int temp = 0; temp < nList.getLength(); temp++) {
          Node nNode = nList.item(temp);
        
          if (nNode.getNodeType() == Node.ELEMENT_NODE) {
             Element eElement = (Element) nNode;
             addDoc(w, eElement.getAttribute("Body"),
            		 eElement.getAttribute("Title"),
            		 eElement.getAttribute("Id"),
            		 eElement.getAttribute("Tags"));
          }
	     }
	}
	
	/*
	 * Private function to add a document to the IndexWriter object
	 * 
	 * @param the IndexWriter object
	 * @param body of the post
	 * @param title of the post
	 * @param id of the post
	 * @param tags of the post
	 */
	private static void addDoc(IndexWriter w, String body, String title, String id, String tags) throws IOException {
	    Document doc = new Document();
	    doc.add(new TextField("body", body, Field.Store.YES));
	    doc.add(new StringField("title", title, Field.Store.YES));
	    doc.add(new StringField("id", id, Field.Store.YES));
	    doc.add(new TextField("tags", tags, Field.Store.YES));
	    w.addDocument(doc);
	}
	
	/*
	 * Function to create the indexes and return the number of indexed documents.
	 * 
	 * @param NodeList of the posts
	 * @return number of the indexed documents.
	 */
	public int createIndex(NodeList nList) throws IOException, ParserConfigurationException, SAXException {
		indexFile(nList);
		return w.numDocs();
	}
	
	
}
