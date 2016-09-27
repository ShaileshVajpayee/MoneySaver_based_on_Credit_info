import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * The analyzer class analyzes the data stored in the particular map.
 * 
 * @author Shailesh Vajpayee Email: srv6224@rit.edu
 *
 */
public class Analyzer {
	public static LinkedHashMap<String, ArrayList<String>> recurring = new LinkedHashMap<>();

	/**
	 * This function removes those recurring transactions which are done every
	 * month and no savings can be done from them as they are necessary for the
	 * customer.
	 * 
	 * @param map:
	 *            The transactions of the customer
	 * @return map: map without recurring transactions.
	 */
	public LinkedHashMap<String, ArrayList<String>> find_recurring(
			LinkedHashMap<String, ArrayList<String>> map) {
		recurring.clear();
		for (String i : map.keySet()) {
			if ((map.get(i).size() >= 25) || (i.contains("ATM"))) {
				recurring.put(i, map.get(i));
			}
		}
		for (String k : recurring.keySet()) {
			if ((!k.toUpperCase().contains("UBER"))
					&& (!k.toUpperCase().contains("LYFT"))
					&& (!k.toUpperCase().contains("TAXI"))) {
				if (map.containsKey(k)) {
					map.remove(k, recurring.get(k));
				}
			}
		}
		return map;
	}

	/**
	 * This function finds those transaction where the customer might have to
	 * pay late fees.
	 * 
	 * @param map
	 *            The transactions of the customer
	 * @return sum The possible late sum for a month for possible transactions.
	 */
	public Double find_late_fees(LinkedHashMap<String, ArrayList<String>> map) {
		Double sum = 0.0;
		Double amount = 0.0;
		for (Entry<String, ArrayList<String>> C : map.entrySet()) {
			if (C.getKey().contains("Late") || C.getKey().contains("Fine")) {
				amount = Double.parseDouble(C.getValue().get(0));
				amount = amount / (C.getValue().size() - 1);
				sum = sum + amount;
			}
		}
		return sum;
	}

	/**
	 * This function finds the amount that can be saved if public transport is
	 * used.
	 * 
	 * @param map
	 * @return sum
	 */
	public Double save_from_publictransport(
			LinkedHashMap<String, ArrayList<String>> map) {
		Double sum = 0.0;
		for (String k : map.keySet()) {
			if (k.toUpperCase().contains("UBER")
					|| k.toUpperCase().contains("LYFT")) {
				if (map.get(k).size() >= 25) {
					sum = sum + Double.parseDouble(map.get(k).get(0)) / 24;
				}
			}
		}
		return sum;
	}

	/**
	 * This function returns the map based on month.
	 * 
	 * @param map
	 * @param recurring
	 * @return map : modified according to month.
	 */
	public LinkedHashMap<String, LinkedHashMap<String, String>> get_month_map(
			LinkedHashMap<String, LinkedHashMap<String, String>> map,
			LinkedHashMap<String, ArrayList<String>> recurring) {
		for (String k : map.keySet()) {
			for (String i : recurring.keySet()) {
				if (map.get(k).keySet().contains(i)) {
					map.get(k).keySet().remove(i);
				}
			}
		}
		return map;
	}

	/**
	 * This function is used to sort the map which has month as key and
	 * transactions and amount as value. The sorting is done according to the
	 * amount in the value.
	 * 
	 * @param map
	 * @return final_map : Sorted map.
	 */
	public LinkedHashMap<String, LinkedHashMap<String, String>> sort_month(
			LinkedHashMap<String, LinkedHashMap<String, String>> map) {
		LinkedHashMap<String, String> clone_map = new LinkedHashMap<>();
		LinkedHashMap<String, LinkedHashMap<String, String>> final_map = new LinkedHashMap<>();
		LinkedHashMap<String, String> final_inner_map;
		for (String i : map.keySet()) {
			final_inner_map = new LinkedHashMap<>();
			clone_map.clear();
			for (String k : map.get(i).keySet()) {
				clone_map.put(map.get(i).get(k), k);
			}
			ArrayList<Double> sort_list = new ArrayList<>();
			for (String f : clone_map.keySet()) {
				sort_list.add(Double.parseDouble(f));
			}
			Collections.sort(sort_list);
			Collections.reverse(sort_list);
			for (Double s : sort_list) {
				String value = clone_map.get(s + "");
				String key = s + "";
				final_inner_map.put(value, key);
				final_map.put(i, final_inner_map);
			}
		}
		return final_map;
	}

	/**
	 * This function is used to find the most costly transaction in a month
	 * 
	 * @param map
	 * @return max : returned from get_max_3_month() funciton
	 */
	public ArrayList<String> get_month_max(
			LinkedHashMap<String, LinkedHashMap<String, String>> map) {
		ArrayList<String> top = new ArrayList<>();
		for (String i : map.keySet()) {
			Iterator it = map.get(i).entrySet().iterator();
			Object tmp = it.next();
			if (it.hasNext() && (tmp != null)) {
				top.add(tmp.toString());
			} else {
				tmp = it.next();
				top.add(tmp.toString());
			}
		}
		int size = top.size();
		ArrayList<Integer> ind = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			if (top.get(i).toString().toUpperCase().contains("PAYPAL")) {
				ind.add(i);
			}
		}
		int count = 0;
		for (int i : ind) {
			i = i - count;
			top.remove(i);
			count = count + 1;
		}
		return get_max_3_month(top);
	}

	/**
	 * This function finds the max transaction from top 3 transactions of a 3
	 * month period
	 * 
	 * @param top_3_3
	 * @return
	 */
	public ArrayList<String> get_max_3_month(ArrayList<String> top_3_3) {
		Double max1 = 0.0;
		String[] max_1;
		Double max2 = 0.0;
		String[] max_2;
		Double max3 = 0.0;
		String[] max_3;
		ArrayList<String> max = new ArrayList<>();
		for (int i = 0; i < top_3_3.size() - 2; i = i + 3) {
			max_1 = top_3_3.get(i).split("=");
			max1 = Double.parseDouble(max_1[1]);
			max_2 = top_3_3.get(i + 1).split("=");
			max2 = Double.parseDouble(max_2[1]);
			max_3 = top_3_3.get(i + 2).split("=");
			max3 = Double.parseDouble(max_3[1]);
			if (((max1 >= max2) && (max1 >= max3))) {
				max.add(max_1[0] + "=" + max1);
			} else if ((max2 >= max1) && (max2 >= max3)) {
				max.add(max_2[0] + "=" + max2);
			} else if ((max3 >= max1) && (max3 >= max2)) {
				max.add(max_3[0] + "=" + max3);
			}
		}
		return max;
	}

	/**
	 * This function returns the hashmap which consists of recurring
	 * transactions data.
	 * 
	 * @return recurring.
	 */
	public LinkedHashMap<String, ArrayList<String>> get_recurring() {
		return recurring;
	}
}
