package resume.search;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public class Result {
	
	ScoreDoc[] hits; 
	IndexSearcher searcher;
	
	public Result(ScoreDoc[] hits, IndexSearcher searcher) {
		this.hits = hits; 
		this.searcher = searcher; 
	}
	
	public ScoreDoc[] getHits() { 
		return hits;
	}
	
	public void setHits(ScoreDoc[] hits) { 
		this.hits = hits; 
	}
	
	public IndexSearcher getSearcher() { 
		return searcher; 
	}
	
	public void setSearcher(IndexSearcher s) { 
		this.searcher = s; 
	}

}
