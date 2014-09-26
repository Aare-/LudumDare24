package pl.subfty.sub.utils.vac;

public class V {
	private int d;
	public int b;
	
	public void put(int v){
		b = v;
		d = (v);
	}
	public int get(){
		return (d);
	}
	public void add(int i){
		put(get()+i);
	}
}
