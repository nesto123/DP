package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Testing {

	//		Testing...
	public static void main(String[] args) throws IOException {
		FileSystem fileSystem = new FileSystem("proba");
		
		
		fileSystem.createFile("nesto.txt","");
		
		System.out.print(fileSystem.getFile("nesto.txt"));
		System.out.println("-----");
		fileSystem.writeToFile("nesto.txt", "bok\nlok" );
		System.out.println("-----");

		System.out.print(fileSystem.getFile("nesto.txt"));

		BufferedReader din = new BufferedReader(
				new InputStreamReader(System.in));
		String chatMsg = din.readLine();
	}
}
