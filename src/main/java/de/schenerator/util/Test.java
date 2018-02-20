package de.schenerator.util;

public class Test {

	public static void main(String[] args) {
		// BigInteger i = new BigInteger("0");
		// BigInteger numFours = new BigInteger("0");
		// while (true) {
		// BigInteger iSquare = i.pow(2);
		// if (iSquare.mod(BigInteger.TEN).longValue() != 4) {
		// i = i.add(BigInteger.ONE);
		// continue;
		// }
		// BigInteger iSquareParts = i.pow(2);
		// BigInteger currentFours = new BigInteger("0");
		// while (iSquareParts.mod(BigInteger.TEN).longValue() == 4) {
		// currentFours = currentFours.add(BigInteger.ONE);
		// iSquareParts = iSquareParts.divide(BigInteger.TEN);
		// if (iSquareParts.equals(BigInteger.ZERO))
		// break;
		// }
		// if (currentFours.compareTo(numFours) > 0) {
		// numFours = currentFours;
		// System.out.println(iSquare + " = " + i + " * " + i);
		// }
		//
		// i = i.add(BigInteger.ONE);
		// }

		long balance = 37;
		long sons = 3;
		while (!fit(balance, sons)) {
			if (sons >= balance) {
				sons = 6;
				balance++;
				if (balance % 1000 == 0) {
					System.out.println("Curr balance: " + balance);
				}
			} else {
				sons++;
			}

		}

	}

	public static boolean fit(long balance, long sons) {
		if (balance % sons != 0) {
			return false;
		}
		long sum = 0;
		long prev = -1;
		long newBalance= balance;
		for (long i = 1; i <= sons; i++) {
			newBalance -= i;
//			double pow = Math.pow((6.0 / 7.0), i - 1);
			double varBal = newBalance / 7.0;
			if (!(varBal == Math.floor(varBal))) {
				return false;
			}
			long partBal = i + (long) varBal;

			if (i > 1) {
				if (partBal != prev) {
					return false;
				}
			}
			newBalance -= (long)varBal;
			prev = partBal;
			sum += partBal;
			if (sum > balance) {
				return false;
			}
		}

		System.out.println("Sum " + sum + " balance " + balance + " Sons: " + sons + " step " + prev);
		if (sum == balance) {
			return true;
		}
		return false;
	}
}
