package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.ClientReserveManaData;
import de.totodev.spellbooktweaks.SpellbookTweaksMod;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.*;

import static de.totodev.spellbooktweaks.AttributeRegistry.MAX_RESERVE_MANA;
import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@Mixin(ManaBarOverlay.class)
public abstract class ManaBarOverlayMixin {
    @Unique
    private static final int MAX_MAX_MANA = 100;
    @Unique
    private static final ResourceLocation spellbookTweaks$TEXTURE = new ResourceLocation(SpellbookTweaksMod.MOD_ID, "textures/gui/icons.png");

    // Spritesheet layout
    @Unique
    private static final int TEXTURE_WIDTH = 154;
    @Unique
    private static final int TEXTURE_HEIGHT = 80;
    @Unique
    private static final int ALIGNMENT_GRID = 20;
    @Unique
    private static final int MAX_BAR_WIDTH = 52;
    @Unique
    private static final int ACTIVE_BAR_OFFSET = 2;
    @Unique
    private static final int RESERVE_BAR_OFFSET = 100;

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
    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
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
        int barX = screenWidth / 2 - TEXTURE_WIDTH / 2 + configOffsetX;
        int barY = screenHeight - HOTBAR_HEIGHT - (int) (ICON_ROW_HEIGHT * 2.5f) - ALIGNMENT_GRID / 2 - configOffsetY - (hasArmorRow ? ARMOR_ROW_HEIGHT : 0);

        // Mana bar
        int activeManaWidth = (int) (MAX_BAR_WIDTH * Math.min((activeMana / (double) MAX_MAX_MANA), 1));
        int reserveManaWidth = (int) (MAX_BAR_WIDTH * Math.min((reserveMana / (double) maxReserveMana), 1));
        guiHelper.blit(spellbookTweaks$TEXTURE, barX, barY, 0, 0, TEXTURE_WIDTH, ALIGNMENT_GRID, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiHelper.blit(spellbookTweaks$TEXTURE, barX + ACTIVE_BAR_OFFSET + MAX_BAR_WIDTH - activeManaWidth, barY, ACTIVE_BAR_OFFSET + MAX_BAR_WIDTH - activeManaWidth, ALIGNMENT_GRID, activeManaWidth, ALIGNMENT_GRID, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        guiHelper.blit(spellbookTweaks$TEXTURE, barX + RESERVE_BAR_OFFSET, barY, RESERVE_BAR_OFFSET, ALIGNMENT_GRID, reserveManaWidth, ALIGNMENT_GRID, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Crystal state and animation
        boolean is_recharging = activeMana < maxActiveMana;
        int crystalOffset = (TEXTURE_WIDTH - ALIGNMENT_GRID) / 2;
        int frameOffset = NEXT_FRAME * ALIGNMENT_GRID;
        if (is_recharging) {
            guiHelper.blit(spellbookTweaks$TEXTURE, barX + crystalOffset, barY, frameOffset, ALIGNMENT_GRID * 3, ALIGNMENT_GRID, ALIGNMENT_GRID, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else {
            if (SPARKLE_NEXT_FRAME == 0 && CHOOSE_NEW_SPARKLE) {
                Vector2i sparklePos = spellbookTweaks$chooseSparklePoint(gui.getMinecraft().level.random);

                // Add offset from crystal sprite origin to center of crystal
                SPARKLE_OFFSET_X = 8 + sparklePos.x;
                SPARKLE_OFFSET_Y = 8 + sparklePos.y;
                // Needed so that it doesn't repeatedly choose a new location during frame 0
                CHOOSE_NEW_SPARKLE = false;
            } else if (SPARKLE_NEXT_FRAME == 5) {
                SPARKLE_NEXT_FRAME = -4;
                CHOOSE_NEW_SPARKLE = true;
            }

            // Draw background
            guiHelper.blit(spellbookTweaks$TEXTURE, barX + crystalOffset, barY, frameOffset, ALIGNMENT_GRID * 2, ALIGNMENT_GRID, ALIGNMENT_GRID, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            // Draw sparkle
            if (SPARKLE_NEXT_FRAME >= 0) {
                int sparkleFrameOffset = 5 * ALIGNMENT_GRID + (SPARKLE_NEXT_FRAME * 5);
                guiHelper.blit(spellbookTweaks$TEXTURE, barX + crystalOffset + SPARKLE_OFFSET_X, barY + SPARKLE_OFFSET_Y, sparkleFrameOffset, ALIGNMENT_GRID * 2, 5, 5, TEXTURE_WIDTH, TEXTURE_HEIGHT);
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

        /*String activeStr = String.valueOf(activeMana);
        String reserveStr = String.valueOf(reserveMana);

        int activeTextX = barX + TEXTURE_WIDTH / 2 - 25;
        int reserveTextX = barX + TEXTURE_WIDTH / 2 + 10;
        int textY = barY + ALIGNMENT_GRID / 2 - 4;

        if (ClientConfigs.MANA_BAR_TEXT_VISIBLE.get()) {
            guiHelper.drawString(gui.getFont(), activeStr, activeTextX, textY, ChatFormatting.WHITE.getColor(), true);
            guiHelper.drawString(gui.getFont(), reserveStr, reserveTextX, textY, ChatFormatting.WHITE.getColor(), true);
        }*/
    }

    @Unique
    private Vector2i spellbookTweaks$chooseSparklePoint(RandomSource randSource) {
        // Get two random numbers from -3 to 2
        int rand = randSource.nextInt(35);
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

        return new Vector2i(x, y);
    }

    @Shadow(remap = false)
    public static boolean shouldShowManaBar(Player player) {
        return false;
    }
}
