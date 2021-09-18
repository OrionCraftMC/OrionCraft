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

package io.github.orioncraftmc.orion.api.gui.utils

import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
import java.util.*

object AnchorUtils {

	private val topYAnchorSet = EnumSet.of(Anchor.TOP_LEFT, Anchor.TOP_MIDDLE, Anchor.TOP_RIGHT)
	private val middleYAnchorSet = EnumSet.of(Anchor.MIDDLE_LEFT, Anchor.MIDDLE, Anchor.MIDDLE_RIGHT)
	private val bottomYAnchorSet = EnumSet.of(Anchor.BOTTOM_LEFT, Anchor.BOTTOM_MIDDLE, Anchor.BOTTOM_RIGHT)

	private val leftXAnchorSet = EnumSet.of(Anchor.TOP_LEFT, Anchor.MIDDLE_LEFT, Anchor.BOTTOM_LEFT)
	private val middleXAnchorSet = EnumSet.of(Anchor.TOP_MIDDLE, Anchor.MIDDLE, Anchor.BOTTOM_MIDDLE)
	private val rightXAnchorSet = EnumSet.of(Anchor.TOP_RIGHT, Anchor.MIDDLE_RIGHT, Anchor.BOTTOM_RIGHT)

	fun computePosition(position: Point, size: Size, anchor: Anchor, containerSize: Size = Size(0.0, 0.0)): Point {

		val (isXLeft, isXMiddle, isXRight) = extractXInformationFromAnchor(anchor)
		val (isYTop, isYMiddle, isYBottom) = extractYInformationFromAnchor(anchor)

		val x = handleAnchoringOnPointInsideContainer(isXLeft, isXMiddle, isXRight, position.x, size.width, containerSize.width)
		val y = handleAnchoringOnPointInsideContainer(isYTop, isYMiddle, isYBottom, position.y, size.height, containerSize.height)

		return Point(x, y)
	}

	private fun extractXInformationFromAnchor(anchor: Anchor): Triple<Boolean, Boolean, Boolean> {
		val isXLeft = leftXAnchorSet.contains(anchor)
		val isXMiddle = middleXAnchorSet.contains(anchor)
		val isXRight = rightXAnchorSet.contains(anchor)
		return Triple(isXLeft, isXMiddle, isXRight)
	}

	private fun extractYInformationFromAnchor(anchor: Anchor): Triple<Boolean, Boolean, Boolean> {
		val isYTop = topYAnchorSet.contains(anchor)
		val isYMiddle = middleYAnchorSet.contains(anchor)
		val isYBottom = bottomYAnchorSet.contains(anchor)
		return Triple(isYTop, isYMiddle, isYBottom)
	}

	private fun handleAnchoringOnPointInsideContainer(
		isOriginalPos: Boolean,
		isMiddle: Boolean,
		isExtraSize: Boolean,
		originalItemPoint: Double,
		originalItemSize: Double,
		originalContainerSize: Double = 0.0
	) = when {
		isOriginalPos -> {
			originalItemPoint
		}
		isMiddle -> {
			(originalContainerSize / 2) - (originalItemPoint / 2) - (originalItemSize / 2)
		}
		isExtraSize -> {
			originalContainerSize - originalItemPoint - originalItemSize
		}
		else -> throw NotImplementedError()
	}

}
