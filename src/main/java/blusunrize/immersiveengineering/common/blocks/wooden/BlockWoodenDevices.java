package blusunrize.immersiveengineering.common.blocks.wooden;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import blusunrize.immersiveengineering.client.render.BlockRenderWoodenDevices;
import blusunrize.immersiveengineering.common.Utils;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;

public class BlockWoodenDevices extends BlockIEBase
{
	public BlockWoodenDevices()
	{
		super("woodenDevice", Material.wood, 2, ItemBlockWoodenDevices.class, "post","watermill","windmill","scaffolding","windmillAdvanced");
		this.setHardness(2.0F);
		this.setResistance(5.0F);
	}
	@Override
	public boolean allowHammerHarvest(int meta)
	{
		return true;
	}
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i=0; i<subNames.length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//Treated wood + post, fence, watermill, windmills
		for(int i=0; i<2; i++)
		{
			icons[0][i] = iconRegister.registerIcon("immersiveengineering:treatedWood");
			icons[1][i] = iconRegister.registerIcon("immersiveengineering:treatedWood");
			icons[2][i] = iconRegister.registerIcon("immersiveengineering:treatedWood");
			icons[4][i] = iconRegister.registerIcon("immersiveengineering:treatedWood");
		}
		//Scaffolding
		icons[3][0] = iconRegister.registerIcon("immersiveengineering:scaffolding_top");
		icons[3][1] = iconRegister.registerIcon("immersiveengineering:scaffolding_side");
	}
	@Override
	public int getRenderType()
	{
		return BlockRenderWoodenDevices.renderID;
	}
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y ,int z, int side)
	{
		if(world.getBlockMetadata(x+(side==4?1:side==5?-1:0),y+(side==0?1:side==1?-1:0),z+(side==2?1:side==3?-1:0))==3)
			return (world.getBlock(x, y, z)==this&&world.getBlockMetadata(x,y,z)==4)?false:true;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityWoodenPost)
		{
			byte type = ((TileEntityWoodenPost)world.getTileEntity(x, y, z)).type;
			switch(type)
			{
			case 0:
				this.setBlockBounds(.25f,0,.25f,  .75f,1f,.75f);
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				this.setBlockBounds(type==7?0:.3125f,.5f,type==5?0:.3125f,  type==6?1:.6875f,1f,type==4?1:.6875f);
				break;
			default:
				this.setBlockBounds(isPost(world,x-1,y,z)?0:.3125f,0,isPost(world,x,y,z-1)?0:.3125f,  isPost(world,x+1,y,z)?1:.6875f,1f,isPost(world,x,y,z+1)?1:.6875f);
				break;
			}
		}
		else
			this.setBlockBounds(0,0,0,1,1,1);
	}
	boolean isPost(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlock(x,y,z)==this && world.getBlockMetadata(x, y, z)==0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityWoodenPost && Utils.isHammer(player.getCurrentEquippedItem()))
		{
			byte type = ((TileEntityWoodenPost)world.getTileEntity(x, y, z)).type;
			if(type==3)
			{
				ForgeDirection fd = ForgeDirection.getOrientation(side);
				ForgeDirection rot = fd.getRotation(ForgeDirection.UP);
				if((!world.isAirBlock(x+fd.offsetX,y+fd.offsetY,z+fd.offsetZ))
						||(world.getTileEntity(x+rot.offsetX,y+rot.offsetY,z+rot.offsetZ) instanceof TileEntityWoodenPost)
						||(world.getTileEntity(x+rot.getOpposite().offsetX,y+rot.getOpposite().offsetY,z+rot.getOpposite().offsetZ) instanceof TileEntityWoodenPost))
					return false;
				world.setBlock(x+fd.offsetX, y, z+fd.offsetZ, this, 0, 0x3);
				if(world.getTileEntity(x+fd.offsetX, y, z+fd.offsetZ) instanceof TileEntityWoodenPost)
					((TileEntityWoodenPost)world.getTileEntity(x+fd.offsetX, y, z+fd.offsetZ)).type=(byte)(2+side);
			}
			else if(type==4||type==5||type==6||type==7)
				world.setBlockToAir(x, y, z);

			return true;
		}
		if(world.getTileEntity(x, y, z) instanceof TileEntityWindmillAdvanced && Utils.getDye(player.getCurrentEquippedItem())>=0)
		{
			if(((TileEntityWindmillAdvanced)world.getTileEntity(x, y, z)).dye == Utils.getDye(player.getCurrentEquippedItem()))
				return false;
			((TileEntityWindmillAdvanced)world.getTileEntity(x, y, z)).dye = Utils.getDye(player.getCurrentEquippedItem());
			if(!player.capabilities.isCreativeMode)
				player.getCurrentEquippedItem().stackSize--;
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if(metadata==0)
			return ret;

		int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; i++)
		{
			Item item = getItemDropped(metadata, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(metadata)));
			}
		}
		return ret;
	}
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityWoodenPost)
		{
			int yy=y;
			byte type = ((TileEntityWoodenPost)world.getTileEntity(x, y, z)).type;
			switch(type)
			{
			case 4:
			case 5:
			case 6:
			case 7:
				return;
			default:
				yy-= ((TileEntityWoodenPost)world.getTileEntity(x, y, z)).type;
				break;
			}

			for(int i=0;i<=3;i++)
			{
				world.setBlockToAir(x,yy+i,z);
				if(i==3)
					for(ForgeDirection fd : new ForgeDirection[]{ForgeDirection.NORTH,ForgeDirection.SOUTH,ForgeDirection.EAST,ForgeDirection.WEST})
						if(world.getTileEntity(x+fd.offsetX,yy+i,z+fd.offsetZ) instanceof TileEntityWoodenPost && ((TileEntityWoodenPost)world.getTileEntity(x+fd.offsetX,yy+i,z+fd.offsetZ)).type>3)
							world.setBlockToAir(x+fd.offsetX,yy+i,z+fd.offsetZ);
			}
			if(type==0 && !world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots)
				world.spawnEntityInWorld(new EntityItem(world, x+.5,y+.5,z+.5, new ItemStack(this,1,0)));
		}
		if(world.getTileEntity(x, y, z) instanceof TileEntityWatermill)
		{
			int[] off = ((TileEntityWatermill)world.getTileEntity(x, y, z)).offset;
			int f = ((TileEntityWatermill)world.getTileEntity(x, y, z)).facing;
			int xx = x - ((f==2||f==3)?off[0]:0);
			int yy = y - off[1];
			int zz = z - ((f==2||f==3)?0:off[0]);

			if(!(off[0]==0&&off[1]==0) && world.isAirBlock(xx, yy, zz))
				return;
			world.setBlockToAir(xx, yy, zz);
			for(int hh=-2;hh<=2;hh++)
			{
				int r=hh<-1||hh>1?1:2;
				for(int ww=-r;ww<=r;ww++)
					world.setBlockToAir(xx+((f==2||f==3)?ww:0), yy+hh, zz+((f==2||f==3)?0:ww));
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}



	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		switch(meta)
		{
		case 0:
			return new TileEntityWoodenPost();
		case 1:
			return new TileEntityWatermill();
		case 2:
			return new TileEntityWindmill();
		case 4:
			return new TileEntityWindmillAdvanced();
		}
		return null;
	}
}