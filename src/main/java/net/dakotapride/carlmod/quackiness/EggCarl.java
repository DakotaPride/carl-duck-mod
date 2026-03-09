package net.dakotapride.carlmod.quackiness;

import net.dakotapride.carlmod.CarlMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;

import javax.annotation.Nullable;

public class EggCarl extends TamableAnimal implements GeoEntity {
    Level level;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public int eggTime;


    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EggCarl.class, EntityDataSerializers.BOOLEAN);
    boolean dancing;
    @Nullable
    private BlockPos jukebox;

    public EggCarl(EntityType<? extends EggCarl> entityType, Level level) {
        super(entityType, level);
        this.level = level;
        this.eggTime = this.random.nextInt(6000) + 6000;
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        // this.maxUpStep = 1.0F;
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.setAirSupply(300);
    }

    //    @Override
//    public boolean canBreatheUnderwater() {
//        return true;
//    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public static AttributeSupplier setAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5.0D)
                // Cute duck, why attack
                // .add(Attributes.ATTACK_DAMAGE, 0.0f)
                // .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20F).build();
    }

    @Override
    public boolean canStandOnFluid(@NotNull FluidState state) {
        return state.is(FluidTags.WATER);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.05F));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Tags.Items.SEEDS), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new DuckPathNavigation(this, level);
    }

    static class DuckPathNavigation extends GroundPathNavigation {
        DuckPathNavigation(EggCarl duck, Level level) {
            super(duck, level);
        }

        @Override
        protected @NotNull PathFinder createPathFinder(int p_33972_) {
            this.nodeEvaluator = new WalkNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, p_33972_);
        }

        @Override
        protected boolean hasValidPathType(@NotNull PathType types) {
            return types == PathType.WATER || super.hasValidPathType(types);
        }

        @Override
        public boolean isStableDestination(@NotNull BlockPos pos) {
            return this.level.getBlockState(pos).is(Blocks.WATER) || super.isStableDestination(pos);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 3.46D) || !this.level.getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.dancing = false;
            this.jukebox = null;
        }

        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround() && vec3d.y < 0.0D) {
            this.setDeltaMovement(vec3d.multiply(1.0D, 0.8D, 1.0D));
        }

        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(CarlMod.CARL_EGG.get());
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public void setRecordPlayingNearby(@NotNull BlockPos pos, boolean b) {
        this.jukebox = pos;
        this.dancing = b;
    }

    public boolean isDancing() {
        return this.dancing;
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource damageSource) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        //this.entityData.set(FROM_BUCKET, false);
        //this.entityData.set(SITTING, false);
        builder.define(SITTING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("EggLayTime", this.eggTime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        if (tag.contains("EggLayTime")) {
            this.eggTime = tag.getInt("EggLayTime");
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(
                                this.getX(),
                                this.getEyeY(),
                                this.getZ(),
                                SoundEvents.WITHER_SPAWN,
                                this.getSoundSource(),
                                1.0F + this.random.nextFloat(),
                                this.random.nextFloat() * 0.7F + 0.3F,
                                false
                        );
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void finishConversion(ServerLevel serverLevel) {
        BiblicallyAccurateCarlBoss biblicallyAccurateCarl = this.convertTo(CarlMod.BIBLICALLY_ACCURATE_CARL_ENTITY.get(), false);
        if (biblicallyAccurateCarl != null) {
            for (EquipmentSlot equipmentslot : this.dropPreservedEquipment(
                    p_351901_ -> !EnchantmentHelper.has(p_351901_, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)
            )) {
                SlotAccess slotaccess = biblicallyAccurateCarl.getSlot(equipmentslot.getIndex() + 300);
                slotaccess.set(this.getItemBySlot(equipmentslot));
            }


            biblicallyAccurateCarl.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(biblicallyAccurateCarl.blockPosition()), MobSpawnType.CONVERSION, null);

            if (!this.isSilent()) {
                serverLevel.levelEvent(null, 1027, this.blockPosition(), 0);
            }
            EventHooks.onLivingConvert(this, biblicallyAccurateCarl);
        }
    }

    private int getConversionProgress() {
        int i = 1;
        if (this.random.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = (int)this.getX() - 4; k < (int)this.getX() + 4 && j < 14; k++) {
                for (int l = (int)this.getY() - 4; l < (int)this.getY() + 4 && j < 14; l++) {
                    for (int i1 = (int)this.getZ() - 4; i1 < (int)this.getZ() + 4 && j < 14; i1++) {
                        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos.set(k, l, i1));
                        if (blockstate.is(Blocks.IRON_BARS) || blockstate.getBlock() instanceof BedBlock) {
                            if (this.random.nextFloat() < 0.3F) {
                                i++;
                            }

                            j++;
                        }
                    }
                }
            }
        }

        return i;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType == MobSpawnType.BUCKET) {
            return spawnGroupData;
        } else {
            return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        }
    }

    @Nullable
    @Override
    // No breeding Carl here... What the hell is wrong with you
    // Evil DakotaPrideModding be like - Breed Carl, MOAR
    public EggCarl getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return CarlMod.EGG_CARL_ENTITY.get().create(level);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (isFood(itemstack) && !isTame()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!EventHooks.onAnimalTame(this, player)) {
                    if (!this.level.isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (isFood(itemstack)) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Tags.Items.SEEDS);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.carl.walk", Animation.LoopType.LOOP));
        } else if (this.isDancing()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.carl.dancing", Animation.LoopType.LOOP));
        } else if (this.isSitting()) {
            event.getController().setAnimation(RawAnimation.begin().then("animation.carl.sitting", Animation.LoopType.LOOP));
        } else {
            event.getController().setAnimation(RawAnimation.begin().then("animation.carl.idle", Animation.LoopType.LOOP));
        }

        // Need to add specific animations for biblically accurate version

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if ("awsome".equalsIgnoreCase(this.getName().getString()) || "carltheawsome".equalsIgnoreCase(this.getName().getString())
            || "dejojo".equalsIgnoreCase(this.getName().getString()) || "dejojotheawsome".equalsIgnoreCase(this.getName().getString())) {
            // placeholder - return SoundEvents.COD_AMBIENT;
            return CarlMod.CARL_WAMP.get();
        } else if ("dragon".equalsIgnoreCase(this.getName().getString()) || "ender_dragon".equalsIgnoreCase(this.getName().getString()) || "jean".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.ENDER_DRAGON_AMBIENT;
        } else if ("mekanism".equalsIgnoreCase(this.getName().getString()) || "mekanized".equalsIgnoreCase(this.getName().getString()) || "create".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.IRON_GOLEM_REPAIR;
        } else if ("bok_choy".equalsIgnoreCase(this.getName().getString()) || "bok_choyo".equalsIgnoreCase(this.getName().getString())) {
            return CarlMod.CARL_QUACK_PLANT.get();
        } else {
            return CarlMod.CARL_QUACK.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        if ("awsome".equalsIgnoreCase(this.getName().getString()) || "carltheawsome".equalsIgnoreCase(this.getName().getString())
                || "dejojo".equalsIgnoreCase(this.getName().getString()) || "dejojotheawsome".equalsIgnoreCase(this.getName().getString())) {
            // placeholder - return SoundEvents.COD_DEATH;
            return CarlMod.CARL_WAMP.get();
        } else if ("dragon".equalsIgnoreCase(this.getName().getString()) || "ender_dragon".equalsIgnoreCase(this.getName().getString()) || "jean".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.ENDER_DRAGON_DEATH;
        } else if ("mekanism".equalsIgnoreCase(this.getName().getString()) || "mekanized".equalsIgnoreCase(this.getName().getString()) || "create".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.IRON_GOLEM_DEATH;
        } else if ("bok_choy".equalsIgnoreCase(this.getName().getString()) || "bok_choyo".equalsIgnoreCase(this.getName().getString())) {
            return CarlMod.CARL_QUACK_PLANT.get();
        } else {
            return CarlMod.CARL_QUACK.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        if ("awsome".equalsIgnoreCase(this.getName().getString()) || "carltheawsome".equalsIgnoreCase(this.getName().getString())
                || "dejojo".equalsIgnoreCase(this.getName().getString()) || "dejojotheawsome".equalsIgnoreCase(this.getName().getString())) {
            // placeholder - return SoundEvents.COD_HURT;
            return CarlMod.CARL_WAMP.get();
        } else if ("dragon".equalsIgnoreCase(this.getName().getString()) || "ender_dragon".equalsIgnoreCase(this.getName().getString()) || "jean".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.ENDER_DRAGON_HURT;
        } else if ("mekanism".equalsIgnoreCase(this.getName().getString()) || "mekanized".equalsIgnoreCase(this.getName().getString()) || "create".equalsIgnoreCase(this.getName().getString())) {
            return SoundEvents.IRON_GOLEM_HURT;
        } else if ("bok_choy".equalsIgnoreCase(this.getName().getString()) || "bok_choyo".equalsIgnoreCase(this.getName().getString())) {
            return CarlMod.CARL_QUACK_PLANT.get();
        } else {
            return CarlMod.CARL_QUACK.get();
        }
    }
}
