package io.github.orioncraftmc.orion.api.gui.model

import java.util.*

object AnchorUtils {

	val topYAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.TOP_LEFT, Anchor.TOP_MIDDLE, Anchor.TOP_RIGHT)
	val middleYAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.MIDDLE_LEFT, Anchor.MIDDLE, Anchor.MIDDLE_RIGHT)
	val bottomYAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.BOTTOM_LEFT, Anchor.BOTTOM_MIDDLE, Anchor.BOTTOM_RIGHT)

	val leftXAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.TOP_LEFT, Anchor.MIDDLE_LEFT, Anchor.BOTTOM_LEFT)
	val middleXAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.TOP_MIDDLE, Anchor.MIDDLE, Anchor.BOTTOM_MIDDLE)
	val rightXAnchorSet: EnumSet<Anchor> = EnumSet.of(Anchor.TOP_RIGHT, Anchor.MIDDLE_RIGHT, Anchor.BOTTOM_RIGHT)

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
