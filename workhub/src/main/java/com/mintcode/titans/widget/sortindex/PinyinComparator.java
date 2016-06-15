package com.mintcode.titans.widget.sortindex;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ChristLu
 *
 */
public class PinyinComparator implements Comparator<Map<String, Object>> {

	public int compare(Map<String, Object> map1, Map<String, Object> map2) {
		String letters1 = map1.get("letters").toString();
		String letters2 = map2.get("letters").toString();
		if (letters1.equals("@")
				|| letters2.equals("#")) {
			return -1;
		} else if (letters1.equals("#")
				|| letters2.equals("@")) {
			return 1;
		} else {
			return letters1.compareTo(letters2);
		}
	}


}
