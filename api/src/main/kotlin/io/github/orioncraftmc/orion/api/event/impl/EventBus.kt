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

@file:Suppress("UnstableApiUsage", "UNCHECKED_CAST")

package io.github.orioncraftmc.orion.api.event.impl

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import io.github.orioncraftmc.orion.api.event.CancellableEvent
import io.github.orioncraftmc.orion.api.event.Event
import io.github.orioncraftmc.orion.api.event.EventHandler

/**
 * An EventBus that allows to register and invoke event notifications
 */
object EventBus {
	@PublishedApi
	internal val eventHandlers: Multimap<Class<out Event>, EventHandler<out Event>> = HashMultimap.create()

	fun <T : Event> registerHandler(clazz: Class<T>, handler: EventHandler<T>) {
		eventHandlers[clazz].add(handler)
	}

	@JvmName("registerInlineHandler")
	inline fun <reified T : Event> registerHandler(handler: EventHandler<T>) {
		registerHandler(T::class.java, handler)
	}

	fun <T : Event> callEvent(event: T) {
		val it = eventHandlers.get(event.javaClass).iterator()
		if (event is CancellableEvent) {
			while (!event.cancelled && it.hasNext()) {
				(it.next() as EventHandler<T>).handleEvent(event)
			}
		} else {
			it.forEachRemaining { (it as EventHandler<T>).handleEvent(event) }
		}
	}

	inline fun <reified T : Event> removeAllHandlers() {
		removeAllHandlers(T::class.java)
	}

	fun removeAllHandlers(clazz: Class<out Event>) {
		eventHandlers[clazz].clear()
	}
}
