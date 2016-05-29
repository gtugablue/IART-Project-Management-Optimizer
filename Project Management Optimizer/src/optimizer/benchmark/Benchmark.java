package optimizer.benchmark;

public abstract class Benchmark implements Runnable {
	protected long score = 0;
	
	@Override
	public abstract void run();
	
	public long getScore() {
		return this.score;
	}
}
