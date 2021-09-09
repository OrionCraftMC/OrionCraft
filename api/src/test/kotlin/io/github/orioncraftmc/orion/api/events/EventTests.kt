/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.api.events

import io.github.orioncraftmc.orion.api.event.impl.EventBus
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class EventTests {
	@AfterTest
	fun cleanupEvents() {
		EventBus.removeAllHandlers<TestEvent>()
		EventBus.removeAllHandlers<CancellableTestEvent>()
	}

	@Test
	fun `Can register Event Handler`() {
		EventBus.registerHandler<TestEvent> {
			fail("Event handler should not be invoked when no event gets")
		}
	}

	@Test
	fun `Event gets invoked`() {
		var eventGotInvoked = false
		EventBus.registerHandler<TestEvent> {
			eventGotInvoked = true
		}
		EventBus.callEvent(TestEvent())

		assertTrue(eventGotInvoked)
	}

	@Test
	fun `Cancelled Events do not get propagated after being cancelled`() {

		EventBus.registerHandler<CancellableTestEvent> {
			it.cancelled = true
		}

		EventBus.registerHandler<CancellableTestEvent> {
			fail("Second Event Handler got invoked")
		}

		val event = CancellableTestEvent()
		EventBus.callEvent(event)

		assertTrue(event.cancelled)
	}

}
