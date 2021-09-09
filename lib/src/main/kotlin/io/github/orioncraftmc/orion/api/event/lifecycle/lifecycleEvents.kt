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

/**
 * Fired immediately after the main mod class is loaded.
 */
class ModConstructionEvent: LifecycleEvent() {
	override val lifecycle: Lifecycle
		get() = Lifecycle.MOD_CONSTRUCTION
}

/**
 * Fired when the client begins its starting cycle.
 */
class ClientStartingEvent: LifecycleEvent() {
	override val lifecycle: Lifecycle
		get() = Lifecycle.CLIENT_STARTING
}

/**
 * Fired after the client finishes starting.
 */
class ClientStartedEvent: LifecycleEvent() {
	override val lifecycle: Lifecycle
		get() = Lifecycle.CLIENT_STARTED
}

/**
 * Fired when the client receives a shutdown signal.
 */
class ClientStoppingEvent: LifecycleEvent() {
	override val lifecycle: Lifecycle
		get() = Lifecycle.CLIENT_STOPPING
}

/**
 * Fired after the client has finished shutting down.
 * This may not be fired if the JVM is shutting down.
 */
class ClientStoppedEvent: LifecycleEvent() {
	override val lifecycle: Lifecycle
		get() = Lifecycle.CLIENT_STOPPED
}
