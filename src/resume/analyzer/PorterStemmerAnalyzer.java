package resume.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class PorterStemmerAnalyzer extends Analyzer {
	
	private CharArraySet stopSet;
	
	public static final CharArraySet STOP_WORDS = 
			StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	
	/*public PorterStemmerAnalyzer() {
	   this(STOP_WORDS);
	}*/

    public PorterStemmerAnalyzer(String[] stopWords) {
	    stopSet = StopFilter.makeStopSet(Version.LUCENE_40, stopWords);
	 }

	@Override
	protected TokenStreamComponents createComponents(String fileName, Reader reader) {
		TokenStream result = new StandardTokenizer(Version.LUCENE_40, reader);
		result = new StandardFilter(Version.LUCENE_40, result);
		result = new LowerCaseFilter(Version.LUCENE_40, result);
	    result = new PorterStemFilter(result);
		result = new StopFilter(Version.LUCENE_40, result, stopSet);
		return result;
	}
	
}
