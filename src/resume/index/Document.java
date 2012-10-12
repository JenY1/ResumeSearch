package resume.index;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Represents a Document that has only been read from a file.
 * This says nothing about the contents of the document, except what they are.
 */
public class Document
    {
	/** The contents of DOCNO */
    public String docno;

	/** A unique identifier for this document. */
    public String id;

	/** The date field of this document. */
    public String date;

	/** The title of this document. */
    public String title;

	/** The body of the document, with any SGML tags stripped out. */
    public String body;

	/** Other tags which are not explicitly stored. */
    public Hashtable other;

    /** The file in which this Document is located. */
    public String filename;

    /** Returns a document number created from the filename */
	public String getDocNo() { 
		if (filename != null && filename != "") { 
			try {
				Pattern MY_PATTERN = Pattern.compile(java.util.regex.Pattern.quote("[^\\]*$"));
				String name = filename; 
				Matcher m = MY_PATTERN.matcher(filename);
				while (m.find()) {
					name = m.group(1);
				}	
				return name.replace(" ", ""); 
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
		}
		return UUID.randomUUID().toString();
	}
    }