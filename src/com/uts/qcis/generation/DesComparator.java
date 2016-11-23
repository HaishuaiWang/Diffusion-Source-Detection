package com.uts.qcis.generation;

import java.util.Comparator;

public class DesComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Pair p1 = (Pair) o1;
		Pair p2 = (Pair) o2;
		if(p1.v < p2.v)
			return 1;
		else
			return 0;
	}

}
