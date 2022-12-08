package dispatchershell;

public class Main {
	public static void main(String[] args) {
		String YELLOW = "\u001B[33m";
		IDispatcher dispatcher = Dispatcher.getInstance("input.txt");
		dispatcher.readFile();
		System.out.println(Color.BLUE.getColor() + "Hello there");
		System.out.println(Color.CYAN.getColor() + "Hello there");
		System.out.println(Color.GREEN.getColor() + "Hello there");
		System.out.println(Color.PURPLE.getColor() + "Hello there");
		System.out.println(Color.RED.getColor() + "Hello there");
		System.out.println(Color.YELLOW.getColor() + "Hello there");
		System.out.println(YELLOW + "Hello there");
	}
}
