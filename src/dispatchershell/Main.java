package dispatchershell;

public class Main {
	public static void main(String[] args) {
		IDispatcher dispatcher = Dispatcher.getInstance("input.txt");			
		dispatcher.readFile().start();
	}
}
