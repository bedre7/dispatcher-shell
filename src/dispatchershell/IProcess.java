package dispatchershell;

public interface IProcess {
	boolean isRealTime();
	public int getId() ;

	public void setId(int id);

	public String getColor() ;

	public void setColor(String color) ;
	public int getArrivalTime() ;

	public void setArrivalTime(int arrivalTime) ;

	public int getBurstTime();

	public void setBurstTime(int burstTime) ;

	public int getElapsedTime() ;

	public void setElapsedTime(int elapsedTime);
	
	public void setPriority(Priority priority) ;
	public Priority getPriority() ;
}
