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

package io.github.orioncraftmc.orion.api

import com.google.common.eventbus.EventBus
import io.github.orioncraftmc.orion.api.event.OrionEventBus
import io.github.orioncraftmc.orion.api.impl.Implementor

/**
 * Convenience methods to access different services provided by Orion.
 */
object OrionApi {
	val version = Implementor.get().version

	val platform = Implementor.get().platform

	val clientBrand = "orion"

	fun scheduler() = Implementor.get().scheduler

	fun tezzellator() = Implementor.get().tezzellator

	fun eventBus() = OrionEventBus

	fun loggerFactory() = Implementor.get().loggerFactory
}
