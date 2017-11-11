
public class IntronRegion extends Region {
	
	private int istart;
	private int istop;

	public IntronRegion(int pos, int istart,int  istop) {
		super(pos, pos);
		this.istart = istart;
		this.istop = istop;
	}

	public int getIntronStart(){
		return istart;
	}
	
	public int getIntronStop(){
		return istop;
	}
	
}
