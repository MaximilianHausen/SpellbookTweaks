package de.totodev.spellbooktweaks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.totodev.spellbooktweaks.ClientReserveManaData;
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

import static de.totodev.spellbooktweaks.AttributeRegistry.MAX_RESERVE_MANA;
import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@Mixin(ManaBarOverlay.class)
public abstract class ManaBarOverlayMixin {
    @Unique
    private static final ResourceLocation spellbookTweaks$TEXTURE = new ResourceLocation(SpellbookTweaksMod.MOD_ID, "textures/gui/icons.png");

    // Spritesheet layout
    @Unique
    private static final int IMAGE_WIDTH = 170;
    @Final
    @Shadow(remap = false)
    static final int IMAGE_HEIGHT = 20;
    @Unique
    private static final int MAX_BAR_WIDTH = 75;
    @Unique
    private static final int ACTIVE_BAR_OFFSET = 2;
    @Unique
    private static final int RESERVE_BAR_OFFSET = 92;

    // Animation
    @Unique
    private static long LAST_NANOS = 0;
    @Unique
    private static int NEXT_FRAME = 0;
    @Unique
    private static int SPARKLE_NEXT_FRAME = 0;
    @Unique
    private static int SPARKLE_OFFSET_X = 4;
    @Unique
    private static int SPARKLE_OFFSET_Y = 4;
    @Unique
    private static boolean CHOOSE_NEW_SPARKLE = true;

    @Final
    @Shadow(remap = false)
    static int ICON_ROW_HEIGHT;
    @Unique
    private static final int ARMOR_ROW_HEIGHT = 8;
    @Final
    @Shadow(remap = false)
    static int HOTBAR_HEIGHT;

    //TODO: Force center layout in config directly

    /**
     * @author SpellbookTweaks
     * @reason Completely replaces the original UI
     */
    @Overwrite(remap = false)
    public static void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        var player = Minecraft.getInstance().player;

        if (!shouldShowManaBar(player))
            return;

        int maxActiveMana = (int) player.getAttributeValue(MAX_MANA.get());
        int activeMana = ClientMagicData.getPlayerMana();
        int maxReserveMana = (int) player.getAttributeValue(MAX_RESERVE_MANA.get());
        int reserveMana = ClientReserveManaData.getReserveMana();

        boolean hasArmorRow = gui.getMinecraft().player.getArmorValue() > 0 && !gui.getMinecraft().player.isCreative();
        int configOffsetX = ClientConfigs.MANA_BAR_X_OFFSET.get();
        int configOffsetY = ClientConfigs.MANA_BAR_Y_OFFSET.get();
        int barPosX = screenWidth / 2 - IMAGE_WIDTH / 2 + configOffsetX;
        int barPosY = screenHeight - HOTBAR_HEIGHT - (int) (ICON_ROW_HEIGHT * 2.5f) - IMAGE_HEIGHT / 2 - configOffsetY - (hasArmorRow ? ARMOR_ROW_HEIGHT : 0);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, spellbookTweaks$TEXTURE);

        // Mana bar
        int activeManaWidth = (int) (MAX_BAR_WIDTH * Math.min((activeMana / (double) maxActiveMana), 1));
        int reserveManaWidth = (int) (MAX_BAR_WIDTH * Math.min((reserveMana / (double) maxReserveMana), 1));
        GuiComponent.blit(poseStack, barPosX, barPosY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 170, 80);
        GuiComponent.blit(poseStack, barPosX + ACTIVE_BAR_OFFSET + MAX_BAR_WIDTH - activeManaWidth, barPosY, ACTIVE_BAR_OFFSET + MAX_BAR_WIDTH - activeManaWidth, IMAGE_HEIGHT, activeManaWidth, IMAGE_HEIGHT, 170, 80);
        GuiComponent.blit(poseStack, barPosX + RESERVE_BAR_OFFSET, barPosY, RESERVE_BAR_OFFSET, IMAGE_HEIGHT, reserveManaWidth, IMAGE_HEIGHT, 170, 80);

        // Crystal state and animation
        boolean is_recharging = activeMana < maxActiveMana;
        int crystalOffset = (IMAGE_WIDTH - 20) / 2;
        int frameOffset = NEXT_FRAME * 20;
        if (is_recharging) {
            GuiComponent.blit(poseStack, barPosX + crystalOffset, barPosY, frameOffset, IMAGE_HEIGHT * 3, 20, IMAGE_HEIGHT, 170, 80);
        } else {
            if (SPARKLE_NEXT_FRAME == 0 && CHOOSE_NEW_SPARKLE) {
                // Choose a random point on the crystal
                int rand = gui.getMinecraft().level.random.nextInt(35);
                int x = (rand % 6) - 3;
                int y = (rand / 6) - 3;

                //Range transformation: [-3, 2] -> [-3, 3] / 0
                if (x >= 0)
                    x++;
                if (y >= 0)
                    y++;

                // Split for easier bias application. x and y will be used for storing the sign
                int absx = Math.abs(x);
                int absy = Math.abs(y);

                // Bias towards the center
                if (absx > 2 && rand % (5 - absx) == 0)
                    absx--;
                if (absy > 2 && rand % (5 - absy) == 0)
                    absy--;

                // Merge back together
                x = Integer.signum(x) * absx;
                y = Integer.signum(y) * absy;

                //Range transformation: [-3, 3] / 0 -> [-3, 2]
                if (x >= 0)
                    x--;
                if (y >= 0)
                    y--;

                // Add offset from crystal sprite origin to center of crystal
                SPARKLE_OFFSET_X = 8 + x;
                SPARKLE_OFFSET_Y = 8 + y;
                // Needed so that it doesn't repeatedly choose a new location during frame 0
                CHOOSE_NEW_SPARKLE = false;
            } else if (SPARKLE_NEXT_FRAME == 5) {
                SPARKLE_NEXT_FRAME = -4;
                CHOOSE_NEW_SPARKLE = true;
            }

            // Draw background
            GuiComponent.blit(poseStack, barPosX + crystalOffset, barPosY, frameOffset, IMAGE_HEIGHT * 2, 20, IMAGE_HEIGHT, 170, 80);

            // Draw sparkle
            if (SPARKLE_NEXT_FRAME >= 0) {
                int sparkleFrameOffset = 20 * 5 + (SPARKLE_NEXT_FRAME * 5);
                GuiComponent.blit(poseStack, barPosX + crystalOffset + SPARKLE_OFFSET_X, barPosY + SPARKLE_OFFSET_Y, sparkleFrameOffset, IMAGE_HEIGHT * 2, 5, 5, 170, 80);
            }

            // Advance sparkle frame
            if (System.nanoTime() - LAST_NANOS > 100000000L) {
                SPARKLE_NEXT_FRAME++;
            }
        }

        // Advance crystal frame
        long currentNanos = System.nanoTime();
        if (currentNanos - LAST_NANOS > 100000000L) {
            NEXT_FRAME = (NEXT_FRAME + 1) % 5;
            LAST_NANOS = currentNanos;
        }
    }

    @Shadow(remap = false)
    public static boolean shouldShowManaBar(Player player) {
        return false;
    }
}
