package dispatchershell;

public class Process implements IProcess {
	private int Id;
	private int arrivalTime;
	private int burstTime;
	private int elapsedTime;
	private Color color; 
	private Priority priority;
	private State state;
	
	public Process(int arrivalTime, Priority priority, int burstTime, Color color) {
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		this.burstTime = burstTime;
		this.color = color;
		this.elapsedTime = 0;
		this.setState(State.NEW);
	}
	
	@Override
	public State execute(int quantum, int maxExecutionTime) {
		while (quantum >= 0 && !this.isOver()) {
			if(this.hasExceededTimeLimit(maxExecutionTime))
			{
				this.setState(State.TERMINATED);
				return this.getState();
			}
						
			Timer.tick();
			this.setState(State.RUNNING);
			this.elapsedTime++;
			quantum--;
		}
		
		this.setState(State.WAITING);
		return this.getState();
	}
	@Override
	public boolean hasExceededTimeLimit(int limit) {
		return this.getElapsedTime() >= limit;
	}
	@Override
	public boolean isOver() {
		return this.getBurstTime() == this.getElapsedTime();
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
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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