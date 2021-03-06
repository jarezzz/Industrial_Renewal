package cassiokf.industrialrenewal.entity;

import cassiokf.industrialrenewal.IndustrialRenewal;
import cassiokf.industrialrenewal.init.GUIHandler;
import cassiokf.industrialrenewal.init.ModItems;
import cassiokf.industrialrenewal.util.interfaces.IConnectibleCart;
import cassiokf.industrialrenewal.util.interfaces.IDirectionCart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class EntitySteamLocomotive extends EntityMinecart implements IConnectibleCart, IDirectionCart
{

    private static final DataParameter<Boolean> PLOW = EntityDataManager.createKey(EntitySteamLocomotive.class, DataSerializers.BOOLEAN);
    public boolean hasPlowItem;
    private boolean rotated;
    public ItemStackHandler inventory = new ItemStackHandler(7)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            EntitySteamLocomotive.this.Sync();
        }
    };

    public EntitySteamLocomotive(World worldIn)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    @Override
    public float getMaxCouplingDistance(EntityMinecart cart)
    {
        return 1.1f;
    }

    @Override
    public float getFixedDistance(EntityMinecart cart)
    {
        return 1f;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (!player.isSneaking())
        {
            if (!this.world.isRemote)
                player.openGui(IndustrialRenewal.instance, GUIHandler.STEAMLOCOMOTIVE, this.world, this.getEntityId(), 0, 0);
            return true;
        }
        return super.processInitialInteract(player, hand);
    }

    private boolean hasPlowItem()
    {
        boolean temp = false;
        ItemStack stack = this.inventory.getStackInSlot(6);
        if (!stack.isEmpty()) {
            temp = true;
        }
        hasPlowItem = temp;
        return hasPlowItem;
    }

    public EntitySteamLocomotive(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);

        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(ModItems.steamLocomotive, 1), 0.0F);
        }
    }

    public EntityMinecart.Type getType() {
        return Type.FURNACE;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("inventory", this.inventory.serializeNBT());
        compound.setBoolean("rotated", rotated);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.rotated = compound.getBoolean("rotated");
        this.Sync();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(ModItems.steamLocomotive);
    }

    public void Sync()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(PLOW, hasPlowItem());
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(PLOW, false);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
        if (this.world.isRemote && key.equals(PLOW))
        {
            this.hasPlowItem = this.dataManager.get(PLOW);
        }
    }

    @Override
    public boolean isRotated()
    {
        return rotated;
    }

    @Override
    public void setRotation(boolean rotation)
    {
        rotated = rotation;
    }
}
