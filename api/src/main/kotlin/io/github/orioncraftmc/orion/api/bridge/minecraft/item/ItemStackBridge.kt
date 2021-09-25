package io.github.orioncraftmc.orion.api.bridge.minecraft.item

interface ItemStackBridge {
	val stackSize: Int

	val itemId: Int

	val meta: Int
}
