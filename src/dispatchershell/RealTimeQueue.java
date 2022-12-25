package dispatchershell;

import java.util.LinkedList;
import java.util.Queue;

public class RealTimeQueue implements IRealTimeQueue {
	private Queue<IProcess> queue;
	private int maxExecutionTime;
	
	public RealTimeQueue(int maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
		this.queue = new LinkedList<IProcess>();
	}
	
	@Override
	public void add(IProcess process) {
		this.queue.add(process);
	}

	@Override
	public void remove(IProcess process) {
		this.queue.remove(process);
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
	@Override
	public void run() 
	{
		while (!this.queue.isEmpty())
		{
			IProcess currentProcess = this.queue.peek();
			currentProcess.execute(currentProcess.getBurstTime(), this.maxExecutionTime);
			this.queue.remove(currentProcess);
		}
	}

}
