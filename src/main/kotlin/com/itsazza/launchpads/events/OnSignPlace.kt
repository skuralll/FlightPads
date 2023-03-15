package com.itsazza.launchpads.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class OnSignPlace : Listener {
    @EventHandler
    fun onSignEdit(event: SignChangeEvent) {
        val string = event.getLine(0) ?: return
        if (!string.equals("[flight]", true)) return
        if (event.player.hasPermission("flightpads.create")) return
        event.player.sendMessage("§cYou don't have a permission to create flight pads!")
        event.isCancelled = true
    }
}