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

@file:Suppress("unused")

package io.github.orioncraftmc.orion.api

object OrionCraftConstants {

	val isDevEnvironment
		get() = System.getProperty("lightcraft.launch.dev") != null

	private val clientName: String
		get() = "OrionCraft${if (OrionCraft.clientVersion.isNostalgiaVersion) " Nostalgia" else ""}"

	val clientTitle: String
		get() {
			return "$clientName (${OrionCraft.clientVersion}/${if (isDevEnvironment) "DEV" else "PROD"})"
		}

	val clientBrand: String
		get() = "orioncraft"

	const val ORION_RESOURCE_LOCATION_NS = "orion"

	const val MINECRAFT_RESOURCE_LOCATION_NS = "minecraft"

	const val DEFAULT_PROFILE_NAME = "Default"

}
