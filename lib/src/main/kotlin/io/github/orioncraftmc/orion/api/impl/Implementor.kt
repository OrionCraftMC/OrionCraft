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

package io.github.orioncraftmc.orion.api.impl

import io.github.orioncraftmc.orion.api.logging.FallbackLoggerFactory
import io.github.orioncraftmc.orion.api.logging.LoggerFactory
import io.github.orioncraftmc.orion.api.meta.Platform
import io.github.orioncraftmc.orion.api.meta.Version
import io.github.orioncraftmc.orion.api.render.FallbackTezzellator
import io.github.orioncraftmc.orion.api.render.Tezzellator
import io.github.orioncraftmc.orion.api.scheduler.FallbackScheduler
import io.github.orioncraftmc.orion.api.scheduler.Scheduler

/**
 * Stores implementation details.
 */
class Implementor(
	val version: Version,
	val platform: Platform,
	val scheduler: Scheduler,
	val tezzellator: Tezzellator,
	val loggerFactory: LoggerFactory
) {
	companion object {
		/**
		 * Represents an empty implementor.
		 */
		private val fallback = Implementor(
			Version.INVALID,
			Platform.INVALID,
			FallbackScheduler,
			FallbackTezzellator,
			FallbackLoggerFactory
		)
		private var implementor: Implementor? = null

		fun get(): Implementor = implementor ?: fallback

		fun set(implementor: Implementor) {
			if (this.implementor != null) {
				throw IllegalStateException("Cannot reassign implementor")
			}

			this.implementor = implementor
		}
	}
}
