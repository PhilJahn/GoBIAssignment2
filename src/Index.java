
public class Index {
	private long start;
	private long length;
	private int line;
	private int cont;
	
	public Index(long start, long length, int line, int cont){
		this.start = start;
		this.length = length;
		this.line = line;
		this.cont = cont;
	}
	
	public long getStart(){
		return start;
	}
	
	public long getLength(){
		return length;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getCont(){
		return cont;
	}
}
