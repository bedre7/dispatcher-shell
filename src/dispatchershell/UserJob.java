package dispatchershell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//A Multilevel-Feedback queue(3 levels) that manages user processes
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
				
		//The multilevel feedback queues
		processQueue[HIGHESTPRIORITY] = new LinkedList<IProcess>();
		processQueue[MEDIUMPRIORITY] = new LinkedList<IProcess>();
		processQueue[LOWESTPRIORITY] = new LinkedList<IProcess>();
	}
	
	@Override
	public IProcess run() throws IOException, InterruptedException {
		IProcess currentProcess = null;
		
		for(int i = 0; i < SIZE; i++)
		{
			//The queue with the highest priority is let run
			if(!processQueue[i].isEmpty()) {
				//The current process is pulled out from the queue
				currentProcess = this.processQueue[i].poll();
				
				//process is executed
				State state = currentProcess.execute(this.quantum);
				
				if(state != State.TERMINATED) {
					//After execution, the process is assigned to the next queue(if possible)
					
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
	
	//Checks if the userjob has a process in any of its queues.
	@Override	
	public boolean hasProcess() {	
		
		for(int priority = 0; priority < SIZE; priority++)
			if(!processQueue[priority].isEmpty()) return true;
	
		return false;
	}
	
	//Removes a process from the respective queue it belongs to
	@Override
	public void remove(IProcess process) {
		int priorityValue = process.getPriority().ordinal();
		int level = priorityValue - 1;
		this.processQueue[level].remove(process);
	}
	
	//Assigns the process passed in to the queue it belongs to
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
