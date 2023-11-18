package de.totodev.spellbooktweaks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.totodev.spellbooktweaks.SpellbookTweaksMod;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.*;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@Mixin(ManaBarOverlay.class)
public abstract class ManaBarOverlayMixin {
    @Unique
    private static final ResourceLocation spellbookTweaks$TEXTURE = new ResourceLocation(SpellbookTweaksMod.MOD_ID, "textures/gui/icons.png");
    @Unique
    private static final int IMAGE_WIDTH = 114;
    @Final
    @Shadow(remap = false)
    static int IMAGE_HEIGHT = 18;
    @Unique
    private static final int MAX_BAR_WIDTH = 50;
    @Unique
    private static final int BAR_HEIGHT = 8;
    @Final
    @Shadow(remap = false)
    static int ICON_ROW_HEIGHT;
    @Final
    @Shadow(remap = false)
    static int HOTBAR_HEIGHT;
    @Final
    @Shadow(remap = false)
    static int CHAR_WIDTH;
    @Final
    @Shadow(remap = false)
    static int TEXT_COLOR;

    //TODO: Force center layout in config directly

    /**
     * @author SpellbookTweaks
     * @reason While a lot of the logic is similar, this Overwrite completely replaces the old UI
     */
    @Overwrite(remap = false)
    public static void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        var player = Minecraft.getInstance().player;

        if (!shouldShowManaBar(player))
            return;

        int maxActiveMana = (int) player.getAttributeValue(MAX_MANA.get());
        int activeMana = ClientMagicData.getPlayerMana();
        //TODO: Query reserve mana in UI
        int maxReserveMana = maxActiveMana;
        int reserveMana = activeMana;

        int configOffsetY = ClientConfigs.MANA_BAR_Y_OFFSET.get();
        int configOffsetX = ClientConfigs.MANA_BAR_X_OFFSET.get();
        int barPosX = screenWidth / 2 - IMAGE_WIDTH / 2 + configOffsetX;
        int barPosY = screenHeight - HOTBAR_HEIGHT - (int) (ICON_ROW_HEIGHT * 2.5f) - IMAGE_HEIGHT / 2 - configOffsetY;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, spellbookTweaks$TEXTURE);

        int activeManaWidth = (int) (50 * Math.min((activeMana / (double) maxActiveMana), 1));
        int reserveManaWidth = (int) (50 * Math.min((reserveMana / (double) maxReserveMana), 1));
        GuiComponent.blit(poseStack, barPosX, barPosY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 128, 128);
        GuiComponent.blit(poseStack, barPosX + 1 + MAX_BAR_WIDTH - activeManaWidth, barPosY + 5, 1 + MAX_BAR_WIDTH - activeManaWidth, 20, activeManaWidth, BAR_HEIGHT, 128, 128);
        GuiComponent.blit(poseStack, barPosX + 63, barPosY + 5, 63, 20, reserveManaWidth, BAR_HEIGHT, 128, 128);

        String manaText = String.valueOf(activeMana);
        int textPosX = barPosX + IMAGE_WIDTH / 2 - (manaText.length() * CHAR_WIDTH / 2);
        int textPosY = barPosY + 5;

        if (ClientConfigs.MANA_BAR_TEXT_VISIBLE.get()) {
            gui.getFont().drawShadow(poseStack, manaText, textPosX, textPosY, TEXT_COLOR);
        }
    }

    @Shadow(remap = false)
    public static boolean shouldShowManaBar(Player player) {
        return false;
    }
}
