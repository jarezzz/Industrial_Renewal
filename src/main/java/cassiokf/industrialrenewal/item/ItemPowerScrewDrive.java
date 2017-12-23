package cassiokf.industrialrenewal.item;

import cassiokf.industrialrenewal.blocks.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPowerScrewDrive extends ItemBase {

    public ItemPowerScrewDrive(String name) {
        super(name);
        maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer entity, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (entity.isSneaking()) {
            if (world.getBlockState(pos).getBlock() == ModBlocks.fluidPipe || world.getBlockState(pos).getBlock() == ModBlocks.energyCable) {
                ItemStack items = new ItemStack(ItemBlock.getItemFromBlock(world.getBlockState(pos).getBlock()), 1);
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative()) {
                    entity.inventory.addItemStackToInventory(items);
                }
                world.setBlockToAir(pos);
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.floorPipe || world.getBlockState(pos).getBlock() == ModBlocks.floorCable || world.getBlockState(pos).getBlock() == ModBlocks.floorLamp) {
                playDrillSound(world, pos);
                if (!world.isRemote && !entity.isCreative()) {
                    if (world.getBlockState(pos).getBlock() == ModBlocks.floorPipe) {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.fluidPipe), 1));
                    }
                    if (world.getBlockState(pos).getBlock() == ModBlocks.floorCable) {
                        entity.inventory.addItemStackToInventory(new ItemStack(ItemBlock.getItemFromBlock(ModBlocks.energyCable), 1));
                    }
                    if (world.getBlockState(pos).getBlock() == ModBlocks.floorLamp) {
                        entity.inventory.addItemStackToInventory(new ItemStack(ModItems.lamp, 1));
                    }
                }
                world.setBlockState(new BlockPos(x, y, z), ModBlocks.blockIndFloor.getDefaultState(), 3);
            }
        }
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, World world, List<String> list, ITooltipFlag flag) {
        list.add("A universal tool for this mod");
    }

    private void playDrillSound(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation("industrialrenewal:drill")), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}