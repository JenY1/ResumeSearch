package resume.search;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class Search {
	// get index 
	// get query 
	// search 
	
	
	public static Result SearchResumes(Query q, String stopPath, String indexPath) {
		try { 
		    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		    
		    org.apache.lucene.search.Query query = new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(q.getQuery());

		    int hitsPerPage = 10; 
		    TopDocs results = searcher.search(query, hitsPerPage);
		    ScoreDoc[] hits = results.scoreDocs;
  
		    int numTotalHits = results.totalHits;
		    System.out.println(numTotalHits + " total matching documents");

		    Result r = new Result(hits, searcher); 

		    return r; 
		} catch(Exception ex) { 
			System.out.println(ex.getMessage());
		}
		return null; 
	}
}
