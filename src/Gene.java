
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Gene extends Region{
	
	private HashMap<String,Transcript> transcripts;
	
	private String id;
	
	private String chr;
	private Index index;
	
	public Gene(int x1, int x2, String id, String chr, Index index){
		super(x1,x2);
		this.id = id;
		this.chr = chr;
		this.index = index;
		transcripts = new HashMap <String,Transcript>();
	}

	public Gene(int x1, int x2, String id, String chr, Index index, String tid){
		super(x1,x2);
		this.id = id;
		this.chr = chr;
		this.index = index;
		transcripts = new HashMap <String,Transcript>();
		transcripts.put(tid,new Transcript(x1,x2,tid));
	}

	public int getTranscriptNumber(){
		int n = 0;
		for( String k : transcripts.keySet() ){
			if(transcripts.get(k).getRegionsTree().size() > 0){
				n ++;
			}
		}
		return n;
	}
	
	public void removeEmpty(){
		for( String k : transcripts.keySet() ){
			if(transcripts.get(k).getRegionsTree().size() == 0){
				transcripts.remove(k);
			}
		}
	}
	
	public String getId(){
		return id;
	}
	
	public String getChr(){
		return chr;
	}
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public String toString(){
		String output = "Gene: "+ super.toString() + " "+ id  + "\n";
		for( String k : transcripts.keySet() ){
			output += transcripts.get(k).toString();
		}
		return output;
	}

	public void add(Transcript trans) {
		this.transcripts.put(trans.getId(), trans);
	}
	
	class StartRegionComparator implements Comparator<Region>
	{
	    public int compare(Region x1, Region x2)
	    {
	        return x1.getStart() - x2.getStart();
	    }
	}

	public ArrayList<Fragment> generateReads(int readLength, double mutRate, int mean, int sd, RandomAccessFile fastaAccess, String transid, int n) throws Exception {
		Transcript trans = transcripts.get(transid);
		int start = trans.getStart();
		int stop = trans.getStop();
		long fastastart = index.getStart() + start + start/60;
		int fastaLength = stop-start;
//		System.out.println("Chrom Start: " + index.getStart());
//		System.out.println("Trans Start: " + start);
//		System.out.println("FastaStart: " + fastastart);
//		
//		System.out.println("FastaLength: " + fastaLength);
//		System.out.println("Index Line: " + index.getLine());
//		System.out.println("Index Cont: " + index.getCont());
		long modulo = start%index.getLine();
//		System.out.println("Modulo: " + modulo);
		
		long lines = ((modulo+ fastaLength)/index.getCont())+1;
//		System.out.println("Line Count " + lines);
		fastaAccess.seek(fastastart-1);
		
		StringBuilder transcriptSeq = new StringBuilder();
		
		for( int i = 0; i <= lines; i++){
			transcriptSeq.append(fastaAccess.readLine());
		}
		
//		System.out.println("Transcript Seq Length " + transcriptSeq.length());
		
		trans.setSeq(transcriptSeq);
		transcriptSeq.setLength(0);
		return trans.makeFragments(readLength, mutRate, mean, sd, n );
	}
}

