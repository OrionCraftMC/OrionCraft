package io.github.orioncraftmc.orion.api.bridge.minecraft.item.inventory

import io.github.orioncraftmc.orion.api.bridge.minecraft.item.ItemStackBridge

/**
 * Immutable inventory definition.
 */
interface InventoryBridge {
	val inventorySize: Int

	fun getStack(slot: Int): ItemStackBridge

	val name: String

	val localizedName: Boolean
}
