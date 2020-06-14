package draylar.creativesearchhotkeys.mixin;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {

    private CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Shadow protected abstract boolean isCreativeInventorySlot(Slot slot);

    @Shadow private boolean ignoreTypedCharacter;
    @Unique private int keyCode;
    @Unique private int scanCode;

    @Inject(
            method = "keyPressed",
            at = @At("HEAD")
    )
    private void store(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.keyCode = keyCode;
        this.scanCode = scanCode;
    }

    @Redirect(
            method = "keyPressed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIndex()I")
    )
    public int yeet(ItemGroup itemGroup) {
        boolean bl = !this.isCreativeInventorySlot(this.focusedSlot) || this.focusedSlot != null && this.focusedSlot.hasStack();

        if(bl && this.handleHotbarKeyPressed(keyCode, scanCode)) {
            this.ignoreTypedCharacter = true;
            return -15815815;
        }

        return ItemGroup.SEARCH.getIndex();
    }
}
