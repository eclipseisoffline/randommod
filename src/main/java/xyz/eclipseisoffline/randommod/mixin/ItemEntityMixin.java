package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.Ownable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {

    @Shadow
    public abstract ItemStack getStack();

    @Shadow @Nullable private LazyEntityReference<Entity> thrower;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void tickInVoid() {
        if (getEntityWorld().getServer() != null && thrower != null && getStack().hasEnchantments()) {
            Entity player = thrower.getEntityByClass(getEntityWorld(), Entity.class);
            if (player != null) {
                setPosition(player.getEntityPos());
            }
        }
    }
}
