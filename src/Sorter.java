import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * This function is used to sort the HashMap according to their values.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
public class Sorter {
	public LinkedHashMap<String, ArrayList<String>> sorted;
	public ArrayList<ArrayList<String>> list = new ArrayList<>();

	/**
	 * Constructor used to initialize sorted.
	 * 
	 * @param map
	 */
	public Sorter(LinkedHashMap<String, ArrayList<String>> map) {
		sorted = map;
	}

	/**
	 * This function is used to sort the HashMap according to values. It used
	 * CustomComparator class to hep sort the data.
	 * 
	 */
	public void sort() {
		for (String k : sorted.keySet()) {
			sorted.get(k).add(0, k);
			list.add(sorted.get(k));
		}
		Collections.sort(list, new CustomComparator());
		sorted.clear();
		for (int i = 0; i < list.size(); i++) {
			sorted.put(list.get(i).remove(0), list.get(i));
		}
	}

	/**
	 * This function returns the sorted map.
	 * 
	 * @return sorted
	 */
	public LinkedHashMap<String, ArrayList<String>> get_sortedMap() {
		return sorted;
	}
}

/**
 * This class implements Comparator and overrides the compare function.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
class CustomComparator implements Comparator<ArrayList<String>> {

	@Override
	public int compare(ArrayList<String> o1, ArrayList<String> o2) {
		if (Double.parseDouble(o1.get(1)) > Double.parseDouble(o2.get(1))) {
			return -1;
		} else if (Double.parseDouble(o1.get(1)) < Double
				.parseDouble(o2.get(1))) {
			return 1;
		}
		return 0;
	}
}