package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.model.CanvasPoint

enum class CanvasAction {
	NONE,
	ELEMENT_MOVE,
	ELEMENT_SELECT,
}

data class CanvasState<ElementType>(
	var currentAction: CanvasAction = CanvasAction.NONE,
	var mouseDown: Boolean = false,
	var mouseDownPosition: CanvasPoint? = null,

    val lastRenderMousePosition: CanvasPoint = CanvasPoint(0f, 0f),
    val selectedElements: MutableSet<ElementType> = mutableSetOf(),

    var lastClickedElement: ElementType? = null,
)
