package dispatchershell;

public interface IProcess {
	public boolean isRealTime();
	public int getId() ;

	public void setId(int id);

	public Color getColor() ;

	public int getArrivalTime() ;

	public void setArrivalTime(int arrivalTime) ;

	public int getBurstTime();

	public void setBurstTime(int burstTime) ;

	public int getElapsedTime() ;

	public void setElapsedTime(int elapsedTime);
	
	public void setPriority(Priority priority) ;
	public Priority getPriority() ;
	public void reducePriority();
}
