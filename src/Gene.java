
import java.util.Comparator;
import java.util.HashMap;

public class Gene extends Region{
	
	private HashMap<String,Transcript> transcripts;
	
	private String id;
	
	private String chr;
	
	public Gene(int x1, int x2, String id, String chr){
		super(x1,x2);
		this.id = id;
		this.chr = chr;
		transcripts = new HashMap <String,Transcript>();
	}

	public Gene(int x1, int x2, String id, String chr, String tid){
		super(x1,x2);
		this.id = id;
		this.chr = chr;
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
}

