package dispatchershell;

public class Main {
	public static void main(String[] args) {
		final int QUANTUM = 1;
		final int MAX_EXECUTION_TIME = 20;
//		final String FILEPATH = args[1];
		final String FILEPATH = "input.txt";
		
		IDispatcher dispatcher = Dispatcher.getInstance(
								  FILEPATH, QUANTUM, MAX_EXECUTION_TIME
								 );			
		dispatcher.readFile()
				  .start();
	}
}
