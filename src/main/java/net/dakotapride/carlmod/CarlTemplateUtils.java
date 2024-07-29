package net.dakotapride.carlmod;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class CarlTemplateUtils {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    private static final Component UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", new ResourceLocation(CarlMod.MODID, "carl_upgrade"))).withStyle(TITLE_FORMAT);
    private static final Component UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(CarlMod.MODID, "smithing_template.carl_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(CarlMod.MODID, "smithing_template.carl_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(CarlMod.MODID, "smithing_template.carl_upgrade.base_slot_description")));
    private static final Component UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(CarlMod.MODID, "smithing_template.carl_upgrade.additions_slot_description")));
    private static final ResourceLocation EMPTY_SLOT_BUCKET = new ResourceLocation(CarlMod.MODID, "item/empty_slot_bucket");
    private static final ResourceLocation EMPTY_SLOT_CARL = new ResourceLocation(CarlMod.MODID, "item/empty_slot_carl");
    private static final List<ResourceLocation> SLOT_LIST_0 = List.of(EMPTY_SLOT_BUCKET);
    private static final List<ResourceLocation> SLOT_LIST_1 = List.of(EMPTY_SLOT_CARL);


    public static SmithingTemplateItem createCarlUpgradeTemplate() {
        return new SmithingTemplateItem(UPGRADE_APPLIES_TO, UPGRADE_INGREDIENTS, UPGRADE, UPGRADE_BASE_SLOT_DESCRIPTION, UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SLOT_LIST_0, SLOT_LIST_1);
    }
}
