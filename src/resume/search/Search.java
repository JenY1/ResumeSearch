package resume.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

import resume.index.Index;



public class Search {
	// get index 
	// get query 
	// search 
	
	
	public static Result SearchResumes(Query q) {
		try { 
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			
			Index index = new Index(); 
		    org.apache.lucene.search.Query query = new QueryParser(Version.LUCENE_40, "title", analyzer).parse(q.getQuery());
		    
		    int hitsPerPage = 10;
		    DirectoryReader reader = DirectoryReader.open(index.getIndex());
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    Result r = new Result(hits, searcher);
		    return r; 
		} catch(Exception ex) { 
			System.out.println(ex.getMessage());
		}
		return null; 
	}
}
