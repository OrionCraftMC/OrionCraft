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

package io.github.orioncraftmc.orion.api.mod.settings

import com.google.common.reflect.TypeToken
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.mod.OrionMod
import kotlin.reflect.KProperty

abstract class AbstractModSetting<T>(val default: T) {
	val mapper get() = OrionCraft.settingsProvider.mapper

	private val modificationNotificationList = mutableListOf<(T) -> Unit>()

	fun addModificationNotification(handler: (T) -> Unit) {
		modificationNotificationList.add(handler)
	}

	var name: String = ""
		private set

	operator fun <M : OrionMod> provideDelegate(
		mod: M, property: KProperty<*>
	): AbstractModSetting<T> {
		name = property.name
		mod.settings.add(this)
		return this
	}

	var cachedValue: T? = null

	inline operator fun <reified U : T> getValue(
		mod: OrionMod,
		property: KProperty<*>
	): U {
		return cachedValue as? U ?: (getModSettingValue<U>(mod, name) ?: (default as U)).also {
			cachedValue = it
		}
	}

	operator fun setValue(mod: OrionMod, property: KProperty<*>?, value: T) {
		cachedValue = value

		val rawModSettings = getRawModSettings(mod)
		rawModSettings[name] = mapper.valueToTree(value)

		notifyUpdate()
	}

	fun notifyUpdate(mod: OrionMod? = null) {
		OrionCraft.settingsProvider.save()
		val value = cachedValue
		if (value != null) {
			if (mod != null) setValue(mod, null, value)
			modificationNotificationList.forEach { it.invoke(value) }
		}
	}

	fun getRawModSettings(mod: OrionMod) =
		OrionCraft.settingsProvider.currentProfile.getOrPut(mod.id) { mutableMapOf() }

	@Suppress("UnstableApiUsage")
	inline fun <reified U> getModSettingValue(mod: OrionMod, name: String): U? {
		val type = object : TypeToken<U>(mod.javaClass) {}.type

		return getRawModSettings(mod)[name]?.let {
			mapper.readValue(
				mapper.treeAsTokens(it),
				mapper.constructType(type)
			)
		}
	}
}
