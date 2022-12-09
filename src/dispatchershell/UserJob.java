package dispatchershell;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class UserJob implements IUserJob{
	
	private final int MAXEXCUTIONTIME=20;
	private final int SIZE;
	private final int QUANTUM=1;
	private Queue<IProcess>[] processQueue;
	private final int HIGHESTPRIORITY=0;
	private final int MEDIUMPRIORITY=1;
	private final int LOWESTPRIORITY=2;
	
	
	
	public UserJob() {
		SIZE=3;
		processQueue=new LinkedList[SIZE];
				
		processQueue[HIGHESTPRIORITY]=new LinkedList<IProcess>();
		processQueue[MEDIUMPRIORITY]=new LinkedList<IProcess>();
		processQueue[LOWESTPRIORITY]=new LinkedList<IProcess>();
	}

	@Override
	public void run() {
		for(int i=0; i<SIZE-1; i++)
		{
			IProcess removedProcess=null;
			
			if(!processQueue[i].isEmpty()) {
				for(int j=0; j<this.QUANTUM; j++) {
					removedProcess=processQueue[i].remove();
					Timer.tick();
				}
				if(i+1<SIZE-1) {
					removedProcess.reducePriority();
					this.processQueue[i+1].add(removedProcess);
				}else {
					processQueue[i].add(removedProcess);
				}
				return;
			}	
		}
	}
	
	@Override	
	public boolean hasProcess() {	
		for(int priority=0; priority<SIZE-1; priority++) {
			if(!processQueue[priority].isEmpty()) return true;
		}
		return false;
	}
	
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

	@Override
	public boolean hasExceededTimeLimit(IProcess process) {
		return process.getElapsedTime()>=this.MAXEXCUTIONTIME;
	}
}
