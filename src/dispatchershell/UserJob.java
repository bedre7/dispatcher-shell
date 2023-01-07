package dispatchershell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//Geri beslemeli kuyuruklari yonten Userjob sinifi
public class UserJob implements IUserJob{
	
	private final int SIZE=3;
	private Queue<IProcess>[] processQueue;
	private final int HIGHESTPRIORITY=0;
	private final int MEDIUMPRIORITY=1;
	private final int LOWESTPRIORITY=2;
	private int quantum;
	int maxExecutionTime;
	
	
	public UserJob(int quantum, int maxExecutionTime) {
		
		this.quantum=quantum;
		this.maxExecutionTime=maxExecutionTime;
		
		processQueue=new LinkedList[SIZE];
		
		//prosesleri geri beslemeli kuyuruklar(sirasiyla) 
		processQueue[HIGHESTPRIORITY]=new LinkedList<IProcess>();
		processQueue[MEDIUMPRIORITY]=new LinkedList<IProcess>();
		processQueue[LOWESTPRIORITY]=new LinkedList<IProcess>();
	}
	
	@Override
	public IProcess run() throws IOException, InterruptedException {
		IProcess currentProcess = null;
		
		for(int i = 0; i < SIZE; i++)
		{
			//en oncelikli kuyurktan baslanarak verilen kuantuma kadar proses calisitirlir
			if(!processQueue[i].isEmpty()) {
				//siradaki proses kuyurktan cekilir
				currentProcess = this.processQueue[i].poll();
				
				//proses calistirlir
				State state = currentProcess.execute(this.quantum,this.maxExecutionTime);
				
				if(state != State.TERMINATED) {
					//calistiktan sonra bir sonraki kuyuruga atanir(mumkunse) 
					
					if(i + 1 < SIZE) {
						currentProcess.reducePriority();
						this.processQueue[i + 1].add(currentProcess);
					}
					else
					{
						processQueue[i].add(currentProcess);  //Last queue
					}
				}
				//We'll write a message to indicate process state
				//Here: "Process Terminated" 
				return currentProcess;
			}	
		}
		
		return currentProcess;
	}
	
	//userjob'in her hangi kuyrugunda proses olup olmadigini kontrol eden fonksiyon
	@Override	
	public boolean hasProcess() {	
		
		for(int priority = 0; priority < SIZE; priority++)
			if(!processQueue[priority].isEmpty()) return true;
	
		return false;
	}
	//verilen prosesi ilgili kuyuruktan cikartan fonksiyon
	@Override
	public void remove(IProcess process) {
		int priorityValue = process.getPriority().ordinal();
		int level = priorityValue - 1;
		this.processQueue[level].remove(process);
	}
	//gelen prosesin onceligine gore uygun kuyuruga atan fonksiyon
	@Override
	public void distribute(IProcess process) {
		
		switch(process.getPriority()) {
		
		case HIGHESTPRIORITY:{
			processQueue[HIGHESTPRIORITY].add(process);
			break;
		}
		case MEDIUMPRIORITY:{
			processQueue[MEDIUMPRIORITY].add(process);
			break;
		}
		case LOWESTPRIORITY:{
			processQueue[LOWESTPRIORITY].add(process);
			break;
		}
		default:
			break;
		
		}
	}
	
}
