package dispatchershell;

import java.io.IOException;

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
	
	//executes the process for the amount of quantum provided
	@Override
	public State execute(int quantum) throws IOException, InterruptedException {
		while (quantum > 0 && !this.isOver()) 
		{
			Console.printProcessState(this, "is running");
			//The timer is ticked for each second the process has executed
			Timer.tick();
			
			this.setState(State.RUNNING);
			this.elapsedTime++;
			quantum--;
		}

		//The process's state is updated to "WAITING"
		this.setState(State.WAITING);
		
		//If the process is over
		if(this.isOver()) {
			Console.printProcessState(this, "has ended");
			this.setState(State.TERMINATED);
		}
		
		//Last execution time is recorded for tracking the waiting time of the process
		this.lastExecutionTime = Timer.getCurrentTime();
		
		return this.getState();
	}
	
	//Compares the priority of the process passed in with the process this function is called upon
	@Override
	public boolean hasHigherPriority(IProcess other) {
		if (other == null) return true;
		return this.getPriority().ordinal() < other.getPriority().ordinal();
	}

	//computes the waiting time of the process
	@Override
	public int getWaitingTime() {
		return Timer.getCurrentTime() - this.lastExecutionTime;
	}
	
	//checks whether the process is over
	@Override
	public boolean isOver() {
		return this.getBurstTime() == this.getElapsedTime();
	}
	
	//Tells if the process is realtime
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

	//reduces the priority of the process by 1
	@Override
	public void reducePriority() {
		
		switch(this.getPriority()) 
		{
			case HIGHESTPRIORITY:	this.setPriority(Priority.MEDIUMPRIORITY); 	break;
			case MEDIUMPRIORITY:	this.setPriority(Priority.LOWESTPRIORITY); 	break;
			case LOWESTPRIORITY:	break;
			default:
				break;
		}
	}
}