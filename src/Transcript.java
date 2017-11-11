import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.stream.IntStream;

import AugmentedTree.IntervalTree;

public class Transcript extends RegionVector{
	
	private IntervalTree<Region> introns; 
	private String id;

	public Transcript(int start, int stop, String id)  {
		super(start, stop);
		this.id = id;
	}
	
	
	public boolean setIntrons(){
		if(this.getRegionsTree().size() > 1){
			introns = this.invert().getRegionsTree();
			return true;
		}
		else{
			return false;
		}
	}
	
	public IntervalTree<Region> getIntrons(){
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
	
}
