package net.dakotapride.carlmod.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CompatItem extends Item {
    String modId;

    public CompatItem(String requiredMod, Properties properties) {
        super(properties);
        this.modId = requiredMod;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        if (Objects.equals(modId, ModIds.CREATE.id)) {
            components.add(Component.translatable("text.carlmod.required_mod.create").withStyle(ChatFormatting.GRAY));
        }

        if (Objects.equals(modId, ModIds.AE2.id)) {
            components.add(Component.translatable("text.carlmod.required_mod.ae2").withStyle(ChatFormatting.GRAY));
        }

        if (Objects.equals(modId, ModIds.REFINED_STORAGE.id)) {
            components.add(Component.translatable("text.carlmod.required_mod.refined_storage").withStyle(ChatFormatting.GRAY));
        }

        if (Objects.equals(modId, ModIds.MEKANSIM.id)) {
            components.add(Component.translatable("text.carlmod.required_mod.mekanism").withStyle(ChatFormatting.GRAY));
        }

        if (Objects.equals(modId, ModIds.THERMAL.id)) {
            components.add(Component.translatable("text.carlmod.required_mod.thermal").withStyle(ChatFormatting.GRAY));
        }

        // super.appendHoverText(itemStack, level, components, flag);
    }

    public enum ModIds {
        // MINECRAFT("minecraft"),
        CREATE("create"),
        AE2("ae2"),
        REFINED_STORAGE("refined_storage"),
        MEKANSIM("mekanism"),
        THERMAL("thermal");

        public final String id;

        ModIds(String id) {
            this.id = id;
        }
    }
}
