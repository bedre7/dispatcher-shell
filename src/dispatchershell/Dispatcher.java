package dispatchershell;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements IDispatcher{
	private static IDispatcher instance;
	private IRealTimeQueue realTimeQueue;
	private IUserJob userJob;
	private String filePath;
	private Color[] colors;
	private List<IProcess> waitingProcesses;
	private final int QUANTUM = 1;
	private int currentTime;

	public Dispatcher(String filePath)
	{
		this.realTimeQueue = new RealTimeQueue();
		this.userJob = new UserJob();
		this.waitingProcesses = new LinkedList<IProcess>();
		this.filePath = filePath;
		this.currentTime = 0;
		this.colors = new Color[]{
			Color.BLUE, Color.CYAN, 
			Color.GREEN, Color.PURPLE, 
			Color.RED, Color.WHITE, 
			Color.YELLOW
		};
	}
	
	public static IDispatcher getInstance(String filePath)
	{
		if (instance == null) {
			return new Dispatcher(filePath);
		}
		return instance;
	}

	@Override
	public IDispatcher readFile() 
	{
		final String separator = ",";
		
		try(BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
			
			String line;
			
			while ((line = br.readLine()) != null)
			{
				String[] param = line.split(separator);
				
				int arrivalTime = Integer.parseInt(param[0].trim());
				Priority priority = this.convertToPriority(Integer.parseInt(param[1].trim()));
				int burstTime = Integer.parseInt(param[2].trim());
				
				IProcess newProcess = new Process(
						arrivalTime, priority, burstTime, this.getRandomColor()
						);
				
				this.waitingProcesses.add(newProcess);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	@Override
	public void start() 
	{
		while(true)
		{
			this.currentTime = Timer.getCurrentTime();
			for (int i = 0; i < this.waitingProcesses.size(); i++)
			{
				IProcess process = this.waitingProcesses.get(i);
				if (this.processHasArrived(process))
				{
					if(process.isRealTime())
					{
						this.realTimeQueue.add(process);
						this.realTimeQueue.run();
					}
					else 
					{
						this.userJob.distribute(process);
					}
				}
				
				
				this.userJob.run();
				Timer.tick();
			}
			
			this.currentTime = Timer.getCurrentTime();
		}
		
		while(useJob.is)
			
	}
	
	
	@Override
	public Color getRandomColor()
	{
		final int MIN = 0;
		final int MAX = 6;
		int randomIndex = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		return this.colors[randomIndex];
	}
	
	private Priority convertToPriority(int priorityValue)
	{
		return switch(priorityValue) {
			case 0 -> Priority.REALTIME;
			case 1 -> Priority.HIGHESTPRIORITY;
			case 2 -> Priority.MEDIUMPRIORITY;
			case 3 -> Priority.LOWESTPRIORITY;
			default -> null;	
		};
	}

	@Override
	public boolean processHasArrived(IProcess process) {
		
		return this.currentTime >= process.getArrivalTime();
	}
}
