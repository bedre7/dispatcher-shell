package dispatchershell;

import java.util.LinkedList;
import java.util.Queue;

public class RealTimeQueue implements IRealTimeQueue {
	Queue<IProcess> queue;
	public RealTimeQueue() {
		queue=new LinkedList<IProcess>();
	}
	@Override
	public void add(IProcess process) {
		queue.add(process);
	}

	@Override
	public IProcess remove() {
		return queue.remove();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	@Override
	public void run() 
	{
		
	}

}
