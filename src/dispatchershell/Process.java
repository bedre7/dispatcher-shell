package dispatchershell;

public class Process implements IProcess {
	private int Id;
	private Color color; 
	private int arrivalTime;
	private int burstTime;
	private int elapsedTime;
	private Priority priority;
	
	public Process(int arrivalTime, Priority priority, int burstTime, Color color) {
		this.arrivalTime=arrivalTime;
		this.priority=priority;
		this.burstTime=burstTime;
		this.color=color;
		this.elapsedTime=0;
		
	}
	
	@Override
	public void execute(int quantum) {
		this.elapsedTime += quantum;
	}
	
	
	@Override
	public boolean isRealTime() {
		
		return this.priority == Priority.REALTIME;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Color getColor() {
		return color;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getBurstTime() {
		return burstTime;
	}

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public void setPriority(Priority priority) {
		this.priority=priority;
	}
	public Priority getPriority() {
		return this.priority;
	}

	@Override
	public void reducePriority() {
		
		switch(this.getPriority()) {
		
		case HIGHESTPRIORITY:{
			this.setPriority(Priority.MEDIUMPRIORITY);
			break;
		}
		case MEDIUMPRIORITY:{
			this.setPriority(Priority.LOWESTPRIORITY);
			break;
		}
		case LOWESTPRIORITY:{
			//Do nothing
			break;
		}
		default:
			break;
		
		}
	}
	
}