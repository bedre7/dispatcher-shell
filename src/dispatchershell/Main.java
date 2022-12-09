package dispatchershell;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
//		IDispatcher dispatcher = Dispatcher.getInstance("input.txt");			
//		dispatcher.readFile().start();
		String[] strs = {"foo", "voo", "bar"};
		List<String> strList = new LinkedList<String>(Arrays.asList(strs));
		for (String str : new LinkedList<String>(strList))
		{
			if (str.equals("foo"))
				strList.remove(str);
		}
		strList.forEach(s -> System.out.println(s));
	}
}
