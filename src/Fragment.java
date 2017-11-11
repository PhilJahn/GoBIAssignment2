import java.util.ArrayList;
import java.util.HashSet;

public class Fragment {
	private StringBuilder fwread;
	private StringBuilder rwread;
	
	private int[] fwMut;
	private int[] rwMut;
	
	private Region fwReg;
	private Region rwReg;
	private ArrayList<Region> fwGene;
	private ArrayList<Region> rwGene;
	
	
	
	public Fragment(StringBuilder fwread, StringBuilder rwread, int[] fwmut, int[] rwmut, Region fwReg, Region rwReg, ArrayList<Region> fwGene, ArrayList<Region> rwGene ){
		this.fwread = fwread;
		this.rwread = rwread;
		
		this.fwMut = fwmut;
		this.rwMut = rwmut;
		
		this.fwReg = fwReg;
		this.rwReg = rwReg;
		this.fwGene = fwGene;
		this.rwGene = rwGene;		
	}
	
	public StringBuilder getFWread(){
		return fwread;
	}
	
	public StringBuilder getRWread(){
		return rwread;
	}
	
	public int[] getFWMut(){
		return fwMut;
	}
	
	public int[] getRWMut(){
		return rwMut;
	}
	
	public Region getFWReg(){
		return fwReg;
	}
	
	public Region getRWReg(){
		return rwReg;
	}
	
	public ArrayList<Region> getFWGene(){
		return fwGene;
	}
	
	public ArrayList<Region> getRWGene(){
		return rwGene;
	}
}
