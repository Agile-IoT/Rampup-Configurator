package at.tugraz.ist.ase.genconfgen;

import java.util.ArrayList;

public class QuickXPlain {
	private static ClingoExecutor clingoExecutor;
	
	public QuickXPlain(ClingoExecutor clingoExecutor) {
		QuickXPlain.clingoExecutor = clingoExecutor;
	}
	
	public ArrayList<String> quickXPlain(ArrayList<String> c, String b) {
		ArrayList<String> alB = new ArrayList<String>();
		alB.add(b);
		// if isEmpty(C) or inconsistent(B) return {};
		if (c.isEmpty() || !clingoExecutor.isConsistent(alB)) {
			return new ArrayList<String>();
		}
		// else return QX({}, C, B);
		return qx(new ArrayList<String>(), c, alB);
	}

	private static ArrayList<String> qx(ArrayList<String> d, ArrayList<String> c, ArrayList<String> b) {
		// if D!={} and inconsistent(B) return {};
		if (!d.isEmpty() && !clingoExecutor.isConsistent(b)) {
			return new ArrayList<String>();
		}
		// if singleton(C) return C;
		if (isSingleton(c)) {
			return c;
		}
		// k=q/2;
		int k = c.size() / 2;
		// C_1 = {c_1..c_k}; C_2 = {c_k+1..c_q};
		ArrayList<String> c1 = new ArrayList<String>();
		c1.addAll(c.subList(0, k));
		ArrayList<String> c2 = new ArrayList<String>();
		c2.addAll(c.subList(k, c.size()));
		// CS_1 = QX(C_2, C_1, B∪C_2);
		ArrayList<String> cs1 = qx(c2, c1, union(b, c2));
		// CS_2 = QX(CS_1, C_2, B∪CS_1);
		ArrayList<String> cs2 = qx(cs1, c2, union(b, cs1));
		// return (CS_1∪CS_2);
		return union(cs1, cs2);
	}

	private static ArrayList<String> union(ArrayList<String> first, ArrayList<String> second) {
		if (!second.isEmpty()) {
			if (!first.isEmpty()) {
				@SuppressWarnings("unchecked")
				ArrayList<String> result = (ArrayList<String>) first.clone();
				for (String constraint : second) {
					if (!result.contains(constraint)) {
						result.add(constraint);
					}
				}
				return result;
			} else {
				return second;
			}
		} else {
			return first;
		}
	}

	private static boolean isSingleton(ArrayList<String> c) {
		if (c.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
}
