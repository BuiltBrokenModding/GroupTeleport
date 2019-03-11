package com.vandendaelen.groupteleport.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandGroupTeleport extends CommandBase {
    private List<String> subcommands = Arrays.asList("range", "area", "regex", "list");
    private String name = "gtp";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        StringBuilder usageString = new StringBuilder();

        usageString.append("/"+name+" ");
        usageString.append(subcommands.get(0));

        for (String sc : subcommands.subList(1,subcommands.size())) {
            usageString.append(MessageFormat.format(" | {0}", sc));
        }

        return usageString.toString();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!subcommands.contains(args[0]))
            throw new CommandException("Error : " + getUsage(sender));

        switch (subcommands.indexOf(args[0])){
            case 0 : //range

                break;
            case 1 : //area

                break;
            case 2 : //regex

                break;
            case 3 : //list

                break;
            default :
                throw new CommandException("Error : " + getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    public static class Permission{
        public static final String GROUP_TELEPORT = "command.groupteleport";
    }
}
