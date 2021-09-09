package io.github.orioncraftmc.orion.api.event;

import java.io.IOException;

import org.junit.Test;
import org.objectweb.asm.util.ASMifier;

public class ASMifierTest {
	@Test
	public void test() throws IOException {
		ASMifier.main(new String[]{TestThing.class.getName()});
	}
}
