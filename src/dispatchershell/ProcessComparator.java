package dispatchershell;

import java.util.Comparator;

public class ProcessComparator implements Comparator<IProcess>{

	@Override
	public int compare(IProcess a, IProcess b) {
		int priorityLeft = a.getPriority().ordinal();
		int priorityRight = b.getPriority().ordinal();
		final int LEFT = -1, RIGHT = 1, NONE = 0;

		if (a.getArrivalTime() == b.getArrivalTime())
		{
			if (priorityLeft < priorityRight)
				return LEFT;
			else if (priorityLeft > priorityRight)
				return RIGHT;
			return NONE;
		}
		else if (a.getArrivalTime() < b.getArrivalTime())
			return LEFT;
		return RIGHT;
	}

}
