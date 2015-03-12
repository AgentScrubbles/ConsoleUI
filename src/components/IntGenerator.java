package components;

import java.util.concurrent.atomic.AtomicInteger;

public class IntGenerator {
	private static AtomicInteger currentCorrelation = new AtomicInteger(0);
	private static AtomicInteger currentUniqueID = new AtomicInteger(Integer.MIN_VALUE);
	public static int generateCorrelation(){
		return currentCorrelation.getAndIncrement();
	}
	public static int generateUniqueID(){
		return currentUniqueID.getAndIncrement();
	}
}
