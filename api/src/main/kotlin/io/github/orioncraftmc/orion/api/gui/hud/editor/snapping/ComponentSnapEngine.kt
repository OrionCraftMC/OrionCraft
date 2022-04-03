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

package io.github.orioncraftmc.orion.api.gui.hud.editor.snapping

import io.github.orioncraftmc.components.Component

data class SnappingPositionData(val value: Double, val component: Component)

object ComponentSnapEngine {

	private fun getSnappingPositionsForComponent(component: Component, axis: SnapAxis): List<SnappingPositionData> {
		return when (axis) {
			SnapAxis.HORIZONTAL -> listOf(
				kotlin.math.ceil(component.effectiveLeft - 0.5),
				kotlin.math.ceil(component.effectiveRight + 0.5)
			)
			SnapAxis.VERTICAL -> listOf(
				kotlin.math.ceil(component.effectiveTop - 0.5),
				kotlin.math.ceil(component.effectiveBottom + 0.5)
			)
		}.map { SnappingPositionData(it, component) }
	}

	fun computeSnappingPositions(componentList: List<Component>): Map<SnapAxis, MutableList<SnappingPositionData>> {
		return SnapAxis.values().associateWith { axis ->
			componentList.flatMap { component ->
				getSnappingPositionsForComponent(component, axis)
			}.distinct().toMutableList()
		}
	}

}
