package com.bonelf.cicada.util;

import java.util.LinkedList;
import java.util.List;

public class ToolUtils {
	/**
	 * 笛卡尔积
	 * @param nArray
	 * @return
	 */
	private static List<List<Object>> combineAlg(List<Object[]> nArray) {
		List<List<Object>> values = new LinkedList<List<Object>>();
		int[] x = new int[nArray.size()];

		boolean flag;
		do {
			/*一种组合形式**/
			List<Object> objs = new LinkedList<>();
			for (int looper = 0; looper < nArray.size(); looper++) {
				objs.add(nArray.get(looper)[x[looper]]);
			}
			flag = nextPermutation(x, nArray);
			values.add(objs);
		} while (!flag);
		/*所有组合形式**/
		return values;
	}

	private static boolean nextPermutation(int[] x, List<Object[]> nArray) {
		boolean carry = false;
		for (int looper = nArray.size() - 1; looper >= 0; looper--) {
			if (x[looper] + 1 == nArray.get(looper).length) {
				carry = true;
				x[looper] = 0;
			} else {
				x[looper] = x[looper] + 1;
				carry = false;
				return false;
			}
		}
		return carry;
	}
}
