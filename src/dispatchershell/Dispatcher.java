package dispatchershell;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements IDispatcher{
	private static IDispatcher instance;
	private static IRealTimeQueue realTimeQueue;
	private static IUserJob userJob;
	private String filePath;
	private Color[] colors;
	private Stack<IProcess> processStack;
	private static List<IProcess> pendingProcesses;
	private int quantum;
	private static int maxExecutionTime;

	public Dispatcher(String filePath, int quantum, int _maxExecutionTime)
	{
		maxExecutionTime = _maxExecutionTime;
		this.quantum = quantum;
		this.filePath = filePath;
		realTimeQueue = new RealTimeQueue(maxExecutionTime);
		userJob = new UserJob(this.quantum, maxExecutionTime);
		this.processStack = new Stack<IProcess>();
		pendingProcesses = new ArrayList<IProcess>();
		this.colors = new Color[]{
			Color.BLUE, Color.CYAN, 
			Color.GREEN, Color.PURPLE, 
			Color.RED, Color.WHITE, 
			Color.YELLOW
		};
	}
	
	public static IDispatcher getInstance(String filePath, int quantum, int maxExecutionTime)
	{
		if (instance == null) {
			return new Dispatcher(filePath, quantum, maxExecutionTime);
		}
		return instance;
	}

	@Override
	public IDispatcher readFile() 
	{
		PriorityQueue<IProcess> priorityQueue = new PriorityQueue<IProcess>(
					new ProcessComparator()
				);
		
		try(BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
			String line;
			
			int Id = 0;
			while ((line = br.readLine()) != null)
			{
				String[] param = line.split(",");
				
				int arrivalTime = Integer.parseInt(param[0].trim());
				Priority priority = this.convertToPriority(Integer.parseInt(param[1].trim()));
				int burstTime = Integer.parseInt(param[2].trim());
				
				IProcess newProcess = new Process(
						Id++, arrivalTime, priority, burstTime, this.getRandomColor()
						);
				
				priorityQueue.add(newProcess);
			}
		}
		catch(IOException e) {
			System.out.println("\nDosya bulunamadi, lutfen verdiginiz dosyanin yolunu kontrol ediniz\n"
					+ "Not: sadece dosyanin ismini girecekseniz dosyanizi projenin ana klasorune koyunuz(JAR'in yaninda)\n");
			e.printStackTrace();
		}

		//prosesler kuyruga eklendikten sonra stack veri yapisina kopyalanir
		//bunun sebebi de siradaki calistirilmasi gereken prosesi kolayca belirlemek
		//icin stack en uygun veri yapisidir
		this.stackProcesses(priorityQueue);
		
		Stack<IProcess> tempStack = new Stack<IProcess>();
		
		while(!priorityQueue.isEmpty())
			tempStack.push(priorityQueue.poll());
		
		while(!tempStack.isEmpty())
			this.processStack.push(tempStack.pop());
		
		return this;
	}
	
	@Override
	public void start() throws IOException, InterruptedException 
	{
		IProcess lastProcess = null;
		
		while(!this.processStack.isEmpty())
		{
			IProcess currentProcess = this.getCurrentProcess();
			
			if (currentProcess != null)
			{
				if (lastProcess != null && currentProcess.hasHigherPriority(lastProcess))
				{
					this.interrupt(lastProcess);
					pendingProcesses.add(lastProcess);
					lastProcess = null;
				}
					
				if(currentProcess.isRealTime())
				{
					realTimeQueue.add(currentProcess);
					realTimeQueue.run();
					continue;
				}
				else
				{
					//normal proses ise userjob'a atanir
					userJob.distribute(currentProcess);
				}
			}
				
			if (userJob.hasProcess())
			{
				lastProcess = userJob.run();
			}
			else
				Timer.tick();
		}
		
		while(userJob.hasProcess()) 
		{
			userJob.run();
		}
		
	}
	@Override
	public void interrupt(IProcess process) {
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
		if (Timer.getCurrentTime() >= process.getArrivalTime()) {
			process.setState(State.READY);
			return true;
		}
		
		return false;
	}
	@Override
	public IProcess getCurrentProcess()
	{
		IProcess currentProcess = null;
		Stack<IProcess> stack = new Stack<IProcess>();
		
		while(!this.processStack.isEmpty())
		{
			currentProcess = this.processStack.pop();
			
			if (this.processHasArrived(currentProcess))
			{
				while(!stack.isEmpty())
					this.processStack.add(stack.pop());
				
				break;
			}
			else
				stack.push(currentProcess);
		}
		
		return currentProcess;
	}
	
	public static void checkPendingProcesses()
	{
		for (IProcess process : new ArrayList<IProcess>(pendingProcesses))
		{
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

	public static boolean shouldBeTerminated(IProcess process)
	{
		return !process.isOver() && process.getWaitingTime() >= maxExecutionTime;
	}
}
