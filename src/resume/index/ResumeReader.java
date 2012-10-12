package resume.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.UUID;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import edu.tcnj.documentretrival.DocumentReader;

public class ResumeReader implements DocumentReader {

	String filename; 
	RandomAccessFile file;
	long randomAccessTime;
	long sequentialAccessTime;
	long cleanupTime;
	int documents;
	
	public ResumeReader(String filename) 
	{
		// initialize performance stats
		randomAccessTime = 0;
		sequentialAccessTime = 0;
		cleanupTime = 0;
		documents = 0;
		
		// get the absolute path
		try
		{
			this.filename = (new File(filename)).getCanonicalPath();
		}
		catch (Exception e)
		{
			this.filename = filename;
		}
		
		try
		{
			file = new RandomAccessFile(filename, "r");
		}
		catch (IOException e)
		{
			System.out.println("IO Error: Could not open file");
		}
	}
	
	public Document readDocument() {
		// parse the next document from the file
		long t = System.currentTimeMillis();
		t = System.currentTimeMillis() - t;
		sequentialAccessTime += t;
		
		// log time for random access
		t = System.currentTimeMillis();
		
		// define a new document
		Document current = new Document();

		try { 
		  // set the filename
		   current.filename = filename;
		   current.docno = current.getDocNo(); 
		   String fileType = filename.split("[.]")[1];
		   if (fileType.equals("doc")) { 
			  	POIFSFileSystem fs1 = new POIFSFileSystem(new FileInputStream(filename));
				WordExtractor extractor1 = new WordExtractor(fs1);
				current.body = extractor1.getText();
		   } else if (fileType.equals("docx")) { 
				XWPFDocument doc = new XWPFDocument(new FileInputStream(filename));
				XWPFWordExtractor ex = new XWPFWordExtractor(doc);
				current.body = ex.getText();
		   }
		   return current;
		} catch (Exception ex) { 
			System.out.println(ex.getMessage()); 
		}
		t = System.currentTimeMillis() - t;
		cleanupTime += t;

		return current;
	}
	
	/** display performance statistics to standard output */
	public void showStats() {
		long totalTime = randomAccessTime + sequentialAccessTime + cleanupTime;
		System.out.println("Time spent in SGMLReader: " + totalTime);
		System.out.println("Time spent doing random access: " + (randomAccessTime / (double)totalTime));
		System.out.println("Time spent doing sequential access: " + (sequentialAccessTime / (double)totalTime));
		System.out.println("Time spent doing cleanup: " + (cleanupTime / (double)totalTime));
	}
	
	
}
