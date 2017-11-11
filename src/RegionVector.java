import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import AugmentedTree.IntervalTree;

public class RegionVector extends Region{
	
//Start inclusive, Stop exclusive


	private IntervalTree<Region> regions;
	
	public RegionVector(int x1, int x2){
		super(x1,x2);
		regions = new IntervalTree<Region>();
	}
	
	public RegionVector(IntervalTree<Region> region){
		super(region.getStart(),region.getStop());
		this.regions = region;
	}
	
	
	public Region[] getRegionsArray(){
		return regions.toArray(new Region[0]);
	}
	
	public IntervalTree<Region> getRegionsTree(){
		return regions;
	}
	
	public ArrayList<Region> getRegions(){
		ArrayList <Region> r = new ArrayList<Region>();
		Region[] regionArray = regions.toArray(new Region[0]);
		for(int i = 0; i < regionArray.length; i++){
			r.add(regionArray[i]);
		}
		return r;
	}
	
	public RegionVector invert(){
		IntervalTree<Region> result = new IntervalTree<Region>();
		ArrayList<Region> regionsArray = new ArrayList<Region>(regions);
		regionsArray.sort(new StartRegionComparator());
		HashSet<Region> resultSet = new HashSet<Region>();
		int start;
		int stop;
		for(int i = 1; i < regionsArray.size(); i++){
			Region last = regionsArray.get(i-1);
			Region cur = regionsArray.get(i);
			start = last.getStop();
			stop = cur.getStart();
			resultSet.add(new Region(start+1,stop));
		}
		result.addAll(resultSet);
		return new RegionVector(result);
	}
	
	public int coveredLength(){
		int l = 0;
		
		Iterator<Set<Region>> iterator = regions.groupIterator();
		while(iterator.hasNext()){
			Collection<Region> overlap = (Collection<Region>) iterator.next();
			Vector<Region> overlapVector = new Vector<Region>(overlap);
			int start = overlapVector.get(0).getStart();
			overlapVector.sort(new StopRegionComparator());
			int stop = overlapVector.lastElement().getStop();
			l += stop-start;
		}
		
		return l;
	}
	
	public boolean add(Region region){
		if(region.getStart() < this.getStart()){
			this.setStart(region.getStart());
		}
		if(region.getStop() > this.getStop()){
			this.setStop(region.getStop());
		}
		return regions.add(region);
	}
	
	public boolean remove(Region region){
		return regions.remove(region);
	}
	
	public String toString(){
		return super.toString() + "\n" + regions.toTreeString();
	}
	
	class StartRegionComparator implements Comparator<Region>
	{
	    public int compare(Region x1, Region x2)
	    {
	        return x1.getStart() - x2.getStart();
	    }
	}
	
	class StopRegionComparator implements Comparator<Region>
	{
	    public int compare(Region x1, Region x2)
	    {
	        return x1.getStop() - x2.getStop();
	    }
	}
	

}
