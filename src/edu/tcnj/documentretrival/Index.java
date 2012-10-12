/**
 * Source from: http://www.tcnj.edu/~mmmartin/keith/Index.java
 */

package edu.tcnj.documentretrival;
import java.io.*;

/** a template for how an Index should be implemented */
public interface Index {
	/** build the index using database statistics */
    public void build(DatabaseStatistics s);

	/** load the index from a file */
	public void load(String filename) throws IOException;

	/** store the index in a file */
	public void store(String filename) throws IOException;
}