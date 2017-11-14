import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.stream.IntStream;

import AugmentedTree.IntervalTree;

public class Transcript extends RegionVector{
	private IntervalTree<IntronRegion> introns; 
	private String id;
	
	private StringBuilder cdsSeq;
	private StringBuilder seq;

	public Transcript(int start, int stop, String id)  {
		super(start, stop);
		this.id = id;
	}
	
	public IntervalTree<IntronRegion> getIntrons(){
		return introns;
	}
	
	public boolean equals(Object o){
		if(o.getClass() == this.getClass()){
			return equals((Transcript) o);
		}
		return false;
	}
	
	public String getId(){
		return id;
	}
	
	public int[] getMutPos(int fraglength, double mutRate){
		double nd =  (fraglength*mutRate)/100.0;
		int n = (int) Math.round(nd);
		int[] mutpos = new Random().ints(0, fraglength).distinct().limit(n).toArray();
		return mutpos;
	}
	
	public StringBuilder mutate(int[] mutPos, StringBuilder frag){
		String fragString = frag.toString();
		Random r = new Random();
		int rInt;
		char mut;
		for(int i : mutPos){
			rInt = r.nextInt(100);
			mut = fragString.charAt(i);
			if(mut == 'A'){
				if(rInt < 50){
					frag.replace(i, i, "T");
				}
				else if(rInt < 75){
					frag.replace(i, i, "C");					
				}
				else{
					frag.replace(i, i, "G");
				}
			}
			else if(mut == 'T'){
				if(rInt < 50){
					frag.replace(i, i, "A");
				}
				else if(rInt < 75){
					frag.replace(i, i, "C");					
				}
				else{
					frag.replace(i, i, "G");
				}
			}
			else if(mut == 'C'){
				if(rInt < 50){
					frag.replace(i, i, "G");
				}
				else if(rInt < 75){
					frag.replace(i, i, "A");					
				}
				else{
					frag.replace(i, i, "T");
				}
			}
			else{
				if(rInt < 50){
					frag.replace(i, i, "C");
				}
				else if(rInt < 75){
					frag.replace(i, i, "A");					
				}
				else{
					frag.replace(i, i, "T");
				}
			}
		}
		return frag;
	}
	
	public StringBuilder revComp(StringBuilder frag){
		StringBuilder revComp = new StringBuilder();
		String revFrag = frag.reverse().toString();
		for(int i = 0; i < revFrag.length(); i++){
			char c = revFrag.charAt(i);
			if(c == 'A'){
				revComp.append('T');
			}
			else if( c == 'T'){
				revComp.append('A');
			}
			else if( c == 'C'){
				revComp.append('G');
			}
			else{
				revComp.append('C');
			}
		}
		return revComp;
	}
	
	
	public boolean equals(Transcript t){
		return this.id.equals(t.getId());
	}
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public String toString(){
		return "Transcript: " + super.toString() + " " + id;
	}

	public void setSeq(StringBuilder transcriptSeq) {
		this.seq = transcriptSeq;
		
	}
	
	public void setIntrons(){
		IntervalTree<IntronRegion> result = new IntervalTree<IntronRegion>();
		ArrayList<Region> regionsArray = this.getRegions();
		regionsArray.sort(new StartRegionComparator());
		HashSet<IntronRegion> resultSet = new HashSet<IntronRegion>();
		int start;
		int stop;
		for(int i = 1; i < regionsArray.size(); i++){
			Region last = regionsArray.get(i-1);
			Region cur = regionsArray.get(i);
			start = last.getStop();
			stop = cur.getStart();
			int pos = start +1 - this.getStart();
			resultSet.add(new IntronRegion(pos,start+1,stop));
		}
		result.addAll(resultSet);
		
	}
	
	public void setCDS(){
		ArrayList<Region> regionsArray = this.getRegions();
		regionsArray.sort(new StartRegionComparator());
		String seqString = seq.toString();
		cdsSeq = new StringBuilder();		
		for(Region region : regionsArray){
			cdsSeq.append(seqString.substring(region.getStart()-this.getStart(), region.getStop()-this.getStart()));
		}
	}
	
	class StartRegionComparator implements Comparator<Region>
	{
	    public int compare(Region x1, Region x2)
	    {
	        return x1.getStart() - x2.getStart();
	    }
	}
	
	
}
