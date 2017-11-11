import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ReadSimulator {

	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();		
		int readLength = -1;
		int mean = -1;
		int sd = -1;
		double mutRate = -1;
		String readcountsPath ="";
		String fastaPath ="";
		String fidxPath ="";
		String gtfPath ="";
		String outputPath ="";
		for(int i =0; i < args.length-1; i++){
			if(args[i].equals("-length")){
				readLength = Integer.parseInt(args[i+1]);
				i++;
			}
			else if(args[i].equals("-frlength")){
				mean = Integer.parseInt(args[i+1]);
				sd = Integer.parseInt(args[i+3]);
				i += 3; 
			}
			else if(args[i].equals("-mutationrate")){
				mutRate = Double.parseDouble(args[i+1]);
				i++; 
			}
			else if(args[i].equals("-readcounts")){
				readcountsPath = args[i+1];
				i++; 
			}
			else if(args[i].equals("-fidx")){
				fidxPath = args[i+1];
				i++; 
			}
			else if(args[i].equals("-gtf")){
				gtfPath = args[i+1];
				i++; 
			}
			else if(args[i].equals("-od")){
				outputPath = args[i+1];
				i++; 
			}
		}
		
		if( readLength == -1 || mean == -1  || sd == -1 || mutRate == -1 || readcountsPath.equals("") || fastaPath.equals("") || fidxPath.equals("") || gtfPath.equals("") ||outputPath.equals("") ){
			System.out.println("Usage Info:\n-length <Read Length>\n-frlength <mean> -SD <SD>\n-muattionrate <mutation rate>\n-readcounts <filepath for read counts>\n-fasta <filepath for FASTA>\n-fidx <filepath for FASTA index>\n-gtf <filepath for GTF>\n-od <output directory>");
		}
		else{
			ReadSimulator rs = new ReadSimulator(readLength, mean, sd, mutRate, readcountsPath, fastaPath, fidxPath, gtfPath);
			String[] outputs = rs.getSimulatedReads();
			ArrayList<String> fwList = new ArrayList<String>();
			fwList.add(outputs[0]);
			ArrayList<String> rwList = new ArrayList<String>();
			rwList.add(outputs[1]);
			ArrayList<String> infoList = new ArrayList<String>();
			infoList.add(outputs[2]);
			
			Path fwfilePath = Paths.get(outputPath.concat("fw.fastaq"));
			Path rwfilePath = Paths.get(outputPath.concat("rw.fastaq"));
			Path infofilePath = Paths.get(outputPath.concat("read.mappinginfo"));
			try {
				Files.write(fwfilePath, fwList, Charset.forName("UTF-8"));
				Files.write(rwfilePath, rwList, Charset.forName("UTF-8"));
				Files.write(infofilePath, infoList, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		long stopTime = System.currentTimeMillis();
//		System.out.println("Input:" + (stopTime-startTime));	
	}

	private int readLength;
	private int mean;
	private int sd;
	private double mutRate;
	private String readcountsPath;
	private String fastaPath;
	private String fidxPath;
	private String gtfPath;
	
	private HashMap<Integer,Gene> geneSet;
	private HashMap<String,Index> fastaindex;
	private HashMap<String,Integer> readcount;
	private ArrayList<String> order;
	private HashSet<String> geneIds;
	private HashSet<String> transIds;
	
	public ReadSimulator(int readLength, int mean, int sd, double mutRate, String readcountsPath, String fastaPath, String fidxPath, String gtfPath) {
		this.readLength = readLength;
		this.mean = mean;
		this.sd = sd;
		this.mutRate = mutRate;
		this.readcountsPath = readcountsPath;
		this.fastaPath = fastaPath;
		this.fidxPath = fidxPath;
		this.gtfPath = gtfPath;
		getContent();
	}


	public static HashMap<String,String> getAttributes (String[] attrs){
		HashMap <String,String> attrMap = new HashMap <String,String>();
		for(int i = 0; i < attrs.length; i++){
			String curattr = attrs[i];
			curattr = curattr.trim();
			String[] attr = curattr.split("\\s+");
			attr[0] = attr[0].trim();
			attr[1] = attr[1].trim();
			attr[1] = attr[1].replaceAll("\"", "");
			attrMap.put(attr[0],attr[1]);
		}
		return attrMap;
	}
	
	public String[] getSimulatedReads(){
		String tab = "\t";
		String brk = "\n";
		char sep = ':';
		char sip = '|';
		StringBuilder fwBuilder = new StringBuilder("");
		StringBuilder rwBuilder = new StringBuilder("");
		StringBuilder infoBuilder = new StringBuilder("");
		
		return new String[]{fwBuilder.toString(),rwBuilder.toString(),infoBuilder.toString()};
	}
	
	public void getContent(){
		readcountsReader();
		gtfReader();
	}
	
	private void readcountsReader(){
		Path filePath = Paths.get(readcountsPath);
		readcount = new HashMap<String,Integer>();
		geneIds = new HashSet<String>();
		transIds = new HashSet<String>();
		order= new ArrayList<String>();
	    try {
	    	File file = filePath.toFile();
	        BufferedReader br = new BufferedReader (new FileReader(file));
	        String line;
	        line = br.readLine();
	        while ((line = br.readLine()) != null){
	        	String[] lineSplit = line.split("\t");
	        	geneIds.add(lineSplit[0]);
	        	transIds.add(lineSplit[1]);
	        	StringBuilder idBuild = new StringBuilder(lineSplit[0]);
	        	idBuild.append("|");
	        	idBuild.append(lineSplit[1]);
	        	String id =idBuild.toString();
	        	order.add(id);
	        	readcount.put(id,Integer.parseInt(lineSplit[2]));
	        }
	        br.close();	
	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fidxReader(){
		Path filePath = Paths.get(fidxPath);
		fastaindex = new HashMap<String,Index>();
	    try {
	    	File file = filePath.toFile();
	        BufferedReader br = new BufferedReader (new FileReader(file));
	        String line;
	        while ((line = br.readLine()) != null){
	        	String[] lineSplit = line.split("\t");	        	
	        }
	        br.close();	
	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void gtfReader(){
		Path filePath = Paths.get(gtfPath);
		geneSet = new HashMap<Integer,Gene>();
	    try {
	    	File file = filePath.toFile();
	        BufferedReader br = new BufferedReader (new FileReader(file));
	        String line;
	        Gene curGene = null;
	        Transcript curTrans = null;
	        ArrayList<Transcript> unknownGene = new ArrayList<Transcript>();
	        ArrayList<Region> unknownTranscript = new ArrayList<Region>();
	        while ((line = br.readLine()) != null){
	        	String[] lineSplit = line.split("\t");
	        	if(lineSplit.length >= 8){
		        	String[] attrSplit = lineSplit[8].split(";");
		        	HashMap<String,String> attr;      	
		        	if(lineSplit[2].equals("CDS")){
		        		attr = getAttributes(attrSplit);
		        		String super_super_id = attr.get("gene_id");
		        		String gene_name = attr.get("gene_name");
		        		String super_id = attr.get("transcript_id");
		        		String id = attr.get("protein_id");
		        		String type = lineSplit[1];
		        		char strand = lineSplit[6].charAt(0);
		        		String chr = lineSplit[0];
		        		int start = Integer.parseInt(lineSplit[3]);
		        		int stop = Integer.parseInt(lineSplit[4]);
		        		Region cds;
		        	}
	        	}
	        }
	        br.close();	
	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}

}
