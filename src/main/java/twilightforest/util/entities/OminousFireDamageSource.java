package twilightforest.util.entities;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.TFDataAttachments;

public class OminousFireDamageSource extends DamageSource {
    public OminousFireDamageSource(DamageSource wrappedSource) {
        super(wrappedSource.typeHolder(), wrappedSource.getDirectEntity(), wrappedSource.getEntity());
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity living) {
        if (living.getKillCredit() instanceof Zombie zombie) {
            Object attached = ((AttachmentTarget) zombie).getAttached(TFDataAttachments.ZOMBIFIED_PLAYER);
            if (attached instanceof GameProfile profile) {
                if (living instanceof Player player && player.getGameProfile().getName().equals(profile.getName())) {
                    return Component.translatable("death.attack.twilightforest.ominousFire.zombified_player.self", living.getDisplayName());
                }
                return Component.translatable("death.attack.twilightforest.ominousFire.zombified_player", living.getDisplayName(), profile.getName());
            }
        }
        return super.getLocalizedDeathMessage(living);
    }
}
