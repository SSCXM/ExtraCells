package extracells.tile;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import appeng.api.Util;
import appeng.api.events.GridPatternUpdateEvent;
import appeng.api.me.tiles.IGridTileEntity;
import appeng.api.me.util.ICraftingPattern;

public class ECPrivatePatternInventory extends ECPrivateInventory
{
	IGridTileEntity gridTE;

	public ECPrivatePatternInventory(List<ItemStack> slots, String customName, int stackLimit, IGridTileEntity gridTE)
	{
		super(slots, customName, stackLimit);
		this.gridTE = gridTE;
	}

	@Override
	public void onInventoryChanged()
	{
		if (gridTE.getGrid() != null)
		{
			MinecraftForge.EVENT_BUS.post(new GridPatternUpdateEvent(gridTE.getWorld(), gridTE.getLocation(), gridTE.getGrid()));
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		ICraftingPattern currentPattern = Util.getAssemblerPattern(itemstack);
		if (currentPattern == null)
			return false;
		for (ItemStack entry : currentPattern.getRequirements())
		{
			if (entry != null && entry.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.isFilledContainer(entry))
				return true;
		}
		return false;
	}
}