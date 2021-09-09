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

package io.github.orioncraftmc.orion.api.event

/**
 * The epitome of functional programming ™️
 */
interface Event {

	val cancellable: Boolean

	val type: Type
		get() = Type.PROCESS

	enum class Type {
		/**
		 * Events that are fired at different points of the game's lifecycle.
		 * E.g. client start, client stopping, client stopped, etc.
		 */
		LIFECYCLE,

		/**
		 * Events that are fired during certain processes that the client runs.
		 * E.g. hud render, keybinding press, etc.
		 */
		PROCESS
	}
}
