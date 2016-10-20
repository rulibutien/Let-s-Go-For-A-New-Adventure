package rulibutien.lgfana.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import rulibutien.lgfana.gen.LgfanaSpawnGenerator;

import java.util.Collections;
import java.util.List;

import static rulibutien.lgfana.common.Lgfana.GAMERULE_GO;
import static rulibutien.lgfana.gen.LgfanaSpawnGenerator.OffsetSpawnY;

public class LgfanaCommand extends CommandBase {

    private static final String goSuccess = new ChatComponentTranslation("commands.lgfana.success.go").getUnformattedTextForChat();
    private static final String resetSuccess = new ChatComponentTranslation("commands.lgfana.success.reset").getUnformattedTextForChat();
    private static final String usage = new ChatComponentTranslation("commands.lgfana.usage").getUnformattedTextForChat();
    private static final String goError = new ChatComponentTranslation("commands.lgfana.error.go").getUnformattedTextForChat();
    private static final String goPending = new ChatComponentTranslation("commands.lgfana.pending.go").getUnformattedTextForChat();
    private static final String resetPending = new ChatComponentTranslation("commands.lgfana.pending.reset").getUnformattedTextForChat();

    private World world;

    @Override
    public String getCommandName() {
        return "lgfana";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return usage;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] arguments) {
        return arguments.length == 1 ? getListOfStringsFromIterableMatchingLastWord(arguments, Collections.singletonList("reset")) : null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {

        this.world = sender.getEntityWorld();
        GameRules rules = this.world.getGameRules();

        if (arguments.length == 1) {

            if (arguments[0].equals("reset")) {

                broadcast(resetPending);
                setUpWorld(false);
                setUpPlayers(false);

            } else throw new WrongUsageException(usage);

        } else if (arguments.length > 1) throw new WrongUsageException(usage);

        else {

            if (!rules.getGameRuleBooleanValue(GAMERULE_GO)) {

                broadcast(goPending);
                setUpWorld(true);
                setUpPlayers(true);

            } else throw new WrongUsageException(goError);

        }
    }

    private void broadcast(String msg) {

        List players = this.world.playerEntities;

        for (Object p : players) {
            if (p instanceof EntityPlayer) {
                ((EntityPlayer) p).addChatMessage(new ChatComponentText(msg));
            }
        }

    }

    private void displayScore(Scoreboard score, int slot, ScoreObjective objective) {
        score.func_96530_a(slot, objective);
    }

    private void removeScore(Scoreboard score, ScoreObjective objective) {
        score.func_96519_k(objective);
    }

    private void setUpPlayers(boolean status) {

        List players = this.world.playerEntities;

        ChunkCoordinates spawn = this.world.getSpawnPoint();
        ChunkCoordinates newSpawn = EntityPlayer.verifyRespawnCoordinates(this.world, spawn, true);

        if (status) {
            while (newSpawn == null) {
                spawn = new ChunkCoordinates(spawn.posX, spawn.posY + 1, spawn.posZ);
                newSpawn = EntityPlayer.verifyRespawnCoordinates(this.world, spawn, true);
            }
        }

        spawn = status ? newSpawn : new ChunkCoordinates(spawn.posX, OffsetSpawnY, spawn.posZ);

        for (Object p : players) {

            if (p instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) p;
                player.extinguish();
                player.heal(player.getMaxHealth());
                player.getFoodStats().addStats(20, 2.0F);
                player.setSpawnChunk(spawn, true);
                player.setPositionAndUpdate(spawn.posX, spawn.posY, spawn.posZ);
                player.addChatMessage(new ChatComponentText(status ? goSuccess : resetSuccess));

            }

        }

    }

    private void setUpWorld(boolean status) {

        GameRules rules = this.world.getGameRules();
        Scoreboard score = this.world.getScoreboard();
        ScoreObjective objective = score.getObjective("deaths");
        LgfanaSpawnGenerator spawnGenerator = new LgfanaSpawnGenerator(this.world);

        if (status) spawnGenerator.vanish();
        else spawnGenerator.generate();

        if (!(objective == null || status)) removeScore(score, objective);

        if (status) {
            objective = score.addScoreObjective("deaths", IScoreObjectiveCriteria.deathCount);
            displayScore(score, Scoreboard.getObjectiveDisplaySlotNumber("belowName"), objective);
            displayScore(score, Scoreboard.getObjectiveDisplaySlotNumber("list"), objective);
        }

        rules.setOrCreateGameRule("doDaylightCycle", status + "");
        rules.setOrCreateGameRule("doMobSpawning", status + "");
        rules.setOrCreateGameRule(GAMERULE_GO, status + "");

        this.world.setWorldTime(0L);

    }


}