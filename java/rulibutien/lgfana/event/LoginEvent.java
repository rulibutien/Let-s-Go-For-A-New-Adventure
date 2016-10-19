package rulibutien.lgfana.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import static rulibutien.lgfana.common.Lgfana.GAMERULE_GO;
import static rulibutien.lgfana.gen.LgfanaSpawnGenerator.OffsetSpawnY;

public class LoginEvent {

    private static final String Tag = "lgfanaFirstJoin";

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {

        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        GameRules rules = world.getGameRules();

        NBTTagCompound tag = player.getEntityData();
        NBTBase modeTag = tag.getTag(Tag);

        if (modeTag == null && ! rules.getGameRuleBooleanValue(GAMERULE_GO)) {

            modeTag = new NBTTagLong(0L);
            tag.setTag(Tag, modeTag);

            ChunkCoordinates spawn = world.getSpawnPoint();
            spawn = new ChunkCoordinates(spawn.posX, OffsetSpawnY, spawn.posZ);
            player.setPositionAndUpdate(spawn.posX, spawn.posY, spawn.posZ);
            player.setSpawnChunk(spawn, true);

        }

    }

}

