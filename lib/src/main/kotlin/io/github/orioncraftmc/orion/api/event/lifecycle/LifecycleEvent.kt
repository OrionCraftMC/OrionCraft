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

package io.github.orioncraftmc.orion.api.event.lifecycle

import io.github.orioncraftmc.orion.api.event.Event

/**
 * Base class for lifecycle events.
 */
abstract class LifecycleEvent: Event {
	abstract val lifecycle: Lifecycle

	final override val type: Event.Type
		get() = Event.Type.LIFECYCLE

	final override val cancellable: Boolean
		get() = false

	enum class Lifecycle {
		/**
		 * Fired immediately after the main mod class is loaded.
		 */
		MOD_CONSTRUCTION,

		/**
		 * Fired when the client begins its starting cycle.
		 */
		CLIENT_STARTING,

		/**
		 * Fired after the client finishes starting.
		 */
		CLIENT_STARTED,

		/**
		 * Fired when the client receives a shutdown signal.
		 */
		CLIENT_STOPPING,

		/**
		 * Fired after the client has finished shutting down.
		 * This may not be fired if the JVM is shutting down.
		 */
		CLIENT_STOPPED
	}
}
