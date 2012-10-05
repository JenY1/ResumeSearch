package resume.search;

public class Query {
	// get any information like name or skill 
	// Stem the query 
	String query = null;
	
	public Query (String q) {
		query = q;
	}
	
	public String getQuery() { 
		return query; 
	}
	
	public void setQuery(String q) { 
		query = q; 
	}
}
