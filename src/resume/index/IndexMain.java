package resume.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.tcnj.documentretrival.BuildSettings;
import edu.tcnj.documentretrival.BuildThread;
import edu.tcnj.documentretrival.DocumentIndex;
import edu.tcnj.documentretrival.InvertedIndex;

import au.com.bytecode.opencsv.CSVReader;

public class IndexMain {
	
	public static final String INDEX_DIRECTORY = "Index";
	
	public static void createIndex() throws IOException { 
		long time = System.currentTimeMillis(),
		threadTime = 0,
		startupTime = System.currentTimeMillis(),
		buildTime = 0,
		storeTime = 0;
		
		CSVReader reader = new CSVReader(new FileReader("stopwords.csv"));
	    String [] nextLine;
	    List<String> stopWordList = new ArrayList(); 
	    while ((nextLine = reader.readNext()) != null) {
	    	for (String line : nextLine) { 
	    		stopWordList.add(line);
	    	} 
	    }
	    
	    InvertedIndex invInd = new InvertedIndex();
		DocumentIndex docInd = new DocumentIndex();
		
		if (BuildSettings.appendToInvertedIndex)
		{
		try
			{
			invInd.load(BuildSettings.invertedIndexFilename);
			}
		catch (IOException e)
			{
			System.out.println("Error loading file '" + BuildSettings.invertedIndexFilename + "'");
			}
		}

	// optionally append to the document index
	if (BuildSettings.appendToDocumentIndex)
		{
		try
			{
			docInd.load(BuildSettings.documentIndexFilename);
			}
		catch (IOException e)
			{
			System.out.println("Error loading file '" + BuildSettings.documentIndexFilename + "'");
			}
		}
	
		List<String> filePaths = new ArrayList<String>(); 
		File dir = new File(BuildSettings.searchDirectory); 
		for (File f : dir.listFiles()) { 
			if ((f.getPath().contains("doc")) && (!f.getPath().contains("~$"))) {
				filePaths.add(f.getPath());
			}
		}
		

		// number of threads
		int threads = BuildSettings.NumberOfThreads;
		// the threads
		BuildThread[] threadGroup = new BuildThread[threads];

		// the beginning of the list of files
		int s = BuildSettings.startOfList;
		// the total number of files
		int numFiles = filePaths.size();
		// the rounded number of files per thread (used for (n-1) threads
		int filesPerThread = (int)Math.round(numFiles / (double)threads);

		// this will contain all of the files to be assigned to a given thread
		String[] stripe = filePaths.toArray(new String[] {});
		
		

		// handle the n-1 threads
		for (int i = 0; i < threads - 1; i++)
			{
			stripe = new String[filesPerThread];
			for (int j = i * filesPerThread; j < (i+1) * filesPerThread; j++)
			{
				stripe[j - i * filesPerThread] = filePaths.get(j);
			}
			// create the new thread with it's files
			threadGroup[i] = new BuildThread(stripe);
			}
			
		// give leftovers to the last thread
		// temp is the starting point within the list of files
		int temp = (threads - 1) * filesPerThread;
		stripe = new String[numFiles - temp];
		for (int j = temp; j < numFiles; j++)
		{
			stripe[j - temp] = filePaths.get(j);
		}
		// create the final thread
		threadGroup[threads - 1] = new BuildThread(stripe);

		// store the time it took to divvy up the work
		startupTime = System.currentTimeMillis() - startupTime;

		// initialize the time it takes for threads to complete
		threadTime = System.currentTimeMillis();

		// minimize the priority of this thread for now
		// because we don't want CPU time in this thread
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// loop through and start all of the threads
		for (int i = 0; i < threads; i++)
			{
			threadGroup[i].invInd = invInd;		// assign the inverted index
			threadGroup[i].docInd = docInd;		// assign the document index

			// maximize its priority so that it gets even more CPU
			// than this thread
			threadGroup[i].setPriority(Thread.MAX_PRIORITY);

			// start this thread
			threadGroup[i].start();
			}

		for (int i = 0; i < threads; i++)
			{
			// yield() means preempt yourself
			// in more words, give the CPU to someone else while there is
			// someone else
			while (threadGroup[i].isAlive())
				Thread.currentThread().yield();
			}

		// compute the time it took for all threads to finish
		threadTime = System.currentTimeMillis() - threadTime;

		// store the indices
		// time it takes to store the files
		storeTime = System.currentTimeMillis();
		try
			{
				invInd.store(BuildSettings.invertedIndexFilename);
			}
		catch (IOException e)
			{
			System.out.println("Error storing file '" + BuildSettings.invertedIndexFilename + "'");
			}

		try
			{
				docInd.store(BuildSettings.documentIndexFilename);
			}
		catch (IOException e)
			{
			System.out.println("Error storing file '" + BuildSettings.documentIndexFilename + "'");
			}
		// compute the time it took to store the indices
		storeTime = System.currentTimeMillis() - storeTime;

		// compute total execution time
		time = System.currentTimeMillis() - time;
		// show performance computations
		if (BuildSettings.printTimes)
		{
			System.out.println("Thread setup %:\t" + (startupTime / (double) time));
			System.out.println("Parallel execution %:\t" + (threadTime / (double)time));
			System.out.println("Index storage %:\t" + (storeTime / (double)time));
			System.out.println("Total exectution time:\t" + time);
		}
	}
}

