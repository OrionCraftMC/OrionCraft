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

package io.github.orioncraftmc.orion.api.gui.hud.mod.single

import io.github.orioncraftmc.orion.api.event.EventBus
import io.github.orioncraftmc.orion.api.event.impl.HudModComponentRefreshEvent
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.components.model.Anchor
import java.util.*

abstract class SingleHudOrionMod(id: String, name: String) : HudOrionMod<SingleHudElementType>(id, name) {

	override val allHudElements: EnumSet<SingleHudElementType> =
		EnumSet.allOf(SingleHudElementType::class.java)

	abstract fun getHudComponent(anchor: Anchor): Component?

	abstract fun getDummyHudComponent(anchor: Anchor): Component

	override fun getHudComponent(anchor: Anchor, hudElement: SingleHudElementType): Component? {
		return getHudComponent(anchor)
	}

	override fun getDummyHudComponent(anchor: Anchor, hudElement: SingleHudElementType): Component {
		return getDummyHudComponent(anchor)
	}

	fun refreshHudComponent() {
		EventBus.callEvent(HudModComponentRefreshEvent(this, SingleHudElementType.SINGLE))
	}
}
