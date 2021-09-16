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

package io.github.orioncraftmc.orion.api.gui.extras.zodiac

import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.TessellatorBridge
import io.github.orioncraftmc.orion.api.bridge.setColor
import io.github.orioncraftmc.orion.api.gui.extras.zodiac.settings.ZodiacSettings
import io.github.orioncraftmc.orion.api.rendering.RenderingUtils
import org.danilopianini.util.FlexibleQuadTree
import kotlin.math.ceil
import kotlin.random.Random

class Zodiac(val options: ZodiacSettings) {

	private var currentCanvasHeight: Int = 0
	private var currentCanvasWidth: Int = 0

	internal val tilt = ZodiacPoint()

	internal val particlesQuadTree = FlexibleQuadTree<ZodiacParticle>(4)
	internal var particlesCount = 0
	fun refresh(canvasWidth: Int, canvasHeight: Int) {
		currentCanvasWidth = canvasWidth
		currentCanvasHeight = canvasHeight

		val radius = Random.nextDouble(options.dotRadiusMin, options.dotRadiusMax)

		val vx = options.velocityX
		val vy = options.velocityY

		val num = ((canvasWidth * canvasHeight) / options.density)

		/*val particlesList = particles.reversed()
		for (zodiacParticle in particlesList) {
			if (zodiacParticle.x > canvasWidth || zodiacParticle.y > canvasHeight)
				particles.remove(zodiacParticle)
		}*/

		/*if (num < particles.size)
			particles.splice(num)*/

		while (num > particlesCount) {
			val r = Random.nextDouble()
			val x = ceil(Random.nextDouble() * canvasWidth)
			val y = ceil(Random.nextDouble() * canvasHeight)
			particlesCount++
			particlesQuadTree.insert(
				ZodiacParticle(
					// position
					z = (r - options.pivot) / 4, //z
					r = radius,
					x = x,
					y = y,
					//  velocity= (random)direction * clamped random velocity
					vx = (if ((Random.nextDouble() > .5)) 1 else -1) * (Random.nextDouble() * vx),//(options.directionX || ((Random.nextDouble() > .5) ? 1 = -1)) * (Random.nextDouble() * (vx[1] - vx[0]) + vx[0]),
					vy = (if ((Random.nextDouble() > .5)) 1 else -1) * (Random.nextDouble() * vy),
					// offset
					dx = 0.0,
					dy = 0.0
				), x, y
			)
		}
	}

	private val bufferSize = 20.0
	private fun getAllParticles(): MutableList<ZodiacParticle> {
		return particlesQuadTree.query(-bufferSize, -bufferSize, currentCanvasWidth + bufferSize, currentCanvasHeight + bufferSize)
	}

	fun draw(canvasWidth: Int, canvasHeight: Int) {

		OpenGlBridge.pushMatrix()
		OpenGlBridge.enableBlend()
		OpenGlBridge.disableTexture2D()
		OpenGlBridge.enableBlendAlphaMinusSrcAlpha()

		val allParticles = getAllParticles()
		for (currentParticle in allParticles) {
			/* MOVE */
			val oldPosX = currentParticle.x
			val oldPosY = currentParticle.y
			currentParticle.x += currentParticle.vx
			currentParticle.y += currentParticle.vy
			if (currentParticle.x !in 0.0..currentCanvasWidth.toDouble() || currentParticle.x !in 0.0..currentCanvasWidth.toDouble()) {
				particlesQuadTree.remove(currentParticle, oldPosX, oldPosY)
			} else {
				particlesQuadTree.move(currentParticle, oldPosX, oldPosY, currentParticle.x, currentParticle.y)
			}

			/* POSITION */
			if (options.parallax != null) {
				val fac = currentParticle.z * options.parallax!!
				currentParticle.dx += (tilt.x * fac - currentParticle.dx) / 10
				currentParticle.dy += (tilt.y * fac - currentParticle.dy) / 10
			}

			val x = currentParticle.x + currentParticle.dx
			val y = currentParticle.y + currentParticle.dy

			handleParticleOffscreenIfNeeded(x, y, currentParticle, canvasWidth, canvasHeight)

			/* DRAW */
			RenderingUtils.drawCircle(x, y, currentParticle.r.toInt(), options.dotColor, 2.0f)

			connectParticles(currentParticle)
		}

		OpenGlBridge.enableTexture2D()
		OpenGlBridge.disableBlend()
		OpenGlBridge.popMatrix()
	}

	private fun connectParticles(
		currentParticle: ZodiacParticle
	) {

		val closeParticles = particlesQuadTree.query(
			currentParticle.x - options.linkDistance,
			currentParticle.y - options.linkDistance,
			currentParticle.x + options.linkDistance,
			currentParticle.y + options.linkDistance
		)
		for (otherParticle in closeParticles) {
			TessellatorBridge.startDrawingLineStrip()

			TessellatorBridge.setColor(options.dotColor)
			OpenGlBridge.setLineWidth(options.linkWidth.toFloat())
			TessellatorBridge.addVertex(currentParticle.x, currentParticle.y, 0.0)
			TessellatorBridge.addVertex(otherParticle.x, otherParticle.y, 0.0)

			TessellatorBridge.draw()
		}
	}

	private fun handleParticleOffscreenIfNeeded(
		x: Double,
		y: Double,
		particle: ZodiacParticle,
		canvasWidth: Int,
		canvasHeight: Int
	) {
		val oldPosX = particle.x
		val oldPosY = particle.y
		if (x < 0 || x > canvasWidth) {
			if (options.bounceX) {
				particle.vx = -particle.vx
			} else {
				particle.x = ((x + canvasWidth) % canvasWidth) - particle.dx
			}
		}

		if (y < 0 || y > canvasHeight) {
			if (options.bounceY) {
				particle.vy = -particle.vy
			} else {
				particle.y = ((y + canvasHeight) % canvasHeight) - particle.dy
			}
		}

		particlesQuadTree.move(particle, oldPosX, oldPosY, particle.x, particle.y)

	}

}
