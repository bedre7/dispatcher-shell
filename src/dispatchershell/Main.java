package dispatchershell;

public class Main {
	public static void main(String[] args) {
		final int QUANTUM = 1;
		final int MAX_EXECUTION_TIME = 20;
		IDispatcher dispatcher = Dispatcher.getInstance("input.txt", 
										QUANTUM, MAX_EXECUTION_TIME
										);			
		dispatcher.readFile()
				  .start();
	}
}
