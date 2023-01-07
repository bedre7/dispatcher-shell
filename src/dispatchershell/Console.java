package dispatchershell;

import java.awt.SystemColor;

//shellin mesaj yazdirma sinifi
public class Console {
	
	public static void log(String text)
	{
		System.out.println(text);
	}
	//Prosesin bilgilerini yazmaya yarayan fonksiyon
	public static void printProcessState(IProcess process, String state)
	{
		System.out.print(process.getColor().colorCode);
		
		System.out.println(Timer.getCurrentTime() + ".0000 sec\tprocess "
			+ state + "\t(id : " + String.format("%04d",process.getId()) + " priority : " + 
			process.getPriority().ordinal() + " remaining time: " + 
			(process.getBurstTime() - process.getElapsedTime()) + ")");
		
		System.out.print(Color.RESET.colorCode);
	}
}