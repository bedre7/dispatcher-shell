package dispatchershell;

public class Process implements IProcess {
	private int Id;
	private int arrivalTime;
	private int burstTime;
	private int elapsedTime;
	private int lastExecutionTime;
	private Color color; 
	private Priority priority;
	private State state;
	
	public Process(int Id, int arrivalTime, Priority priority, int burstTime, Color color) {
		this.Id = Id;
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		this.burstTime = burstTime;
		this.color = color;
		this.elapsedTime = 0;
		this.state = State.NEW;
	}
	
	@Override
	public State execute(int quantum, int maxExecutionTime) {
		while (quantum > 0 && !this.isOver()) 
		{
			if(this.hasExceededTimeLimit(maxExecutionTime))
			{
				Console.printProcessState(this, "exceeded time");
				this.setState(State.TERMINATED);
				this.lastExecutionTime = Timer.getCurrentTime();
				return this.getState();
			}

			Console.printProcessState(this, "is running");
			Timer.tick();
			this.setState(State.RUNNING);
			this.elapsedTime++;
			quantum--;
		}
		
		this.setState(State.WAITING);
		
		if(this.isOver()) {
			Console.printProcessState(this, "has ended");
			this.setState(State.TERMINATED);
		}
		
		this.lastExecutionTime = Timer.getCurrentTime();
		
		return this.getState();
	}
	
	@Override
	public boolean hasHigherPriority(IProcess other) {
		if (other == null) return true;
		return this.getPriority().ordinal() < other.getPriority().ordinal();
	}
	
	@Override
	public boolean hasExceededTimeLimit(int limit) {
		return this.getElapsedTime() >= limit;
	}
	
	@Override
	public int getWaitingTime() {
		return Timer.getCurrentTime() - this.lastExecutionTime;
	}
	
	@Override
	public boolean isOver() {
		return this.getBurstTime() == this.getElapsedTime();
	}
	
	@Override
	public boolean isRealTime() {
		return this.priority == Priority.REALTIME;
	}
	@Override
	public int getId() {
		return Id;
	}
	@Override
	public State getState() {
		return state;
	}
	@Override
	public void setState(State state) {
		this.state = state;
	}
	@Override
	public Color getColor() {
		return color;
	}
	@Override
	public int getArrivalTime() {
		return arrivalTime;
	}
	@Override
	public int getBurstTime() {
		return burstTime;
	}
	@Override
	public int getElapsedTime() {
		return elapsedTime;
	}
	@Override
	public void setPriority(Priority priority) {
		this.priority=priority;
	}
	@Override
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