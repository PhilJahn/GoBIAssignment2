
public class ExonRegion extends Region {
	
	private int estart;
	private int estop;
	
	/**
	 * 
	 * @param start start in transcript coordiantes
	 * @param stop stop in transcript coordiantes
	 * @param estart start on genome
	 * @param estop stop on genome
	 */

	public ExonRegion(int start, int stop, int estart,int estop) {
		super(start, stop);
		this.estart = estart;
		this.estop = estop;
	}

	public int getExonStart(){
		return estart;
	}
	
	public int getExonStop(){
		return estop;
	}
	
	public String toString(){
		return "[" + this.getStart() + ";" + this.getStop() + "]("+ this.length() +") -> [" + estart + ";" + estop + "](" + (estop-estart) +")";
	}
}
