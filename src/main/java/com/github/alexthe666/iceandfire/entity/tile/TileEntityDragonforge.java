package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeBricks;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.message.MessageUpdateDragonforge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class TileEntityDragonforge extends LockableContainerBlockEntity implements SidedInventory {

    private static final int[] SLOTS_TOP = new int[]{0, 1};
    private static final int[] SLOTS_BOTTOM = new int[]{2};
    private static final int[] SLOTS_SIDES = new int[]{0, 1};
    private static final Direction[] HORIZONTALS = new Direction[]{
        Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };
    public int fireType;
    public int cookTime;
    public int lastDragonFlameTimer = 0;
    net.minecraftforge.common.util.LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper
        .create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    private DefaultedList<ItemStack> forgeItemStacks = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private boolean prevAssembled;
    private boolean canAddFlameAgain = true;

    public TileEntityDragonforge(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
    }

    public TileEntityDragonforge(BlockPos pos, BlockState state, int fireType) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
        this.fireType = fireType;
    }

    public static void tick(World level, BlockPos pos, BlockState state, TileEntityDragonforge entityDragonforge) {
        boolean flag = entityDragonforge.isBurning();
        boolean flag1 = false;
        entityDragonforge.fireType = entityDragonforge.getFireType(entityDragonforge.getCachedState().getBlock());
        if (entityDragonforge.lastDragonFlameTimer > 0) {
            entityDragonforge.lastDragonFlameTimer--;
        }
        entityDragonforge.updateGrills(entityDragonforge.assembled());
        if (!level.isClient) {
            if (entityDragonforge.prevAssembled != entityDragonforge.assembled()) {
                BlockDragonforgeCore.setState(entityDragonforge.fireType, entityDragonforge.prevAssembled, level, pos);
            }
            entityDragonforge.prevAssembled = entityDragonforge.assembled();
            if (!entityDragonforge.assembled())
                return;
        }
        if (entityDragonforge.cookTime > 0 && entityDragonforge.canSmelt() && entityDragonforge.lastDragonFlameTimer == 0) {
            entityDragonforge.cookTime--;
        }
        if (entityDragonforge.getStack(0).isEmpty() && !level.isClient) {
            entityDragonforge.cookTime = 0;
        }
        if (!entityDragonforge.world.isClient) {
            if (entityDragonforge.isBurning()) {
                if (entityDragonforge.canSmelt()) {
                    ++entityDragonforge.cookTime;
                    if (entityDragonforge.cookTime >= entityDragonforge.getMaxCookTime()) {
                        entityDragonforge.cookTime = 0;
                        entityDragonforge.smeltItem();
                        flag1 = true;
                    }
                } else {
                    if (entityDragonforge.cookTime > 0) {
                        IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(pos.asLong(), entityDragonforge.cookTime));
                        entityDragonforge.cookTime = 0;
                    }
                }
            } else if (!entityDragonforge.isBurning() && entityDragonforge.cookTime > 0) {
                entityDragonforge.cookTime = MathHelper.clamp(entityDragonforge.cookTime - 2, 0,
                    entityDragonforge.getMaxCookTime());
            }

            if (flag != entityDragonforge.isBurning()) {
                flag1 = true;
            }
        }

        if (flag1) {
            entityDragonforge.markDirty();
        }
        if (!entityDragonforge.canAddFlameAgain) {
            entityDragonforge.canAddFlameAgain = true;
        }
    }

    @Override
    public int size() {
        return this.forgeItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.forgeItemStacks) {
            if (!itemstack.isEmpty())
                return false;
        }

        return true;
    }

    private void updateGrills(boolean grill) {
        for (Direction facing : HORIZONTALS) {
            BlockPos grillPos = this.getPos().offset(facing);
            if (this.grillMatches(this.world.getBlockState(grillPos).getBlock())) {
                BlockState grillState = this.getGrillBlock().getDefaultState().with(BlockDragonforgeBricks.GRILL, grill);
                if (this.world.getBlockState(grillPos) != grillState) {
                    this.world.setBlockState(grillPos, grillState);
                }
            }
        }
    }

    public Block getGrillBlock() {
        return switch (this.fireType) {
            case 1 -> IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 -> IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default -> IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(); // isFire == 0
        };
    }

    public boolean grillMatches(Block block) {
        return switch (this.fireType) {
            case 0 -> block == IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 -> block == IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 -> block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default -> false;
        };
    }

    @Override
    public @NotNull ItemStack getStack(int index) {
        return this.forgeItemStacks.get(index);
    }

    @Override
    public @NotNull ItemStack removeStack(int index, int count) {
        return Inventories.splitStack(this.forgeItemStacks, index, count);
    }

    @Override
    public @NotNull ItemStack removeStack(int index) {
        return Inventories.removeStack(this.forgeItemStacks, index);
    }

    @Override
    public void setStack(int index, ItemStack stack) {
        ItemStack itemstack = this.forgeItemStacks.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.areItemsEqual(stack, itemstack)
            && ItemStack.areEqual(stack, itemstack);
        this.forgeItemStacks.set(index, stack);

        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (index == 0 && !flag
            || this.cookTime > this.getMaxCookTime()) {
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);
        this.forgeItemStacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(compound, this.forgeItemStacks);
        this.cookTime = compound.getInt("CookTime");
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        compound.putInt("CookTime", (short) this.cookTime);
        Inventories.writeNbt(compound, this.forgeItemStacks);
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    public boolean isBurning() {
        return this.cookTime > 0;
    }

    public int getFireType(Block block) {
        if (block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get()
            || block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get()) {
            return 0;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_ICE_CORE.get() || block == IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get()) {
            return 1;
        }
        if (block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get()
            || block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get()) {
            return 2;
        }
        return 0;
    }

    public String getTypeID() {
        return switch (this.getFireType(this.getCachedState().getBlock())) {
            case 0 -> "fire";
            case 1 -> "ice";
            case 2 -> "lightning";
            default -> "";
        };
    }

    public int getMaxCookTime() {
        return this.getCurrentRecipe().map(DragonForgeRecipe::getCookTime).orElse(100);
    }

    private Block getDefaultOutput() {
        return this.fireType == 1 ? IafBlockRegistry.DRAGON_ICE.get() : IafBlockRegistry.ASH.get();
    }

    private ItemStack getCurrentResult() {
        Optional<DragonForgeRecipe> recipe = this.getCurrentRecipe();
        return recipe.map(DragonForgeRecipe::getResultItem)
                .orElseGet(() -> new ItemStack(this.getDefaultOutput()));
    }

    public Optional<DragonForgeRecipe> getCurrentRecipe() {
        return this.world.getRecipeManager().getFirstMatch(IafRecipeRegistry.DRAGON_FORGE_TYPE.get(), this, this.world);
    }

    public List<DragonForgeRecipe> getRecipes() {
        return this.world.getRecipeManager().listAllOfType(IafRecipeRegistry.DRAGON_FORGE_TYPE.get());
    }

    public boolean canSmelt() {
        ItemStack cookStack = this.forgeItemStacks.get(0);
        if (cookStack.isEmpty())
            return false;

        ItemStack forgeRecipeOutput = this.getCurrentResult();

        if (forgeRecipeOutput.isEmpty())
            return false;

        ItemStack outputStack = this.forgeItemStacks.get(2);
        if (!outputStack.isEmpty() && !ItemStack.areItemsEqual(outputStack, forgeRecipeOutput))
            return false;

        int calculatedOutputCount = outputStack.getCount() + forgeRecipeOutput.getCount();
        return (calculatedOutputCount <= this.getMaxCountPerStack()
            && calculatedOutputCount <= outputStack.getMaxCount());
    }

    @Override
    public boolean canPlayerUse(@NotNull PlayerEntity player) {
        if (player.getWorld().getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
                this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public void smeltItem() {
        if (!this.canSmelt())
            return;

        ItemStack cookStack = this.forgeItemStacks.get(0);
        ItemStack bloodStack = this.forgeItemStacks.get(1);
        ItemStack outputStack = this.forgeItemStacks.get(2);

        ItemStack output = this.getCurrentResult();

        if (outputStack.isEmpty()) {
            this.forgeItemStacks.set(2, output.copy());
        } else {
            outputStack.increment(output.getCount());
        }

        cookStack.decrement(1);
        bloodStack.decrement(1);
    }

    @Override
    public boolean isValid(int index, @NotNull ItemStack stack) {
        return switch (index) {
            case 1 -> this.getRecipes().stream().anyMatch(item -> item.isValidBlood(stack));
            case 0 -> true;//getRecipes().stream().anyMatch(item -> item.isValidInput(stack))
            default -> false;
        };
    }

    @Override
    public int @NotNull [] getAvailableSlots(@NotNull Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsert(int index, @NotNull ItemStack itemStackIn, Direction direction) {
        return this.isValid(index, itemStackIn);
    }

    @Override
    public boolean canExtract(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();

            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }

        return true;
    }

    @Override
    public void clear() {
        this.forgeItemStacks.clear();
    }

    @Override
    public <T> net.minecraftforge.common.util.@NotNull LazyOptional<T> getCapability(
        net.minecraftforge.common.capabilities.@NotNull Capability<T> capability, Direction facing) {
        if (!this.removed && facing != null
            && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return this.handlers[0].cast();
            if (facing == Direction.DOWN)
                return this.handlers[1].cast();
            else
                return this.handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected @NotNull Text getContainerName() {
        return Text.translatable("container.dragonforge_fire" + DragonType.getNameFromInt(this.fireType));
    }

    public void transferPower(int i) {
        if (!this.world.isClient) {
            if (this.canSmelt()) {
                if (this.canAddFlameAgain) {
                    this.cookTime = Math.min(this.getMaxCookTime() + 1,
                            this.cookTime + i);
                    this.canAddFlameAgain = false;
                }
            } else {
                this.cookTime = 0;
            }
            IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(this.pos.asLong(), this.cookTime));
        }
        this.lastDragonFlameTimer = 40;
    }

    private boolean checkBoneCorners(BlockPos pos) {
        return this.doesBlockEqual(pos.north().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && this.doesBlockEqual(pos.north().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && this.doesBlockEqual(pos.south().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get())
            && this.doesBlockEqual(pos.south().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get());
    }

    private boolean checkBrickCorners(BlockPos pos) {
        return this.doesBlockEqual(pos.north().east(), this.getBrick()) && this.doesBlockEqual(pos.north().west(), this.getBrick())
            && this.doesBlockEqual(pos.south().east(), this.getBrick()) && this.doesBlockEqual(pos.south().west(), this.getBrick());
    }

    private boolean checkBrickSlots(BlockPos pos) {
        return this.doesBlockEqual(pos.north(), this.getBrick()) && this.doesBlockEqual(pos.east(), this.getBrick())
            && this.doesBlockEqual(pos.west(), this.getBrick()) && this.doesBlockEqual(pos.south(), this.getBrick());
    }

    private boolean checkY(BlockPos pos) {
        return this.doesBlockEqual(pos.up(), this.getBrick()) && this.doesBlockEqual(pos.down(), this.getBrick());
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket packet) {
        this.readNbt(packet.getNbt());
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    public boolean assembled() {
        return this.checkBoneCorners(this.pos.down()) && this.checkBrickSlots(this.pos.down()) && this.checkBrickCorners(this.pos)
            && this.atleastThreeAreBricks(this.pos) && this.checkY(this.pos) && this.checkBoneCorners(this.pos.up()) && this.checkBrickSlots(this.pos.up());
    }

    private Block getBrick() {
        return switch (this.fireType) {
            case 0 -> IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 -> IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            default -> IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
        };
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return this.world.getBlockState(pos).getBlock() == block;
    }

    private boolean atleastThreeAreBricks(BlockPos pos) {
        int count = 0;
        for (Direction facing : HORIZONTALS) {
            if (this.world.getBlockState(pos.offset(facing)).getBlock() == this.getBrick()) {
                count++;
            }
        }
        return count > 2;
    }

    @Override
    public ScreenHandler createMenu(int id, @NotNull PlayerInventory playerInventory, @NotNull PlayerEntity player) {
        return new ContainerDragonForge(id, this, playerInventory, new ArrayPropertyDelegate(0));
    }

    @Override
    protected @NotNull ScreenHandler createScreenHandler(int id, @NotNull PlayerInventory player) {
        return new ContainerDragonForge(id, this, player, new ArrayPropertyDelegate(0));
    }
}
