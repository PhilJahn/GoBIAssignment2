import java.util.ArrayList;
import java.util.Collection;

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
