package dispatchershell;

import java.util.Comparator;

/*
  A comparator class used to compare and sort processes based on their priority
  Note: First the priority is being used as a paramter to compare two processes
		if their priority is equal, their arrival time is being used as a factor
*/
public class ProcessComparator implements Comparator<IProcess>{

	@Override
	public int compare(IProcess a, IProcess b) {
		int priorityLeft = a.getPriority().ordinal();
		int priorityRight = b.getPriority().ordinal();
		int arrivalTimeLeft = a.getArrivalTime();
		int arrivalTimeRight = b.getArrivalTime();
		
		final int LEFT = -1, RIGHT = 1, NONE = 0;

		if (priorityLeft == priorityRight)
		{
			if (arrivalTimeLeft < arrivalTimeRight)
				return LEFT;
			else if (arrivalTimeLeft > arrivalTimeRight)
				return RIGHT;
			else
				return NONE;
		}
		else if (priorityLeft < priorityRight)
			return LEFT;
		
		return RIGHT;
	}

}
