package com.itsazza.launchpads.events

import com.itsazza.launchpads.LaunchPads
import com.itsazza.launchpads.pads.LaunchPadStorage
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class OnStep : Listener {
    private val instance = LaunchPads.instance

    @EventHandler
    fun onStep(event: PlayerInteractEvent) {
        // check event type
        if (event.action != Action.PHYSICAL) return

        // check block type
        val block = event.clickedBlock ?: return
        if (!block.type.name.contains("_PLATE")) return

        // check sign
        val signLocation = instance.config.getInt("signYOffset", -2)
        val dataBlock = block.getRelative(0, signLocation, 0)
        if (dataBlock.state !is Sign) return
        val sign = dataBlock.state as Sign
        if (!sign.getLine(0).equals("[flight]", true)) return

        // get launchpad data
        val player = event.player
        val launchPad = LaunchPadStorage.get(sign.getLine(1))
        if (launchPad == null) {
            player.sendMessage("§cCouldn't find flight pad with name \"${sign.getLine(1)}\"")
            return
        }

        // launch player
        launchPad.type.launch(player, sign.getLine(2).split(',').map { it.trim() }).also { if (!it) return }
        launchPad.effects?.forEach { it.play(player) }
    }
}