package com.vandendaelen.groupteleport.objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class Area {
    private BlockPos startPos;
    private BlockPos endPos;

    public Area(BlockPos startPos, BlockPos endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public Area(double x1, double y1, double z1, double x2, double y2, double z2) {
        this(new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
    }

    private boolean isInArea(EntityPlayer player){
        return (double)(player.getPosition().getX() + 1) > this.startPos.getX() && (double)player.getPosition().getX() < this.endPos.getX() && (double)(player.getPosition().getZ() + 1) > this.startPos.getZ() && (double)player.getPosition().getZ() < this.endPos.getZ() && (double)(player.getPosition().getY() + 1) > this.startPos.getY() && (double)(player.getPosition().getY() + 1) < this.endPos.getY();
    }

    public EntityPlayerMP[] getPlayersInArea(){
        List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();
        for(EntityPlayerMP playerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()){
            if(isInArea(playerMP))
                players.add(playerMP);
        }
        return players.toArray(new EntityPlayerMP[] {});
    }
}
