package dispatchershell;

public class Process implements IProcess {
	public int Id;
	public String color;
	public int arrivalTime;
	public Priority priority;
	public int burstTime;
	public int elapsedTime;
	@Override
	public boolean isRealTime() {
		
		return this.priority == Priority.REALTIME;
	}
	
}