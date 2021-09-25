package io.github.orioncraftmc.orion.api.bridge.minecraft.entity

import io.github.orioncraftmc.orion.api.bridge.minecraft.item.inventory.PlayerInventoryBridge

interface EntityPlayerBridge {
	val playerInventory: PlayerInventoryBridge
}
