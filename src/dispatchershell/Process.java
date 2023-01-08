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
	private ProcessBuilder processBuilder;
	
	public Process(int Id, int arrivalTime, Priority priority, int burstTime, Color color) {
		this.Id = Id;
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		this.burstTime = burstTime;
		this.color = color;
		this.elapsedTime = 0;
		this.state = State.NEW;
		this.processBuilder = new ProcessBuilder(
				"cmd", "/c", "echo", "\r"
				).inheritIO();
	}
	//prosesi verilen quantuma gore calistiran ve zamanlayiciyi saydiran fonksiyon
	@Override
	public State execute(int quantum) throws IOException, InterruptedException {
		
//		this.processBuilder.start().waitFor();
		
		while (quantum > 0 && !this.isOver()) 
		{
			Console.printProcessState(this, "is running");
			//prosesin calistigi her dongu cevrime kadar zamanlayici artirilir
			Timer.tick();
			
			this.setState(State.RUNNING);
			this.elapsedTime++;
			quantum--;
		}
		//proses bekletilir
		this.setState(State.WAITING);
		
		//proses bitmisse mesaj yazdirilir
		if(this.isOver()) {
			Console.printProcessState(this, "has ended");
			this.setState(State.TERMINATED);
		}
		//son calisma zamani guncellenir
		this.lastExecutionTime = Timer.getCurrentTime();
		
		return this.getState();
	}
	//iki proseslerin oncelik seviyelerini karsilasitiran fonksiyon
	@Override
	public boolean hasHigherPriority(IProcess other) {
		if (other == null) return true;
		return this.getPriority().ordinal() < other.getPriority().ordinal();
	}
	//bekleme zamani bulan fonksiyon
	@Override
	public int getWaitingTime() {
		return Timer.getCurrentTime() - this.lastExecutionTime;
	}
	//proses bitip bitmedigini kontrol eden fonksiyon
	@Override
	public boolean isOver() {
		return this.getBurstTime() == this.getElapsedTime();
	}
	//proses gercek zamanli olup olmadigini kontrol eden fonksiyon
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
	//prosesin onceligi dusuren fonksiyon
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