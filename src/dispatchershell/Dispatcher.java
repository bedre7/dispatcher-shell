package dispatchershell;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements IDispatcher{
	private static IDispatcher instance;
	private static IRealTimeQueue realTimeQueue;
	private static IUserJob userJob;
	private String filePath;
	private Color[] colors;
	private List<IProcess> allProcesses;
	private static List<IProcess> pendingProcesses;
	private int quantum;
	private static int maxWaitingTime;

	public Dispatcher(String filePath, int quantum, int _maxWaitingTime)
	{
		this.quantum = quantum;
		this.filePath = filePath;
		maxWaitingTime = _maxWaitingTime;
		realTimeQueue = new RealTimeQueue();
		userJob = new UserJob(this.quantum);
		pendingProcesses = new ArrayList<IProcess>();
		this.allProcesses = new ArrayList<>();
		this.colors = new Color[]
		{
			Color.BLUE, Color.CYAN, 
			Color.GREEN, Color.PURPLE, 
			Color.RED, Color.WHITE, 
			Color.YELLOW
		};
	}
	
	//Singleton pattern is being used to create an instance from this class
	public static IDispatcher getInstance(String filePath, int quantum, int maxWaitingTime)
	{
		if (instance == null) {
			return new Dispatcher(filePath, quantum, maxWaitingTime);
		}
		return instance;
	}

	@Override
	public IDispatcher readFile() 
	{
		//Processes are being sorted first by their priority and then by their arrival time
		
		PriorityQueue<IProcess> priorityQueue = new PriorityQueue<IProcess>(
					new ProcessComparator()
				);
		
		try(BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
			String line;
			
			int ID = 0;
			while ((line = br.readLine()) != null)
			{
				String[] param = line.split(",");
				
				int arrivalTime = Integer.parseInt(param[0].trim());
				Priority priority = this.convertToPriority(Integer.parseInt(param[1].trim()));
				int burstTime = Integer.parseInt(param[2].trim());
				
				//Each line in the file is parsed and new process object is created
				IProcess newProcess = new Process(
						ID++, arrivalTime, priority, burstTime, this.getRandomColor()
						);
				
				priorityQueue.add(newProcess);
			}
		}
		catch(IOException e) {
			System.out.println("\nFile not found, Please make sure the file path you provided is correct\n"
			e.printStackTrace();
		}

		/*
			After sorting the processes using comparator defined for the priority queue
		 	they are copied to Arraylist to make it easier to traverse the list
		*/
		
		while (!priorityQueue.isEmpty())
		{
			this.allProcesses.add(priorityQueue.poll());
		}
		
		return this;
	}
	
	//Assigns processes to the queue they belong and runs the appropriate queue if needed
	@Override
	public void start() throws IOException, InterruptedException 
	{
		//The last process that was being executed
		IProcess lastProcess = null;
		
		while(!this.allProcesses.isEmpty())
		{
			//The process that needs to be executed at that moment is searched
			IProcess currentProcess = this.getCurrentProcess();
			
			if (currentProcess != null)
			{
				//If the current process is of higher priority, the last process is interrupted
				if (lastProcess != null && currentProcess.hasHigherPriority(lastProcess))
				{
					this.interrupt(lastProcess);
					lastProcess = null;
				}
			
				//If the current process is real time, it is assigned to its respective queue and run
				if(currentProcess.isRealTime())
				{
					realTimeQueue.add(currentProcess);
					realTimeQueue.run();
					continue;
				}
				else
				{
					//If it is user process, it is assigned to userjob queue
					userJob.distribute(currentProcess);
				}
			}
				
			if (userJob.hasProcess())
			{
				lastProcess = userJob.run();
			}
			else
				Timer.tick();				//if there is no queue to be run, the timer is ticked
		}
		
		//If the User job queue still has process, it is let to execute it's processes
		while(userJob.hasProcess()) 
		{
			userJob.run();
		}
		
	}
	
	//It interrupts the process
	@Override
	public void interrupt(IProcess process) {
		pendingProcesses.add(lastProcess);
		//When a process is interrupted its state is set to "ready"
		process.setState(State.READY);	
		Console.printProcessState(process, "is interrupted");
	}
	
	@Override
	public Color getRandomColor()
	{
		final int MIN = 0;
		final int MAX = 6;
		int randomIndex = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		return this.colors[randomIndex];
	}
	
	//Finds out the priority enum value for a integer value passed in
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

	//Checks whether the process passed in has arrived or not
	@Override
	public boolean processHasArrived(IProcess process) {
		if (Timer.getCurrentTime() >= process.getArrivalTime()) {
			process.setState(State.READY);
			return true;
		}
		
		return false;
	}
	
	//Finds out the process with the highest priority that needs to be executed at the moment
	@Override
	public IProcess getCurrentProcess()
	{
		for (IProcess currentProcess : new ArrayList<IProcess>(this.allProcesses))
		{
			if (this.processHasArrived(currentProcess)) 
			{
				//if the process has arrived it is removed from the waiting list and returned
				this.allProcesses.remove(currentProcess);
				return currentProcess;
			}
		}
		
		return null;
	}
	
	//Checks if there is/are any process(es) which exceeded the maximum waiting time
	public static void checkPendingProcesses()
	{
		for (IProcess process : new ArrayList<IProcess>(pendingProcesses))
		{
			//if 20 seconds have passed the process is terminated
			if (Dispatcher.shouldBeTerminated(process))
			{
				if(process.isRealTime())
					realTimeQueue.remove(process);
				else 
					userJob.remove(process);
				
				pendingProcesses.remove(process);
				Console.printProcessState(process, "exceeded limit");
			}
		}
	}
	
	//Checks if the process passed in has exceeded maximum waiting time
	public static boolean shouldBeTerminated(IProcess process)
	{
		return !process.isOver() && process.getWaitingTime() >= maxWaitingTime;
	}
}
