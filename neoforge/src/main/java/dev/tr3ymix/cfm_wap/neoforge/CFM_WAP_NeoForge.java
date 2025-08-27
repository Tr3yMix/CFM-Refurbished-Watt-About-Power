package dev.tr3ymix.cfm_wap.neoforge;

import dev.tr3ymix.cfm_wap.block.CircuitBreakerBlock;
import dev.tr3ymix.cfm_wap.client.gui.screen.CircuitBreakerScreen;
import dev.tr3ymix.cfm_wap.client.renderer.CircuitBreakerBlockEntityRenderer;
import dev.tr3ymix.cfm_wap.registry.ModBlockEntities;
import dev.tr3ymix.cfm_wap.registry.ModMenuTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import dev.tr3ymix.cfm_wap.CFM_WAP;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;

@Mod(CFM_WAP.MOD_ID)
public final class CFM_WAP_NeoForge {
    public CFM_WAP_NeoForge(IEventBus modEventBus) {

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onRegisterCapabilities);
    }

    private void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(CFM_WAP::init);
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.CIRCUIT_BREAKER.get(),
                (entity, context) -> {
                    BlockState state = entity.getBlockState();
                    if(state.hasProperty(CircuitBreakerBlock.DIRECTION)){
                        return context == state.getValue(CircuitBreakerBlock.DIRECTION) ? (IEnergyStorage) entity.ENERGY_STORAGE.asPlatformHandler() : null;
                    }
                    return null;
                });
    }

    @Mod.EventBusSubscriber(modid = CFM_WAP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD ,value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            event.enqueueWork(() ->{
                BlockEntityRenderers.register(
                        ModBlockEntities.CIRCUIT_BREAKER.get(),
                        CircuitBreakerBlockEntityRenderer::new
                );
            });
        }

        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event){
            event.register(ModMenuTypes.CIRCUIT_BREAKER.get(), CircuitBreakerScreen::new);
        }
    }
}
