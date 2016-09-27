import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * The Mapper class is responsible for storing the data from the files into
 * LinekdHashMaps.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
public class Mapper {
	public LinkedHashMap<String, ArrayList<String>> mapped;
	public LinkedHashMap<String, ArrayList<String>> ori_mapped;
	public LinkedHashMap<String, LinkedHashMap<String, String>> date_mapped;

	/**
	 * The constructor of Mapper class which initializes the maps.
	 */
	public Mapper() {
		mapped = new LinkedHashMap<String, ArrayList<String>>();
		ori_mapped = new LinkedHashMap<String, ArrayList<String>>();
		date_mapped = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	}

	/**
	 * This function accepts line as input and sends it to required functions.
	 * 
	 * @param s:
	 *            line from file
	 */
	public void process_line(String s) {
		String[] line = s.split(",");
		store_sum(line);
		store_date(line);
		store(line);
	}

	/**
	 * This function sums up the amounts for the transactions with same name
	 * 
	 * @param line:
	 *            line from file
	 */
	public void store_sum(String[] line) {
		ArrayList<String> l = new ArrayList<>();
		if (mapped.containsKey(line[1])) {
			l = mapped.get(line[1]);
			double sum = Double.parseDouble(line[2])
					+ Double.parseDouble(mapped.get(line[1]).get(0));
			l.remove(0);
			l.add(0, Double.toString(Math.round(sum)));
			l.add(line[0]);
			mapped.put(line[1], l);
		} else {
			l.add(line[2]);
			l.add(line[0]);
			mapped.put(line[1], l);
		}
	}

	/**
	 * This function simply puts the line into the hashmap
	 * 
	 * @param line
	 *            line from data
	 */
	public void store(String[] line) {
		ArrayList<String> l = new ArrayList<>();
		l.add(line[0]);
		l.add(line[2]);
		ori_mapped.put(line[1], l);
	}

	/**
	 * This function is used to find transactions monthly, and store them with
	 * respect to month.
	 * 
	 * @param line
	 *            line from data
	 */
	public void store_date(String[] line) {
		LinkedHashMap<String, String> l = new LinkedHashMap<>();
		String cmp = line[0].substring(0, 7);
		Double sum = 0.0;
		if (date_mapped.size() > 0) {
			if (date_mapped.keySet().contains(cmp)) {
				l = date_mapped.get(cmp);
				if (l.values().contains(line[1])) {
					sum = sum + Double.parseDouble(l.get(line[1]));
					l.put(line[1], sum + "");
					date_mapped.put(cmp, l);
				} else {
					l.put(line[1], line[2]);
					date_mapped.put(cmp, l);
				}
			} else {
				l.put(line[1], line[2]);
				date_mapped.put(cmp, l);
			}
		} else {
			l.put(line[1], line[2]);
			date_mapped.put(cmp, l);
		}
	}

	/**
	 * Function used to return the LinkedHashMap mapped
	 * 
	 * @return mapped
	 */
	public LinkedHashMap<String, ArrayList<String>> get_map() {
		return mapped;
	}

	/**
	 * Function used to return the LinkedHashMap ori_mapped
	 * 
	 * @return ori_mapped
	 */
	public LinkedHashMap<String, ArrayList<String>> get_ori_map() {
		return ori_mapped;
	}

	/**
	 * Function used to return the LinkedHashMap date_mapped
	 * 
	 * @return date_mapped
	 */
	public LinkedHashMap<String, LinkedHashMap<String, String>> get_date_map() {
		return date_mapped;
	}

}
