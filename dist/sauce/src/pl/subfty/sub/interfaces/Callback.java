package pl.subfty.sub.interfaces;

public interface Callback {
	public final static int RESTART=0,
							HIT=1,
							MISS=2,
							KILL=3;
	
	public abstract void call(int type);
}