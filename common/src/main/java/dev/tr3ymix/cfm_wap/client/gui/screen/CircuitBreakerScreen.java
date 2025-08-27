package dev.tr3ymix.cfm_wap.client.gui.screen;

import com.mrcrayfish.furniture.refurbished.client.gui.widget.IconButton;
import com.mrcrayfish.furniture.refurbished.client.gui.widget.OnOffSlider;
import com.mrcrayfish.furniture.refurbished.client.util.ScreenHelper;
import com.mrcrayfish.furniture.refurbished.network.Network;
import com.mrcrayfish.furniture.refurbished.network.message.MessageTogglePower;
import dev.tr3ymix.cfm_wap.Config;
import dev.tr3ymix.cfm_wap.inventory.CircuitBreakerMenu;
import dev.tr3ymix.cfm_wap.util.Utils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CircuitBreakerScreen extends AbstractContainerScreen<CircuitBreakerMenu> {
    private static final ResourceLocation TEXTURE = Utils.resource("textures/gui/container/circuit_breaker.png");

    protected OnOffSlider slider;

    public CircuitBreakerScreen(CircuitBreakerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    protected void init() {
        super.init();
        this.slider = this.addRenderableWidget(
                new OnOffSlider(this.leftPos + this.imageWidth - 22 - 6, this.topPos + 5, Utils.translation("gui", "generator_toggle"),
                        (btn) -> Network.getPlay().sendToServer(new MessageTogglePower())));
    }

    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.slider.setEnabled((this.menu).isEnabled());
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if((this.menu).getEnergy() > 0){
            int energyLevel = (int)(60 * ((float)this.menu.getEnergy() / this.menu.getMaxEnergy()));
            graphics.blit(TEXTURE, this.leftPos + 26, this.topPos + 17 + 60 - energyLevel,
                    176, 60 - energyLevel, 6, energyLevel);
        }

        Status status = this.getStatus();
        graphics.blit(IconButton.ICON_TEXTURES, this.leftPos + 66, this.topPos + 29, (float)status.iconU, (float)status.iconV,
                10, 10, 64, 64);
        graphics.blit(IconButton.ICON_TEXTURES, this.leftPos + 66, this.topPos + 46, 0.0F, 10.0F,
                10, 10, 64, 64);
        if (ScreenHelper.isMouseWithinBounds(mouseX, mouseY, this.leftPos + 26, this.topPos + 17, 6, 60)) {
            this.setTooltipForNextRenderPass(Utils.translation
                    ("gui", "progress", (this.menu).getEnergy(), (this.menu).getMaxEnergy()));
        }
    }

    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        Status status = this.getStatus();
        graphics.drawString(this.font, status.label, 80, 30, status.textColour, true);
        Pair<Component, Integer> pair = this.getNodeCount();
        graphics.drawString(this.font, pair.left(), 80, 47, pair.right(), true);
    }

    private Pair<Component, Integer> getNodeCount() {
        int nodeCount = (this.menu).getNodeCount();
        int maxNodeCount = Config.SERVER.electricity.maximumNodesInCircuitBreakerNetwork.get();
        Component label = Utils.translation("gui", "node_count", nodeCount, maxNodeCount);
        int textColour = nodeCount > maxNodeCount ? -3983818 : -1;
        return Pair.of(label, textColour);
    }

    private Status getStatus() {
        if ((this.menu).isOverloaded()) {
            return Status.OVERLOADED;
        } else {
            if ((this.menu).isEnabled()) {
                if ((this.menu).isPowered()) {
                    return Status.ONLINE;
                }

                if ((this.menu).getEnergy() == 0) {
                    return Status.NO_ENERGY;
                }
            }

            return Status.OFFLINE;
        }
    }



    private enum Status {
        ONLINE(-8799453, 50, 0, Utils.translation("gui", "status.online")),
        OFFLINE(-3983818, 40, 0, Utils.translation("gui", "status.offline")),
        OVERLOADED(-2711764, 30, 0, Utils.translation("gui", "status.overloaded")),
        NO_ENERGY(-2711764, 20, 20, Utils.translation("gui", "status.energy"));

        private final int textColour;
        private final int iconU;
        private final int iconV;
        private final Component label;

        Status(int textColour, int iconU, int iconV, Component label) {
            this.textColour = textColour;
            this.iconU = iconU;
            this.iconV = iconV;
            this.label = label;
        }
    }
}
