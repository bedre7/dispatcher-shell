package dispatchershell;

import java.util.Comparator;

//proseslerini onceliklerine gore siralamaya yarayan karsilasitirici fonksiyon
//Note: once proseslerin oncelikleri bakilmaktadir
//		oncelikleri ayni ise varis zamanlarina gore siralanmaktadir
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
			else {
				return NONE;
			}
		}
		else if (priorityLeft < priorityRight)
			return LEFT;
		return RIGHT;
	}

}
