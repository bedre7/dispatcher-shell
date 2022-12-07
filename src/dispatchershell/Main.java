package dispatchershell;

public class Main {
	public static final String ANSIYELLOW = "\u001B[33m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static void main(String[] args) {
		Console console = new Console();
		console.log("00:00:00 Process1 of priority 2 is running...");
		console.log("00:00:00 Process1 of priority 2 is running...");
		System.out.print(ANSIYELLOW + "Seidy is kral");
		
	}

}
