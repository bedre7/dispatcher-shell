package dispatchershell;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessQueue implements IProcessQueue {
	Queue<IProcess> queue;
	public ProcessQueue() {
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
	
}
