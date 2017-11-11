
public class Index {
	private int start;
	private int length;
	private int line;
	private int cont;
	
	public Index(int start, int length, int line, int cont){
		this.start = start;
		this.length = length;
		this.line = line;
		this.cont = cont;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getLine(){
		return line;
	}
	
	public int getCont(){
		return cont;
	}
}
