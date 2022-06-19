package io.github.nickacpt.behaviours.canvas.model

data class CanvasPoint(var x: Float, var y: Float) {

	operator fun plus(other: CanvasPoint): CanvasPoint {
		return CanvasPoint(x + other.x, y + other.y)
	}

	operator fun minus(other: CanvasPoint): CanvasPoint {
		return CanvasPoint(x - other.x, y - other.y)
	}

}
