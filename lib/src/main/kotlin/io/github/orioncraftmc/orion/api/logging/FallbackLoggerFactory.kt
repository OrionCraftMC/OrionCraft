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

package io.github.orioncraftmc.orion.api.logging

object FallbackLoggerFactory: LoggerFactory {
	override fun create(name: String): Logger {
		return FallbackLogger
	}
}

object FallbackLogger: Logger {
	override fun name(): String {
		return "fallback"
	}

	override fun debug(str: String, vararg args: Any) {
		println("[debug] " + str.format(args))
	}

	override fun info(str: String, vararg args: Any) {
		println("[info] " + str.format(args))
	}

	override fun warn(str: String, vararg args: Any) {
		println("[warn] " + str.format(args))
	}

	override fun error(str: String, vararg args: Any) {
		println("[error] " + str.format(args))
	}

	override fun fatal(str: String, vararg args: Any) {
		println("[fatal] " + str.format(args))
	}
}