package rulibutien.lgfana.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import rulibutien.lgfana.command.LgfanaCommand;
import rulibutien.lgfana.command.WhatTimeIsIt;
import rulibutien.lgfana.event.LogOutEvent;
import rulibutien.lgfana.event.LoginEvent;
import rulibutien.lgfana.event.PlacingBlock;
import rulibutien.lgfana.gen.LgfanaSpawnGenerator;
import rulibutien.lgfana.proxy.CommonProxy;

import static rulibutien.lgfana.common.Lgfana.MODID;


@Mod(modid = MODID, acceptableRemoteVersions = "*")

public class Lgfana {

    public static final String MODID = "lgfana";

    public static final String GAMERULE_GO = "lgfanaGo";
    private static final String GAMERULE_SETUP = "lgfanaSetUp";

    public static boolean ob;

    @Instance(MODID)
    public static Lgfana instance;

    @SidedProxy(clientSide = "rulibutien.lgfana.proxy.ClientProxy",
            serverSide = "rulibutien.lgfana.proxy.CommonProxy")
    public static CommonProxy proxy;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new LoginEvent());
        FMLCommonHandler.instance().bus().register(new LogOutEvent());
        MinecraftForge.EVENT_BUS.register(new PlacingBlock());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) throws NoSuchFieldException, IllegalAccessException {

        event.registerServerCommand(new LgfanaCommand());
        event.registerServerCommand(new WhatTimeIsIt());

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.getEntityWorld();
        GameRules rules = world.getGameRules();
        LgfanaSpawnGenerator spawnGenerator = new LgfanaSpawnGenerator(world);

        ob = Loader.isModLoaded("OpenBlocks");

        if (!rules.hasRule(GAMERULE_SETUP)) {

            rules.setOrCreateGameRule("doDaylightCycle", "false");
            rules.setOrCreateGameRule("doFireTick", "false");
            rules.setOrCreateGameRule("doMobSpawning", "false");
            rules.setOrCreateGameRule("mobGriefing", "false");
            if (ob) rules.setOrCreateGameRule("openblocks:spawn_graves", "false");

            world.setWorldTime(0L);

            spawnGenerator.generate();

            rules.setOrCreateGameRule(GAMERULE_GO, "false");
            rules.setOrCreateGameRule(GAMERULE_SETUP, "true");

        }

    }


}