package cassiokf.industrialrenewal.tileentity;

import cassiokf.industrialrenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import cassiokf.industrialrenewal.util.interfaces.IMecanicalEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityDamAxis extends TileEntityMultiBlocksTube<TileEntityDamAxis> implements IMecanicalEnergy
{
    private EnumFacing[] faces = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};

    public TileEntityDamAxis()
    {
    }

    @Override
    public int passRotation(int amount)
    {
        if (!isMaster() && !isMasterInvalid()) return getMaster().passRotation(amount);

        if (getMachinesPosSet() != null && !getMachinesPosSet().isEmpty())
        {
            IMecanicalEnergy te = null;
            for (BlockPos pos : getMachinesPosSet().keySet())
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof IMecanicalEnergy && !(tile instanceof TileEntityDamAxis))
                {
                    te = (IMecanicalEnergy) tile;
                }
            }
            if (te != null) return te.passRotation(amount);
        }
        return 0;
    }

    @Override
    public EnumFacing[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityDamAxis;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        TileEntity te = world.getTileEntity(bPos.up());
        if (!(te instanceof TileEntityDamAxis) && te instanceof IMecanicalEnergy)
        {
            addMachine(bPos.up(), EnumFacing.UP);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        return super.writeToNBT(compound);
    }
}
