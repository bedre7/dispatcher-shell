package dispatchershell;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class UserJob implements IUserJob{
	
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
			
			break;
		}
		case MEDIUMPRIORITY:{
			
			break;
		}
		case LOWESTPRIORITY:{
			
			break;
		}
		default:
			break;
		
		}
	}
}
