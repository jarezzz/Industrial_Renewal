package cassiokf.industrialrenewal.tileentity.railroad;

import cassiokf.industrialrenewal.tileentity.TileEntitySyncable;
import cassiokf.industrialrenewal.blocks.railroad.BlockLoaderRail;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityBaseLoader extends TileEntitySyncable
{

    public waitEnum waitE = waitEnum.NO_ACTIVITY;

    public waitEnum getWaitEnum() {
        if (waitE == null) {
            waitE = waitEnum.WAIT_FULL;
        }
        return waitE;
    }

    public void setWaitEnum(int value) {
        waitE = waitEnum.valueOf(value);
    }

    public void setNextWaitEnum()
    {
        int old = getWaitEnum().ordinal();
        waitE = waitEnum.valueOf(old + 1);
        Sync();
    }

    public abstract boolean isUnload();

    public void letCartPass(boolean value) {
        if (getWaitEnum() == waitEnum.NEVER) {
            return;
        }
        if (isUnload()) {
            IBlockState oldState = this.world.getBlockState(this.pos.up());
            if (oldState.getBlock() instanceof BlockLoaderRail) {
                TileEntityLoaderRail te = (TileEntityLoaderRail) this.world.getTileEntity(this.pos.up());
                if (te != null) {
                    te.ChangePass(value);
                }
            }
            return;
        }
        IBlockState oldState = this.world.getBlockState(this.pos.down(2));
        if (oldState.getBlock() instanceof BlockLoaderRail) {
            TileEntityLoaderRail te = (TileEntityLoaderRail) this.world.getTileEntity(this.pos.down(2));
            if (te != null) {
                te.ChangePass(value);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("EnumConfig", this.waitE.intValue);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("EnumConfig")) waitE = waitEnum.valueOf(compound.getInteger("EnumConfig"));
        super.readFromNBT(compound);
    }

    public enum waitEnum {
        WAIT_FULL(0),
        WAIT_EMPTY(1),
        NO_ACTIVITY(2),
        NEVER(3);

        public int intValue;

        waitEnum(int value) {
            intValue = value;
        }

        public static waitEnum valueOf(int waitNo) {
            if (waitNo > waitEnum.values().length - 1) {
                waitNo = 0;
            }
            for (waitEnum l : waitEnum.values()) {
                if (l.intValue == waitNo) return l;
            }
            throw new IllegalArgumentException("waitEnum not found");
        }
    }

}
