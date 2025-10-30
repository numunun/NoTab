package org.TogethersChannel.noTab

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.plugin.java.JavaPlugin

class NoTab : JavaPlugin() {
    override fun onEnable() {
        logger.info("NoTab Plugin enabled.")

        val protocolManager = ProtocolLibrary.getProtocolManager()

        protocolManager.addPacketListener(object : PacketAdapter(
            this,
            ListenerPriority.NORMAL,
            PacketType.Play.Server.PLAYER_INFO
        ) {
            override fun onPacketSending(event: PacketEvent) {
                val actions = event.packet.playerInfoActions.readSafely(0) ?: return

                if (actions.contains(EnumWrappers.PlayerInfoAction.UPDATE_LISTED)) {
                    val mutable = actions.toMutableSet()
                    mutable.remove(EnumWrappers.PlayerInfoAction.UPDATE_LISTED)
                    event.packet.playerInfoActions.write(0, mutable)

                    if (mutable.isEmpty()) event.isCancelled = true
                }
            }
        })
    }

    override fun onDisable() {
        logger.info("NoTab Plugin disabled.")
    }
}
