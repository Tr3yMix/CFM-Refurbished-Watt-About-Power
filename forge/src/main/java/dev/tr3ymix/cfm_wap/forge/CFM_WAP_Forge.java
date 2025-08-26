package dev.tr3ymix.cfm_wap.forge;


import dev.architectury.platform.forge.EventBuses;
import dev.tr3ymix.cfm_wap.CFM_WAP;
import dev.tr3ymix.cfm_wap.client.gui.screen.CircuitBreakerScreen;
import dev.tr3ymix.cfm_wap.client.renderer.CircuitBreakerBlockEntityRenderer;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(CFM_WAP.MOD_ID)
public final class CFM_WAP_Forge {
    public CFM_WAP_Forge() {

        @SuppressWarnings("removal")
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EventBuses.registerModEventBus(CFM_WAP.MOD_ID, eventBus);

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, ForgeEvents::onAttachCapabilities);

        CFM_WAP.init();
    }

    @Mod.EventBusSubscriber(modid = CFM_WAP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.CIRCUIT_BREAKER.get(), CircuitBreakerScreen::new);
            BlockEntityRenderers.register(ModBlockEntities.CIRCUIT_BREAKER.get(), CircuitBreakerBlockEntityRenderer::new);
        }

    }
}
