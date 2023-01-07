package dispatchershell;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//Gercek zamanli proses kuyurugu yoneten sinif
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
	//prosesleri FCFS algoritmasina gore calisitiran fonksiyon
	@Override
	public void run() throws IOException, InterruptedException 
	{
		while (!this.queue.isEmpty())
		{
			IProcess currentProcess = this.queue.peek();
			currentProcess.execute(currentProcess.getBurstTime(), this.maxExecutionTime);
			this.queue.remove(currentProcess);
		}
	}

}
