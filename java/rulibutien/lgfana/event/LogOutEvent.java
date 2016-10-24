package rulibutien.lgfana.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import static rulibutien.lgfana.common.Lgfana.GAMERULE_GO;
import static rulibutien.lgfana.common.Lgfana.timeStopsWhenEmpty;

public class LogOutEvent {

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {

        GameRules rules = event.player.getEntityWorld().getGameRules();

        if (timeStopsWhenEmpty) {
            if (rules.getGameRuleBooleanValue(GAMERULE_GO)) {
                if (MinecraftServer.getServer().getCurrentPlayerCount() == 1) {
                    rules.setOrCreateGameRule("doDaylightCycle", "false");
                }
            }
        }

    }

}

