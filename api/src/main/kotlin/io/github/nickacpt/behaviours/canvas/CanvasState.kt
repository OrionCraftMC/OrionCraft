package io.github.nickacpt.behaviours.canvas

import io.github.nickacpt.behaviours.canvas.model.CanvasPoint

data class CanvasState<ElementType>(
    var mouseDown: Boolean = false,

    val lastRenderMousePosition: CanvasPoint = CanvasPoint(0f, 0f),
    val selectedElements: MutableSet<ElementType> = mutableSetOf(),

    var lastClickedElement: ElementType? = null,
)
