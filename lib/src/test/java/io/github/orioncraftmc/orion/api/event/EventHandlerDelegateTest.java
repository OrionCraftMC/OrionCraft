package io.github.orioncraftmc.orion.api.event;

import java.util.Arrays;

import org.junit.Test;

public class EventHandlerDelegateTest {
	@Test
	public void test() {
		Object e = EventHandlerDelegate.compile(null, "HahayesIexist");
		System.out.println(e.getClass());
		System.out.println(Arrays.toString(e.getClass().getConstructors()));
	}
}
