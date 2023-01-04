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
	//Singleton pattern kullanilarak nesne olusturuluyor

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
		//proseslerin onceliklerine ve daha sonrasinda gelis zamanlarina gore siralanmistir

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
				
				//Dosyadaki her satir parse edilerek yeni proses olusturuluyor

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
		
		return this;
	}
	//prosesleri kuyurktan stack'e kopyalayan fonksiyon
	@Override
	public void stackProcesses(PriorityQueue<IProcess> priorityQueue)
	{
		//gecici stack
		//kopyalnirken ilk etapta stack'in sirasi ters olacagi icin
		//mecburen gecici stack uzerinden kopyalama yapilmaktadir
		Stack<IProcess> tempStack = new Stack<IProcess>();
		
		while(!priorityQueue.isEmpty())
			tempStack.push(priorityQueue.poll());
		
		while(!tempStack.isEmpty())
			this.processStack.push(tempStack.pop());
	
	}
	
		//proses kuyuruklarina proses atayan ve calistiran ana fonksiyon

	@Override
	public void start() throws IOException, InterruptedException 
	{
		//son calistirilan proses

		IProcess lastProcess = null;
		
		while(!this.processStack.isEmpty())
		{
	//o sirada calistirilmasi gereken proses varsa aranir

			IProcess currentProcess = this.getCurrentProcess();
			if (currentProcess != null)
			{
			//siradaki proses yuksek oncelikli ise son proses askiya alinir

				if (lastProcess != null && currentProcess.hasHigherPriority(lastProcess))
				{
					this.interrupt(lastProcess);
					pendingProcesses.add(lastProcess);
					lastProcess = null;
				}
				//siradaki proses gercek zamanli ise kendi kuyruga ataniyor ve hemen calistirilir

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
			//o sirada gelen proses yoksa userjob calismasina izin verilir
	
			if (userJob.hasProcess())
			{
				lastProcess = userJob.run();
			}
			else
				Timer.tick();
				//hic bir proses yoksa zamanlayici arttirilir 

		}
		//proseslerin hepsi zamani gelmisse ve userjob'in hala bitmeyen
		//prosesi varsa calistirilir
		while(userJob.hasProcess()) 
		{
			userJob.run();
		}
		
	}

	//verilen prosesi askiya alan fonksiyon

	@Override
	public void interrupt(IProcess process) {
		//When a process is interrupted its state is set to "ready"
		process.setState(State.READY);	
		Console.printProcessState(process, "is interrupted");
	}
	//rastgele renk olusturan fonksiyon

	@Override
	public Color getRandomColor()
	{
		final int MIN = 0;
		final int MAX = 6;
		int randomIndex = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		return this.colors[randomIndex];
	}
	//verilen sayiya gore oncelik bulan fonksiyon

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
	//verilen prosesin varip varmadigini sorgulayan fonksiyon

	@Override
	public boolean processHasArrived(IProcess process) {
		
		if (Timer.getCurrentTime() >= process.getArrivalTime()) {
			process.setState(State.READY);
			return true;
		}
		
		return false;
	}
	
	//siradaki calistirilmasi gereken en oncelikli prosesi arayip donduren fonksiyon
	@Override
	public IProcess getCurrentProcess()
	{
		IProcess currentProcess = null;
		//zamani gelen proses bulana kadar prosesler stack'e yedeklenir

		Stack<IProcess> stack = new Stack<IProcess>();
		
		while(!this.processStack.isEmpty())
		{
			currentProcess = this.processStack.pop();
			
			//zamani gelmisse siradaki proses dondurulr

			if (this.processHasArrived(currentProcess))
			{
			//zamanlari gelmeyen prosesler yedekten asil listeye aktarilir 

				while(!stack.isEmpty())
					this.processStack.add(stack.pop());
				
				break;
			}
			else
				stack.push(currentProcess);
		}
		
		return currentProcess;
	}
	
	//bekleme zamanlari gecen prosesleri olup olmadigini kontrol eder

	public static void checkPendingProcesses()
	{
		for (IProcess process : new ArrayList<IProcess>(pendingProcesses))
		{
		//20 sn bekleme suresi gecmisse proses sonlandirilir

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
	//prosesin zamani asip asmadigini kontrol eden fonksiyon

	public static boolean shouldBeTerminated(IProcess process)
	{
		return !process.isOver() && process.getWaitingTime() >= maxExecutionTime;
	}
}
