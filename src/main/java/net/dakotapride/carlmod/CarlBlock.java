package net.dakotapride.carlmod;

import com.mojang.serialization.MapCodec;
import net.dakotapride.carlmod.config.CarlConfig;
import net.dakotapride.carlmod.register.ModBlocks;
import net.dakotapride.carlmod.register.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class CarlBlock extends HorizontalDirectionalBlock implements Equipable {
    public static final MapCodec<CarlBlock> CODEC = simpleCodec(CarlBlock::new);
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
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CarlBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BIG_HELMET, false).setValue(POWERED, false));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return ModBlocks.CARL_ITEM.get().getDefaultInstance();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            if (flag) {
                this.attemptToQuack(level, pos);
            }

            level.setBlock(pos, state.setValue(POWERED, flag), 3);
        }
    }

    public boolean attemptToQuack(Level level, BlockPos pos) {
        return this.attemptToQuack(null, level, pos);
    }

    public boolean attemptToQuack(@Nullable Entity entity, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            level.playSound(null, pos, ModSounds.CARL_QUACK.get(), SoundSource.BLOCKS, CarlConfig.QUACK_RIGHT_CLICK_VOLUME.get().floatValue(), CarlConfig.QUACK_RIGHT_CLICK_PITCH.get().floatValue());
            level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext context, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("text.carlmod.space_duck").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.translatable("text.carlmod.equippable.head").withStyle(ChatFormatting.BLUE));
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
        builder.add(FACING, BIG_HELMET, POWERED);
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionHand hand = player.getUsedItemHand();

        if (player.isCrouching()) {
            if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
                level.setBlock(pos, state.cycle(BIG_HELMET),3);
            }
        } else if (!player.isCrouching()) {
            player.playSound(ModSounds.CARL_QUACK.get(), CarlConfig.QUACK_RIGHT_CLICK_VOLUME.get().floatValue(), CarlConfig.QUACK_RIGHT_CLICK_PITCH.get().floatValue());
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
