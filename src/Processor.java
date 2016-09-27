import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * The processor class is the main class of the system. It consists of objects
 * of the required classes and function calls to funcitons of the particular
 * classes.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
public class Processor {

	public static LinkedHashMap<String, ArrayList<String>> A;
	public static LinkedHashMap<String, ArrayList<String>> B;

	public static LinkedHashMap<String, ArrayList<String>> A_sum;
	public static LinkedHashMap<String, ArrayList<String>> B_sum;
	
	public static LinkedHashMap<String, ArrayList<String>> A_sorted;
	public static LinkedHashMap<String, ArrayList<String>> B_sorted;
	
	public static LinkedHashMap<String, ArrayList<String>> A_no_recur;
	public static LinkedHashMap<String, ArrayList<String>> B_no_recur;

	public static LinkedHashMap<String, LinkedHashMap<String, String>> A_date;
	public static LinkedHashMap<String, LinkedHashMap<String, String>> B_date;

	public static LinkedHashMap<String, ArrayList<String>> A_recurring;
	public static LinkedHashMap<String, ArrayList<String>> B_recurring;
	
	public static LinkedHashMap<String, String> A_month_max;
	public static LinkedHashMap<String, String> B_month_max;
	
	public static Double SAVINGS = 0.0;

	public Processor() {

	}

	public String month_3_max(ArrayList<String> A_max,
			ArrayList<String> B_max) {
		int size = 0;
		if (A_max.size() <= B_max.size()) {
			size = A_max.size();
		} else
			size = B_max.size();
		String three_monthly = "";
		for (int i = 0; i < size; i++) {
			if (Double.parseDouble(A_max.get(i).split("=")[1]) >= Double
					.parseDouble(B_max.get(i).split("=")[1])) {
				three_monthly += A_max.get(i) + "\n";
			} else
				three_monthly += B_max.get(i) + "\n";
		}
		return three_monthly;
	}

	/**
	 * The analyze function is responsible for appropriate object creation and
	 * function calling to analyze the data stored in HashMaps.
	 */
	public void analyze() {
		// Creating analyzer object of Analyzer class.
		Analyzer analyzer = new Analyzer();

		// removing recurring transactions.
		A_no_recur = analyzer.find_recurring(A_sum);
		A_recurring = analyzer.get_recurring();
		B_no_recur = analyzer.find_recurring(B_sum);
		B_recurring = analyzer.get_recurring();

		// getting map with date as keys and {transaction and amount} as values
		A_date = analyzer.get_month_map(A_date, A_recurring);
		B_date = analyzer.get_month_map(B_date, B_recurring);

		A_date = analyzer.sort_month(A_date);
		B_date = analyzer.sort_month(B_date);

		// getting the transactions with the max amount excluding recurring
		// ones.
		ArrayList<String> A_max = analyzer.get_month_max(A_date);
		ArrayList<String> B_max = analyzer.get_month_max(B_date);

		// getting the transactions for top 3 months.
		String result = month_3_max(A_max, B_max);
		
		//Calculating amount which can be saved by using public transport.
		Double sum1 = 0.0;
		sum1 += analyzer.save_from_publictransport(A_no_recur);
		Double sum = 0.0;
		sum = sum + analyzer.save_from_publictransport(B_no_recur);
		if (sum >= sum1) {
			sum1 = sum;
		}
		if (sum1 > 10.0) {
			sum1 = sum1 - 3.33;
		}
		System.out.println("FOR A PERIOD OF 3 months:-\n");
		System.out.println("SAVINGS by using public transport: "
				+ Math.round(sum1 * 3) + "$");

		SAVINGS += sum1;
		
		//Finding the late fees which customers made in given data.
		sum1 = 0.0;
		sum1 += analyzer.find_late_fees(A_no_recur);
		sum = 0.0;
		sum += analyzer.find_late_fees(B_no_recur);
		if (sum >= sum1) {
			sum1 = sum;
		}
		System.out.println("SAVINGS from preventing late fees: "
				+ Math.round(sum1 * 3) + "$");
		SAVINGS += sum1;

		
		//Scope for saving in transactions with most amount in 3 months period.
		SAVINGS = SAVINGS * 3;
		System.out.println(
				"\nSome transactions in a three month period from which you can save money:-");
		System.out.println(result);
		String[] arr = result.split("\n");
		sum = 0.0;
		for (String s : arr) {
			String[] sp = s.split("=");
			sum = sum + Double.parseDouble(sp[1]);
		}
		sum = sum / 5.0;
		System.out.println(
				"If you manage to save even 20% from these transactions");
		SAVINGS += sum;
		System.out.println("\nYour total possible savings are "
				+ Math.round(SAVINGS) + "$");

	}

	/**
	 * This the main function of the sytem.
	 * 
	 * @param args
	 *            2 arguments specifying the file names
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Processor processor = new Processor();
		// FileProcessor fp1 = new FileProcessor("transactions-person-A.csv");
		// FileProcessor fp2 = new FileProcessor("transactions-person-B.csv");

		if (args.length != 2) {
			System.err.println("PLEASE provide the two file names as follows");
			Thread.sleep(100);
			System.out.println(
					"if on terminal:\njava Processor transactions-person-A.csv transactions-person-B.csv");
			System.out.println(
					"else please add{ transactions-person-A.csv transactions-person-B.csv }to run configurations");
			System.exit(0);
		}

		// Creating object of file processor which will read file line by line.
		FileProcessor fp1 = new FileProcessor(args[0]);
		FileProcessor fp2 = new FileProcessor(args[1]);

		// Creating Mapper which will map the line to the LinkedHashMaps.
		Mapper mapper1 = new Mapper();
		Mapper mapper2 = new Mapper();
		String line = "";
		while ((line = fp1.readLineFromFile()) != null) {
			mapper1.process_line(line);
		}
		A_sum = mapper1.get_map();
		A = mapper1.get_ori_map();
		line = "";
		while ((line = fp2.readLineFromFile()) != null) {
			mapper2.process_line(line);
		}

		B_sum = mapper2.get_map();
		B = mapper2.get_ori_map();

		// Sorter objects which sorts the maps according to the value.
		Sorter sort1 = new Sorter(A_sum);
		sort1.sort();
		A_sorted = sort1.get_sortedMap();

		Sorter sort2 = new Sorter(B_sum);
		sort2.sort();
		B_sorted = sort2.get_sortedMap();
		A_date = mapper1.get_date_map();
		B_date = mapper2.get_date_map();

		// call to analyze function which will analyze the data stored in
		// hashmaps
		processor.analyze();

	}
}
