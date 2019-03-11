package com.vandendaelen.groupteleport.commands;

import com.vandendaelen.groupteleport.objects.Area;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandGroupTeleport extends CommandTeleport {
    private List<String> subcommands = Arrays.asList("range", "area", "regex", "list");
    private String name = "gtp";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        StringBuilder usageString = new StringBuilder();

        usageString.append("/"+getName()+" ");
        usageString.append(subcommands.get(0));

        for (String sc : subcommands.subList(1,subcommands.size())) {
            usageString.append(MessageFormat.format(" | {0}", sc));
        }
        usageString.append(" x y z <arg>");
        return usageString.toString();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!subcommands.contains(args[0]) || args.length < 4)
            throw new CommandException("Error : " + getUsage(sender));

        Vec3d vec3d = sender.getPositionVector();
        int j = 1;
        CommandBase.CoordinateArg xCord = parseCoordinate(vec3d.x, args[j++], true);
        CommandBase.CoordinateArg yCord = parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
        CommandBase.CoordinateArg zCord = parseCoordinate(vec3d.z, args[j++], true);

        switch (subcommands.indexOf(args[0])){
            case 0 : //range
                if (args.length < 5)
                    throw new CommandException(MessageFormat.format("/{0} range x y z <rangearea>", getName()));

                for(EntityPlayerMP player : sender.getEntityWorld().getEntitiesWithinAABB(EntityPlayerMP.class, Block.FULL_BLOCK_AABB.offset(sender.getPosition()).grow(Integer.parseInt(args[4])))){
                    doTeleport(player,xCord,yCord,zCord);
                }
                break;
            case 1 : //area
                if (args.length < 10)
                    throw new CommandException(MessageFormat.format("/{0} area <x y z> <x y z> <x y z>", getName()));

                CommandBase.CoordinateArg xCord1 = parseCoordinate(vec3d.x, args[j++], true);
                CommandBase.CoordinateArg yCord1 = parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
                CommandBase.CoordinateArg zCord1 = parseCoordinate(vec3d.z, args[j++], true);
                CommandBase.CoordinateArg xCord2 = parseCoordinate(vec3d.x, args[j++], true);
                CommandBase.CoordinateArg yCord2 = parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
                CommandBase.CoordinateArg zCord2 = parseCoordinate(vec3d.z, args[j], true);

                Area area = new Area(xCord1.getResult(),yCord1.getResult(), zCord1.getResult(), xCord2.getResult(), yCord2.getResult(), zCord2.getResult());
                for(EntityPlayerMP playerMP : area.getPlayersInArea())
                    doTeleport(playerMP, xCord, yCord, zCord);
                break;
            case 2 : //regex
                if (args.length < 5)
                    throw new CommandException(MessageFormat.format("/{0} regex x y z <regexp>", getName()));
                Pattern pattern;
                Matcher matcher;
                pattern = Pattern.compile(args[4]);
                for(EntityPlayerMP playerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
                    matcher = pattern.matcher(playerMP.getDisplayNameString());
                    if (matcher.find())
                        doTeleport(playerMP, xCord, yCord, zCord);
                }
                break;
            case 3 : //list
                if (args.length < 5)
                    throw new CommandException(MessageFormat.format("/{0} list x y z <name...s>", getName()));
                while (j < args.length){
                    doTeleport(sender.getEntityWorld().getPlayerEntityByName(args[j]), xCord, yCord, zCord);
                    j++;
                }
                break;
            default :
                throw new CommandException("Error : " + getUsage(sender));
        }
    }

    private static void doTeleport(Entity teleportingEntity, CoordinateArg argX, CoordinateArg argY, CoordinateArg argZ){
        teleportingEntity.dismountRidingEntity();
        ((EntityPlayerMP)teleportingEntity).connection.setPlayerLocation(argX.getResult(), argY.getResult(), argZ.getResult(),teleportingEntity.rotationYaw, teleportingEntity.rotationPitch);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, subcommands);
        if (args.length >= 5 && args[0].equals("list")){
            return getListOfStringsMatchingLastWord(args, Arrays.asList(FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames()));
        }
        return Collections.emptyList();
    }

    public static class Permission{
        public static final String GROUP_TELEPORT = "command.groupteleport";
    }
}
