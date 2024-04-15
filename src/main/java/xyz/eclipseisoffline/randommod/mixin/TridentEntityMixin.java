package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

    @Shadow private boolean dealtDamage;

    protected TridentEntityMixin(
            EntityType<? extends PersistentProjectileEntity> type,
            World world, ItemStack stack) {
        super(type, world, stack);
    }

    @Override
    protected void tickInVoid() {
        dealtDamage = true;
    }
}
