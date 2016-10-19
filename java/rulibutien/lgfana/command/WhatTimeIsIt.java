package rulibutien.lgfana.command;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;

public class WhatTimeIsIt extends CommandBase {

    @Override
    public String getCommandName() {
        return "whattime";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.whattime.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {

        ServerConfigurationManager server = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
        long worldtime = sender.getEntityWorld().getWorldTime();

        long time = worldtime % 24000;
        int day = ((int) ((worldtime / 1000) / 24)) + 1;
        long hours = (time + 6000) / 1000;
        long seconds = (long) (((time + 6000) % 1000) * (60.0 / 1000.0));

        String hoursStr = String.format("%02d", hours);
        String secondsStr = String.format("%02d", seconds);

        String msg = "Day " + day + " " + hoursStr + ":" + secondsStr;

        server.sendChatMsg(new ChatComponentText(msg));

    }


}