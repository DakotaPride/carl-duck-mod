package net.dakotapride.carlmod;

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
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;

public class CarlDuckEntity extends TamableAnimal implements GeoEntity, Bucketable {
    Level level;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CarlDuckEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CarlDuckEntity.class, EntityDataSerializers.BOOLEAN);
    boolean dancing;
    @Nullable
    private BlockPos jukebox;

    protected CarlDuckEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.level = level;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        // this.maxUpStep = 1.0F;
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

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
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
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
        DuckPathNavigation(CarlDuckEntity duck, Level level) {
            super(duck, level);
        }

        @Override
        protected @NotNull PathFinder createPathFinder(int p_33972_) {
            this.nodeEvaluator = new WalkNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, p_33972_);
        }

        @Override
        protected boolean hasValidPathType(@NotNull BlockPathTypes types) {
            return types == BlockPathTypes.WATER || super.hasValidPathType(types);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        // this.entityData.define(DATA_INTERESTED_ID, false);
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(SITTING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean("FromBucket", this.fromBucket());
        tag.putBoolean("isSitting", this.isSitting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.getBoolean("FromBucket"));
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor serverLevelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        // boolean flag = false;
        if (spawnType == MobSpawnType.BUCKET) {
            return groupData;
        } else {
            return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, spawnType, groupData, tag);
        }
    }

    @Nullable
    @Override
    // No breeding Carl here... What the hell is wrong with you
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageableMob) {
        return null;
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

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
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

        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
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

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean b) {
        this.entityData.set(FROM_BUCKET, b);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(CarlMod.DUCK_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
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
        } else {
            return CarlMod.CARL_QUACK.get();
        }
    }
}
