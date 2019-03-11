package com.vandendaelen.groupteleport;

import com.vandendaelen.groupteleport.commands.CommandGroupTeleport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GroupTeleport.MODID, name =  GroupTeleport.MOD_NAME, version = GroupTeleport.VERSION.VERSION,acceptableRemoteVersions = "*")
public class GroupTeleport {
    public static final String MODID = "groupteleport";
    public static final String MOD_NAME = "GroupTeleport";

    public static class VERSION{
        public static final String MCVERSION = "1.12.2";
        public static final String MAJORMOD = "0";
        public static final String MAJORAPI = "0";
        public static final String MINOR = "0";
        public static final String PATCH = "1";
        public static final String VERSION = MCVERSION+"-"+MAJORMOD+"."+MAJORAPI+"."+MINOR+"."+PATCH;
    }

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MODID)
    public static GroupTeleport INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGroupTeleport());
    }
}
