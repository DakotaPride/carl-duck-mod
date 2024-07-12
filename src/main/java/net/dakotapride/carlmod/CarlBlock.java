package net.dakotapride.carlmod;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class CarlBlock extends HorizontalDirectionalBlock implements Wearable {
    VoxelShape north = Stream.of(
            Block.box(5.5, 4.5, 2.5, 10.5, 9.5, 7.5),
            Block.box(6, 5, 3, 10, 9, 7),
            Block.box(5, 0, 5, 11, 5, 12),
            Block.box(4, 1, 5.5, 5, 5, 11.5),
            Block.box(11, 1, 5.5, 12, 5, 11.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape east = Stream.of(
            Block.box(8.5, 4.5, 5.5, 13.5, 9.5, 10.5),
            Block.box(9, 5, 6, 13, 9, 10),
            Block.box(4, 0, 5, 11, 5, 11),
            Block.box(4.5, 1, 4, 10.5, 5, 5),
            Block.box(4.5, 1, 11, 10.5, 5, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape south = Stream.of(
            Block.box(5.5, 4.5, 8.5, 10.5, 9.5, 13.5),
            Block.box(6, 5, 9, 10, 9, 13),
            Block.box(5, 0, 4, 11, 5, 11),
            Block.box(11, 1, 4.5, 12, 5, 10.5),
            Block.box(4, 1, 4.5, 5, 5, 10.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape west = Stream.of(
            Block.box(2.5, 4.5, 5.5, 7.5, 9.5, 10.5),
            Block.box(3, 5, 6, 7, 9, 10),
            Block.box(5, 0, 5, 12, 5, 11),
            Block.box(5.5, 1, 11, 11.5, 5, 12),
            Block.box(5.5, 1, 4, 11.5, 5, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    VoxelShape bigNorth = Stream.of(
            Block.box(4.5, 3.5, 0.75, 11.5, 10.5, 7.75),
            Block.box(6, 5, 3, 10, 9, 7),
            Block.box(5, 0, 5, 11, 5, 12),
            Block.box(4, 1, 5.5, 5, 5, 11.5),
            Block.box(11, 1, 5.5, 12, 5, 11.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape bigEast = Stream.of(
            Block.box(8.25, 3.5, 4.5, 15.25, 10.5, 11.5),
            Block.box(9, 5, 6, 13, 9, 10),
            Block.box(4, 0, 5, 11, 5, 11),
            Block.box(4.5, 1, 4, 10.5, 5, 5),
            Block.box(4.5, 1, 11, 10.5, 5, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape bigSouth = Stream.of(
            Block.box(4.5, 3.5, 8.25, 11.5, 10.5, 15.25),
            Block.box(6, 5, 9, 10, 9, 13),
            Block.box(5, 0, 4, 11, 5, 11),
            Block.box(11, 1, 4.5, 12, 5, 10.5),
            Block.box(4, 1, 4.5, 5, 5, 10.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    VoxelShape bigWest = Stream.of(
            Block.box(0.75, 3.5, 4.5, 7.75, 10.5, 11.5),
            Block.box(3, 5, 6, 7, 9, 10),
            Block.box(5, 0, 5, 12, 5, 11),
            Block.box(5.5, 1, 11, 11.5, 5, 12),
            Block.box(5.5, 1, 4, 11.5, 5, 5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty BIG_HELMET = BooleanProperty.create("big_helmet");

    public CarlBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BIG_HELMET, false));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("text.carlmod.space_duck").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.translatable("text.carlmod.equippable.head").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return CarlMod.CARL_ITEM.get().getDefaultInstance();
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite()).setValue(BIG_HELMET, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BIG_HELMET);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        if (!state.getValue(BIG_HELMET)) {
            return switch (state.getValue(FACING)) {
                case SOUTH -> south;
                case WEST -> west;
                case EAST -> east;
                default -> north;
            };
        } else {
            return switch (state.getValue(FACING)) {
                case SOUTH -> bigSouth;
                case WEST -> bigWest;
                case EAST -> bigEast;
                default -> bigNorth;
            };
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        // player.playSound(null, pos, CarlMod.CARL_QUACK.get(), SoundSource.BLOCKS, 1.3f, 1f);

        if (player.isCrouching()) {
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                level.setBlock(pos, state.cycle(BIG_HELMET),3);
            }
        } else if (!player.isCrouching()) {
            player.playSound(CarlMod.CARL_QUACK.get(), 0.8F, 1.0F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
