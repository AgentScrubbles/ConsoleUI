package main_console;

import java.util.concurrent.atomic.AtomicInteger;

public class CorrelationGenerator {
	private static AtomicInteger current = new AtomicInteger(0);
	public static int generate(){
		return current.getAndIncrement();
	}
}
