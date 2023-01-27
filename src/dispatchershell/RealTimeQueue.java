package dispatchershell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//A queue class used to manage real time processes
public class RealTimeQueue implements IRealTimeQueue {
	private Queue<IProcess> queue;
	
	public RealTimeQueue() {
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

	//A function that executes processes in First-Come, First-Served algorithm
	@Override
	public void run() throws IOException, InterruptedException 
	{
		while (!this.queue.isEmpty())
		{
			IProcess currentProcess = this.queue.peek();
			currentProcess.execute(currentProcess.getBurstTime());
			this.queue.remove(currentProcess);
		}
	}

}
