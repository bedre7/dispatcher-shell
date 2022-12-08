package dispatchershell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Dispatcher implements IDispatcher{
	private static IDispatcher instance;
	private IRealTimeQueue realTimeQueue;
	private IUserJob userJob;
	private String filePath;
	private Color[] colors;

	private Dispatcher(String filePath)
	{
		this.realTimeQueue = new RealTimeQueue();
		this.userJob = new UserJob();
		this.filePath = filePath;
		this.colors = new Color[]{
			Color.BLUE, Color.CYAN, 
			Color.GREEN, Color.PURPLE, 
			Color.RED, Color.WHITE, 
			Color.YELLOW
		};
	}
	
	public static IDispatcher getInstance(String filePath)
	{
		if (instance == null) {
			return new Dispatcher(filePath);
		}
		return instance;
	}

	@Override
	public void readFile() {
		// TODO Auto-generated method stub
		final String separator = ",";
		try(BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
			String line;
			
			while ((line = br.readLine()) != null)
			{
				String[] parameters = line.split(separator);
				IProcess newProcess = new Process();
			}
		}catch(IOException e) {
			System.out.println(e.fillInStackTrace());
		}
	}
	
	@Override
	public Color getRandomColor()
	{
		final int MIN = 0;
		final int MAX = 6;
		int randomIndex = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
		return this.colors[randomIndex];
	}
	
}
