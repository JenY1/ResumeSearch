package resume.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;

import resume.search.Query;
import resume.search.Result;



/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
			String query = request.getParameter("q");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter(  );
			Query q = new Query(query);
			String stopWords = getServletContext().getRealPath("/WEB-INF/stopwords.csv");
			String resumes = getServletContext().getRealPath("/WEB-INF/Index");
			Result test = resume.search.Search.SearchResumes(q, stopWords, resumes);
			
		    out.println("Found " + test.getHits().length + " hits.");
		    for(int i=0;i<test.getHits().length;++i) {
		      int docId = test.getHits()[i].doc;
		      Document d = test.getSearcher().doc(docId);
		      out.println((i + 1) + ". " + d.get("title"));
		    }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
