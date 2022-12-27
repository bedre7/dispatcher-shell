package dispatchershell;

public class Main {
	public static void main(String[] args) {
		final int QUANTUM = 1;
		final int MAX_EXECUTION_TIME = 20;
		 
		final String FILEPATH = args.length == 0 ? "input.txt" : args[0];
		
		IDispatcher dispatcher = Dispatcher.getInstance(
								  FILEPATH, QUANTUM, MAX_EXECUTION_TIME
								 );			
		try {
			dispatcher.readFile()
					  .start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
