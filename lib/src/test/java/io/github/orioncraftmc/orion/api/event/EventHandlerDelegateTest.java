/*
 * Copyright (C) 2021 OrionCraftMC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.github.orioncraftmc.orion.api.event;

import java.lang.reflect.Method;
import java.util.Arrays;

import io.github.orioncraftmc.orion.api.event.render.HudRenderEvent;
import org.junit.Test;

public class EventHandlerDelegateTest {
	@Test
	public void test() throws NoSuchMethodException {
		Method m = this.getClass().getMethod("lol", HudRenderEvent.class);
		EventHandlerDelegate e = EventHandlerDelegate.generateStaticMethodHandler(m, "HahayesIexist");
		System.out.println(e.getClass());
		System.out.println(Arrays.toString(e.getClass().getConstructors()));
		long start = System.nanoTime();
		lol(new HudRenderEvent(0.04F));
		long end = System.nanoTime();
		System.out.println("normal way 1 (slow): " + (end - start));
		start = System.nanoTime();
		lol(new HudRenderEvent(0.04F));
		end = System.nanoTime();
		System.out.println("normal way 2: " + (end - start));
		start = System.nanoTime();
		lol(new HudRenderEvent(0.04F));
		end = System.nanoTime();
		System.out.println("normal way 3: " + (end - start));
		start = System.nanoTime();
		e.call(new HudRenderEvent(0.5F));
		end = System.nanoTime();
		System.out.println("my way: " + (end - start));
		start = System.nanoTime();
		e.call(new HudRenderEvent(0.5F));
		end = System.nanoTime();
		System.out.println("my way 2: " + (end - start));
		start = System.nanoTime();
		e.call(new HudRenderEvent(0.5F));
		end = System.nanoTime();
		System.out.println("my way 3: " + (end - start));
	}

	public static void lol(HudRenderEvent event) {
		System.out.println("i am very smart");
		System.out.println(event.getTickDelta());
	}
}
