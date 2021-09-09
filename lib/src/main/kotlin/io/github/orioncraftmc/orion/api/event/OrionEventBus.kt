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

@file:Suppress("UnstableApiUsage")

package io.github.orioncraftmc.orion.api.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.eventbus.EventBus
import io.github.orioncraftmc.orion.api.logging.Logger
import io.github.orioncraftmc.orion.api.logging.LoggerFactory
import java.util.function.Consumer

/**
 * An adaptation of [EventBus] that is modified for orion's needs.
 *
 * <p>Some methods are specific to [Event].</p>
 */
object OrionEventBus {
	private val logger: Logger = LoggerFactory.create("OrionEventBus")
	private val events: Multimap<Event, Consumer<Event>> = HashMultimap.create()
	private val listeners: Set<Any> = HashSet()

	fun post(event: Event) {
		val it = events.get(event).iterator()
		if (event is CancellableEvent) {
			while (!event.cancelled  && it.hasNext()) {
				it.next().accept(event)
			}
		} else {
			it.forEachRemaining { it.accept(event) }
		}
	}

	fun register(obj: Any) {
		if (listeners.contains(obj)) {
			logger.error("Duplicate event handler being registered $obj")
			logger.error("Skipping registration")
			return
		}

		var handlerCount = 0
	}
}
