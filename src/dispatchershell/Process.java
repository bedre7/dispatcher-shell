package dispatchershell;

public class Process implements IProcess {
	public int Id;
	public String color;
	public int arrivalTime;
	public Priority priority;
	public int burstTime;
	@Override
	public boolean isRealTime() {
		
		return this.priority == Priority.REALTIME;
	}
	
}