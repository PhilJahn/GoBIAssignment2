import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.NormalDistribution;

import AugmentedTree.IntervalTree;

public class Transcript extends RegionVector{
//	private IntervalTree<IntronRegion> introns; 
	private IntervalTree<ExonRegion> exons; 
	private String id;
	
	private String cdsSeq;
	private String seq;

	public Transcript(int start, int stop, String id)  {
		super(start, stop);
		this.id = id;
	}
	
	public IntervalTree<ExonRegion> getExons(){
		return exons;
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
	
	public static int[] getMutPos(int fraglength, double mutRate){
		double nd =  (fraglength*mutRate)/100.0;
		int n = (int) Math.round(nd);
		int[] mutpos = new Random().ints(0, fraglength).distinct().limit(n).toArray();
		return mutpos;
	}
	
	public static StringBuilder mutate(int[] mutPos, StringBuilder frag){
		String fragString = frag.toString();
		Random r = new Random();
		int rInt;
		char mut;
		for(int i : mutPos){
			rInt = r.nextInt(100);
			mut = fragString.charAt(i);
			if(mut == 'A'){
				if(rInt < 50){
					frag.replace(i, i+1, "T");
				}
				else if(rInt < 75){
					frag.replace(i, i+1, "C");					
				}
				else{
					frag.replace(i, i+1, "G");
				}
			}
			else if(mut == 'T'){
				if(rInt < 50){
					frag.replace(i, i+1, "A");
				}
				else if(rInt < 75){
					frag.replace(i, i+1, "C");					
				}
				else{
					frag.replace(i, i+1, "G");
				}
			}
			else if(mut == 'C'){
				if(rInt < 50){
					frag.replace(i, i+1, "G");
				}
				else if(rInt < 75){
					frag.replace(i, i+1, "A");					
				}
				else{
					frag.replace(i, i+1, "T");
				}
			}
			else{
				if(rInt < 50){
					frag.replace(i, i+1, "C");
				}
				else if(rInt < 75){
					frag.replace(i, i+1, "A");					
				}
				else{
					frag.replace(i, i+1, "T");
				}
			}
		}
		return frag;
	}
	
	public static StringBuilder revComp(StringBuilder frag){
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
		this.seq = transcriptSeq.toString();
//		this.setIntrons();
		this.setCDS();
	}
	
//	public void setIntrons(){
//		IntervalTree<IntronRegion> result = new IntervalTree<IntronRegion>();
//		ArrayList<Region> regionsArray = this.getRegions();
//		regionsArray.sort(new StartRegionComparator());
//		HashSet<IntronRegion> resultSet = new HashSet<IntronRegion>();
//		int start;
//		int stop;
//		for(int i = 1; i < regionsArray.size(); i++){
//			Region last = regionsArray.get(i-1);
//			Region cur = regionsArray.get(i);
//			start = last.getStop();
//			stop = cur.getStart();
//			int pos = start +1 - this.getStart();
//			resultSet.add(new IntronRegion(pos,start+1,stop));
//		}
//		result.addAll(resultSet);
//		
//	}
	
	public void setCDS(){
		IntervalTree<ExonRegion> result = new IntervalTree<ExonRegion>();
		HashSet<ExonRegion> resultSet = new HashSet<ExonRegion>();
		ArrayList<Region> regionsArray = this.getRegions();
		regionsArray.sort(new StartRegionComparator());
		String seqString = seq.toString();
		StringBuilder cdsSeqBuilder = new StringBuilder();
		int tstart = this.getStart();
		int etstart;
		int etstop;
		int egstart;
		int egstop;
		
//		System.out.println("Seq Length: " + seqString.length());
//		System.out.println("Trans Length: " + this.length());
//		System.out.println(this.getRegionsTree().toTreeString());
				
		for(Region region : regionsArray){
			etstart= cdsSeqBuilder.length();
			etstop = region.getStop() - region.getStart() + etstart;
			egstart = region.getStart();
			egstop = region.getStop();
			resultSet.add(new ExonRegion(etstart,etstop,egstart,egstop));
			cdsSeqBuilder.append(seqString.substring(region.getStart()-this.getStart(), region.getStop()-this.getStart()+1));
		}
		this.cdsSeq = cdsSeqBuilder.toString();
		result.addAll(resultSet);
		this.exons = result;
	}
	
	
	
	class StartRegionComparator implements Comparator<Region>
	{
	    public int compare(Region x1, Region x2)
	    {
	        return x1.getStart() - x2.getStart();
	    }
	}



	public ArrayList<Fragment> makeFragments(int readLength, double mutRate, int mean, int sd, int n) {
		ArrayList<Fragment> results = new ArrayList<Fragment>();
		
		NormalDistribution nd = new NormalDistribution(mean,sd);
		
		Random r = new Random();
		
//		n = 1;
//		mutRate = 5.0;
		
		for(int i = 0; i < n; i++){
			int fraglength = (int) Math.round(nd.sample());
			
			while(fraglength < readLength || fraglength >= cdsSeq.length()){
				fraglength = (int) Math.round(nd.sample());
			}
			
//			fraglength = 122;
			
//			System.out.println("i: " + i + " fraglength:" + fraglength);
			
			int startPos = r.nextInt(cdsSeq.length() - fraglength);
//			startPos= 145;
//			System.out.println("startPos: " + startPos);
			StringBuilder fragseq = new StringBuilder(cdsSeq.substring(startPos, startPos + fraglength));
//			System.out.println("FragSeq: " + fragseq.toString() + " Length: " + fragseq.toString().length());
			int[] mutPos = getMutPos(fraglength,mutRate);
			
//			System.out.println(Arrays.toString(mutPos));
			Arrays.sort(mutPos);
			fragseq = mutate(mutPos, fragseq);
			
			String fragseqString = fragseq.toString();
			
			int fwstart = startPos + 0;
			int fwstop = startPos + readLength;
			Region fwReg = new Region(fwstart,fwstop);
			
			int rwstart = startPos + fraglength-readLength;
			int rwstop = startPos + fraglength;
			Region rwReg = new Region(rwstart, rwstop);
			
			
			
			String fwread = fragseqString.substring(0, readLength);
			
//			System.out.println("FWRead: "+ fwread + " Length: " + fwread.length());
			
			StringBuilder rwread = new StringBuilder(fragseqString.substring(fraglength-readLength));
//			System.out.println("RWRead1: "+ rwread + " Length: " + rwread.length());
			rwread = revComp(rwread);
			String rwreadString = rwread.toString();
//			System.out.println("RWRead: "+ rwreadString + " Length: " + rwreadString.length());
			
			int j = -1;
			while(j+1 < mutPos.length && mutPos[j+1] < readLength){
				j++;
			}
			
			int[] fwMut= new int[0];
			if(j != -1){
				fwMut = new int[j+1];
				for(int k = 0; k <= j; k++){
					fwMut[k] = mutPos[k];
				}
			}
			
			j = mutPos.length;
			while(j > 0 && mutPos[j-1] > fraglength-readLength){
				j--;
			}
			
			int[] rwMut= new int[0];
			if(j != mutPos.length){
				rwMut = new int[mutPos.length-j];
				for(int k = j; k < mutPos.length; k++){
					rwMut[k-j] = fraglength-1 - mutPos[k];
				}
			}
			
//			System.out.println("CDS: "+  cdsSeq + " Length: " + cdsSeq.length());
//			System.out.println(exons.toTreeString());
			
			ArrayList<Region> fwGene = new ArrayList<Region>();	
			ArrayList<ExonRegion> fwExons = new ArrayList<ExonRegion>();
			fwExons = exons.getIntervalsIntersecting(fwstart, fwstop, fwExons);
			fwExons.sort(new StartRegionComparator());
			
//			System.out.println("fwExons: " + fwExons.toString());
			
			if(fwExons.size() > 1){
				ExonRegion curExon = fwExons.get(0);
				int startExonStart = fwstart-curExon.getStart() + curExon.getExonStart();
				fwGene.add(new Region(startExonStart,curExon.getExonStop()));
				
				for(int h = 1; h < fwExons.size()-1; h ++){
					curExon = fwExons.get(h);
					fwGene.add(new Region(curExon.getExonStart(), curExon.getExonStop()));
				}
				
				curExon = fwExons.get(fwExons.size()-1);
				int startExonStop = fwstop-curExon.getStart() + curExon.getExonStart();
				fwGene.add(new Region(curExon.getExonStart(),startExonStop));
			}
			else{
				ExonRegion curExon = fwExons.get(0);
				int startExonStart = fwstart-curExon.getStart() + curExon.getExonStart();
				int startExonStop = fwstop-curExon.getStart() + curExon.getExonStart();
//				System.out.println(startExonStart);
				
				fwGene.add(new Region(startExonStart,startExonStop));
			}
			
			ArrayList<Region> rwGene = new ArrayList<Region>();	
			ArrayList<ExonRegion> rwExons = new ArrayList<ExonRegion>();
			rwExons = exons.getIntervalsIntersecting(rwstart, rwstop,rwExons);
			rwExons.sort(new StartRegionComparator());
			if(rwExons.size() > 1){
				ExonRegion curExon = rwExons.get(0);
				int startExonStart = rwstart-curExon.getStart() + curExon.getExonStart();
				rwGene.add(new Region(startExonStart,curExon.getExonStop()));
				
				for(int h = 1; h < rwExons.size()-1; h ++){
					curExon = rwExons.get(h);
					rwGene.add(new Region(curExon.getExonStart(), curExon.getExonStop()));
				}
				
				curExon = rwExons.get(rwExons.size()-1);
				int startExonStop = rwstop-curExon.getStart() + curExon.getExonStart();
				rwGene.add(new Region(curExon.getExonStart(),startExonStop));
			}
			else{
				ExonRegion curExon = rwExons.get(0);
				int startExonStart = rwstart-curExon.getStart() + curExon.getExonStart();
				int startExonStop = rwstop-curExon.getStart() + curExon.getExonStart();
				rwGene.add(new Region(startExonStart,startExonStop));
			}
			
			results.add(new Fragment(fwread, rwreadString, fwMut, rwMut, fwReg, rwReg, fwGene, rwGene));
			
		}
		return results;
	}
	
	
}
