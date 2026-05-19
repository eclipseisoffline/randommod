package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {

    @Shadow
    public abstract ItemStack getItem();

    @Shadow @Nullable private EntityReference<Entity> thrower;

    public ItemEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void onBelowWorld() {
        if (level().getServer() != null && thrower != null && getItem().isEnchanted()) {
            Entity player = thrower.getEntity(level(), Entity.class);
            if (player != null) {
                setPos(player.position());
            }
        }
    }
}
