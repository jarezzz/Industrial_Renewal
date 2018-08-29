package cassiokf.industrialrenewal.network;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.References;
import cassiokf.industrialrenewal.util.GUIHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.NETWORKCHANNEL);
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialRenewal.instance, new GUIHandler());

        INSTANCE.registerMessage(new PacketUpdateFirstAidKit.Handler(), PacketUpdateFirstAidKit.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(new PacketRequestUpdateFirstAidKit.Handler(), PacketRequestUpdateFirstAidKit.class, 1, Side.SERVER);

        INSTANCE.registerMessage(new PacketSteamLocomotive.Handler(), PacketSteamLocomotive.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnSteamLocomotive.Handler(), PacketReturnSteamLocomotive.class, 3, Side.SERVER);

        INSTANCE.registerMessage(new PacketRecordPlayer.Handler(), PacketRecordPlayer.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(new PacketReturnRecordPlayer.Handler(), PacketReturnRecordPlayer.class, 5, Side.SERVER);
    }
}
