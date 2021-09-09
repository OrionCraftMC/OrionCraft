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
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Consumer
import kotlin.reflect.KClass

/**
 * An adaptation of [EventBus] that is modified for orion's needs.
 *
 * <p>Some methods are specific to [Event].</p>
 */
object OrionEventBus {
	private val logger: Logger = LoggerFactory.create("OrionEventBus")
	private val events: Multimap<Class<out Event>, Consumer<Event>> = HashMultimap.create()
	private val listeners: Set<Any> = HashSet()

	fun post(event: Event) {
		val it = events.get(event::class.java).iterator()
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
			logger.warn("Duplicate event handler being registered $obj")
			logger.warn("Skipping registration")
			return
		}

		var handlerCount = 0

		if (obj is KClass<*> || obj is Class<*>) {
			val clazz = if (obj is KClass<*>) {
				obj.java
			} else {
				obj as Class<*>
			}

			val methods: MutableSet<Method> = mutableSetOf()
			methods += clazz.methods
			var `super` = clazz.superclass
			while (`super` != Object::class.java) {
				methods += `super`.methods
				`super` = `super`.superclass
			}

			HashSet(methods).stream()
				.filter { Modifier.isStatic(it.modifiers) }
				.filter { it.getAnnotation(EventListener::class.java) != null }
				.forEach {
					if (it.parameterCount != 1) {
						logger.warn("Found method '$it' with EventListener annotation but with more than one parameter")
						logger.warn("Skipping registration of method")
						return@forEach
					}

					if (Event::class.java.isAssignableFrom(it.parameterTypes[0])) {
						logger.warn("Found method '$it' with EventListener annotation but with incorrect parameter type")
						logger.warn("Skipping registration of method")
						return@forEach
					}

					val newClazzName = "OrionEventHandlerDelegate_${it.name}_${it.declaringClass.name}"
					val clazzStr =
							"""
								public class $newClazzName extends ${EventHandlerDelegate::class.java.name} {
									
								}
							""".trimIndent()

				}
		}
	}
}
