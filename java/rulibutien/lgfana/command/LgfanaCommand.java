package rulibutien.lgfana.command;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import rulibutien.lgfana.gen.LgfanaSpawnGenerator;

import java.util.List;

import static rulibutien.lgfana.common.Lgfana.*;
import static rulibutien.lgfana.gen.LgfanaSpawnGenerator.OffsetSpawnY;

public class LgfanaCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "lgfana";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.lgfana.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {

        World world = sender.getEntityWorld();
        GameRules rules = world.getGameRules();
        ChunkCoordinates spawn = world.getSpawnPoint();
        LgfanaSpawnGenerator spawnGenerator = new LgfanaSpawnGenerator(world);
        List players = world.playerEntities;
        Scoreboard score = world.getScoreboard();

        if (arguments.length > 0) {

            if (arguments[0].equals("reset") && rules.getGameRuleBooleanValue(GAMERULE_SETUP) && rules.getGameRuleBooleanValue(GAMERULE_GO)) {

                rules.setOrCreateGameRule("doDaylightCycle", "false");
                rules.setOrCreateGameRule("doFireTick", "false");
                rules.setOrCreateGameRule("doMobSpawning", "false");
                rules.setOrCreateGameRule("mobGriefing", "false");

                rules.setOrCreateGameRule(GAMERULE_GO, "false");

                world.setWorldTime(0L);

                spawnGenerator.generate();

                rules.setOrCreateGameRule(GAMERULE_SETUP, "true");

                spawn = new ChunkCoordinates(spawn.posX, OffsetSpawnY, spawn.posZ);

                for (Object p : players) {
                    if (p instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) p;
                        player.setPositionAndUpdate(spawn.posX, spawn.posY, spawn.posZ);
                        player.setSpawnChunk(spawn, true);
                    }
                }

                score.func_96519_k(score.getObjective("morts"));

            }
        }

        else if (! rules.getGameRuleBooleanValue(GAMERULE_GO)) {

            rules.setOrCreateGameRule(GAMERULE_GO, "true");

            spawnGenerator.vanish();

            ScoreObjective objective = score.addScoreObjective("morts", IScoreObjectiveCriteria.deathCount);
            score.func_96530_a(0, objective);
            score.func_96530_a(2, objective);

            rules.setOrCreateGameRule("doDaylightCycle", "true");
            rules.setOrCreateGameRule("doMobSpawning", "true");

            ChunkCoordinates newSpawn = EntityPlayer.verifyRespawnCoordinates(world, spawn, true);

            while (newSpawn == null) {
                spawn = new ChunkCoordinates(spawn.posX, spawn.posY + 1, spawn.posZ);
                newSpawn = EntityPlayer.verifyRespawnCoordinates(world, spawn, true);
            }

            for (Object p : players) {

                if (p instanceof EntityPlayer) {

                    EntityPlayer player = (EntityPlayer) p;

                    player.extinguish();
                    player.heal(player.getMaxHealth());

                    if (proxy.equals(Side.CLIENT)) {
                        player.getFoodStats().setFoodLevel(20);
                        player.getFoodStats().setFoodSaturationLevel(5.0F);
                    }

                    player.setSpawnChunk(newSpawn, true);
                    player.setPositionAndUpdate(newSpawn.posX, newSpawn.posY, newSpawn.posZ);

                }

            }

        }


    }


}