import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The FileProcessor class reads data from file and returns one line at a time.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
public class FileProcessor {
	public FileInputStream f_stream;
	public BufferedReader input;

	public FileProcessor(String fileName) {
		try {
			f_stream = new FileInputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			input = new BufferedReader(new InputStreamReader(f_stream));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This file reads and returns one line.
	 * 
	 * @return line from file
	 * @throws IOException
	 */
	String readLineFromFile() throws IOException {
		try {
			return input.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Exception found";
	}
}
