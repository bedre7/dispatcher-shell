package dispatchershell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//Geri beslemeli kuyruklari yoneten Userjob sinifi
public class UserJob implements IUserJob{
	
	private Queue<IProcess>[] processQueue;
	private final int HIGHESTPRIORITY = 0;
	private final int MEDIUMPRIORITY = 1;
	private final int LOWESTPRIORITY = 2;
	private final int SIZE = 3;
	private int quantum;
	
	
	public UserJob(int quantum) {
		
		this.quantum = quantum;
		processQueue = new LinkedList[SIZE];
				
		//proseslerin geri beslemeli kuyruklari(sirasiyla)
		processQueue[HIGHESTPRIORITY] = new LinkedList<IProcess>();
		processQueue[MEDIUMPRIORITY] = new LinkedList<IProcess>();
		processQueue[LOWESTPRIORITY] = new LinkedList<IProcess>();
	}
	
	@Override
	public IProcess run() throws IOException, InterruptedException {
		IProcess currentProcess = null;
		
		for(int i = 0; i < SIZE; i++)
		{
			//en oncelikli kuyruktan baslanarak verilen kuantuma kadar proses calistirilir
			if(!processQueue[i].isEmpty()) {
				//siradaki proses kuyurktan cekilir
				currentProcess = this.processQueue[i].poll();
				
				//proses calistirilir
				State state = currentProcess.execute(this.quantum);
				
				if(state != State.TERMINATED) {
					//calistiktan sonra bir sonraki kuyuruga atanir(mumkunse) 
					
					//calistiktan sonra bir sonraki kuyruga atanir(eger mumkunse)
					if(i + 1 < SIZE) {
						currentProcess.reducePriority();
						this.processQueue[i + 1].add(currentProcess);
					}
					else
					{
						processQueue[i].add(currentProcess);  //Last queue
					}
				}
				return currentProcess;
			}	
		}
		
		return currentProcess;
	}
	
	//userjob'in herhangi kuyrugunda proses olup olmadigini kontrol eden fonksiyon
	@Override	
	public boolean hasProcess() {	
		
		for(int priority = 0; priority < SIZE; priority++)
			if(!processQueue[priority].isEmpty()) return true;
	
		return false;
	}
	
	//verilen prosesi ilgili kuyruktan cikartan fonksiyon
	@Override
	public void remove(IProcess process) {
		int priorityValue = process.getPriority().ordinal();
		int level = priorityValue - 1;
		this.processQueue[level].remove(process);
	}
	
	//gelen prosesin onceligine gore uygun kuyruga atanan fonksiyon
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
