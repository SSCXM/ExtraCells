package extracells.blocks;

import java.text.DecimalFormat;

import appeng.api.me.items.IAEWrench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extracells.Extracells;
import extracells.tile.TileEntityBusFluidStorage;
import extracells.tile.TileEntityMEBattery;

public class BlockMEBattery extends BlockContainer
{

	@SideOnly(Side.CLIENT)
	public Icon iconLow;
	@SideOnly(Side.CLIENT)
	public Icon iconMed;
	@SideOnly(Side.CLIENT)
	public Icon iconHi;

	public BlockMEBattery(int id)
	{
		super(id, Material.rock);
		this.setCreativeTab(extracells.Extracells.ModTab);
		this.setUnlocalizedName("block.mebattery");
		this.setHardness(2.0F);
		this.setResistance(10.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int b)
	{
		return iconLow;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
	{
		TileEntity tileentity = blockAccess.getBlockTileEntity(x, y, z);

		if (tileentity != null)
		{
			TileEntityMEBattery meBattery = (TileEntityMEBattery) tileentity;

			Double energy = meBattery.getEnergy();
			Double maxEnergy = meBattery.getMaxEnergy();

			if (energy < (maxEnergy * 0.25))
			{
				return iconLow;
			} else if (energy >= (maxEnergy * 0.25) && energy <= (maxEnergy * 0.75))
			{
				return iconMed;
			} else
			{
				return iconHi;
			}
		}
		return iconLow;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconregister)
	{
		iconLow = iconregister.registerIcon("extracells:mebattery.low");
		iconMed = iconregister.registerIcon("extracells:mebattery.medium");
		iconHi = iconregister.registerIcon("extracells:mebattery.high");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityMEBattery();
	}

	@Override
	public boolean hasTileEntity()
	{
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourID)
	{
		if (!world.isRemote)
		{
			((TileEntityMEBattery) world.getBlockTileEntity(x, y, z)).onNeighborBlockChange(world, x, y, z, neighbourID);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ)
	{
		if (!world.isRemote)
		{
			if (world.getBlockTileEntity(x, y, z) == null || player.isSneaking())
			{
				return false;
			}
			player.openGui(Extracells.instance, 5, world, x, y, z);
		}
		return true;
	}
}
