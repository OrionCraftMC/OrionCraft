/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modified by OrionCraftMC
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

	inline fun <reified T: Event> registerHandler(handler: EventHandler<T>) {
		eventHandlers[T::class.java].add(handler)
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
}
