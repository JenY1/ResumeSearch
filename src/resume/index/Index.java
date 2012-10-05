package resume.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import au.com.bytecode.opencsv.CSVReader;

public class Index {
	
	/*public Index() {
		try { 
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			
			index = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	
			IndexWriter w = new IndexWriter(index, config);
			
			File dir = new File("Resumes");
			for (File child : dir.listFiles()) {
			    addDoc(w, child.getName(), child.getPath());
			}
			
			w.close();
		} catch (Exception ex) { 
			ex.getMessage();
		}
	}*/
	
	public Directory getIndex() { 
		try {
			
			// Stopwords 
			CharArraySet stopWords = new CharArraySet(Version.LUCENE_40, 0, true); 
			CSVReader reader = new CSVReader(new FileReader("yourfile.csv"));
		    String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	for (String line : nextLine) { 
		    		stopWords.add(line);
		    	} 
		    }
			
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40, stopWords);
			
			// Store the index in memory 
			Directory index = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	
			IndexWriter w = new IndexWriter(index, config);
			
			File dir = new File("Resumes");
			String test = dir.getPath(); 
			for (File child : dir.listFiles()) {
				w.addDocument(addDoc(w, child.getName(), child.getPath()));
			}
			w.close();
			
			return index; 
		} catch (Exception ex) { 
			ex.getMessage();
		}
		return null;
    }
	
    private Document addDoc(IndexWriter w, String fileName, String path) throws IOException {
	   String fileType = fileName.split("[.]")[1];
	   String text = ""; 
	   if (fileType == "doc") { 
		  	POIFSFileSystem fs1 = new POIFSFileSystem(new FileInputStream(path));
			WordExtractor extractor1 = new WordExtractor(fs1);
			text = extractor1.getText();
	   } else if (fileType == "docx") { 
			XWPFDocument doc = new XWPFDocument(new FileInputStream(path));
			XWPFWordExtractor ex = new XWPFWordExtractor(doc);
			text = ex.getText();
	   }
	   
	   // remove stop words 
	   // stemmer 
		
	   Document doc = new Document();
	   doc.add(new Field(fileName, text, TextField.TYPE_STORED));
	   return doc;
    }

}
