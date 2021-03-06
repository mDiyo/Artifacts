package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentOreRadar implements IArtifactComponent {
	//Random rand;
	private static ArrayList<ItemStack> oreBlocks = new ArrayList<ItemStack>();
	private ArrayList<Vec3> detectedBlocks = new ArrayList<Vec3>();
	
	public ComponentOreRadar() {
		//rand = new Random();
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		return "";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(!itemStack.stackTagCompound.hasKey("orePingDelay_armor")) {
			itemStack.stackTagCompound.setInteger("orePingDelay_armor", 0);
		}
		if(itemStack.stackTagCompound.getInteger("orePingDelay_armor") > 0) {
			//System.out.println(itemStack.stackTagCompound.getInteger("orePingDelay"));
			return;
		}
		itemStack.stackTagCompound.setInteger("orePingDelay_armor", 9);
		int x = (int)player.posX;
		int y = (int)player.posY;
		int z = (int)player.posZ;
		//+/- 4
		boolean found = false;
		for(int yy = y - 2; yy <= y + 3; ++yy) {
			for(int xx = x - 4; xx <= x + 4; ++xx) {
				for(int zz = z - 4; zz <= z + 4; ++zz) {
					boolean already = false;
					for(int b = detectedBlocks.size()-1; b >= 0; b--) {
						Vec3 s = detectedBlocks.get(b);
						if((int)s.xCoord == xx && (int)s.yCoord == yy && (int)s.zCoord == zz) {
							already = true;
						}
					}
					if(!already) {
						int id = world.getBlockId(xx, yy,zz);
						Block block = Block.blocksList[id];
						if(block == Block.oreDiamond || block == Block.oreIron || block == Block.oreCoal || block == Block.oreRedstone || block == Block.oreRedstoneGlowing || block == Block.oreEmerald || block == Block.oreGold || block == Block.oreLapis || block == Block.oreNetherQuartz){
							ByteArrayOutputStream bt = new ByteArrayOutputStream();
							DataOutputStream out = new DataOutputStream(bt);
							try
							{
								out.writeInt(23);
								out.writeInt(xx);
								out.writeInt(yy);
								out.writeInt(zz);
								Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
								Player p = (Player)player;
								PacketDispatcher.sendPacketToPlayer(packet, p);
							}
							catch (IOException ex)
							{
								System.out.println("couldnt send packet!");
							}
							Vec3 s = Vec3.createVectorHelper(xx, yy, zz);
							detectedBlocks.add(s);
							found = true;
							xx = x + 10;
							zz = z + 10;
							yy = y + 10;
						}
						else {
							String oreName;
							for(int a=ComponentOreRadar.oreBlocks.size()-1; a >= 0; a--) {
								if(id == ComponentOreRadar.oreBlocks.get(a).itemID) {
									ByteArrayOutputStream bt = new ByteArrayOutputStream();
									DataOutputStream out = new DataOutputStream(bt);
									try
									{
										out.writeInt(23);
										out.writeInt(xx);
										out.writeInt(yy);
										out.writeInt(zz);
										Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
										Player p = (Player)player;
										PacketDispatcher.sendPacketToPlayer(packet, p);
									}
									catch (IOException ex)
									{
										System.out.println("couldnt send packet!");
									}
									Vec3 s = Vec3.createVectorHelper(xx, yy, zz);
									detectedBlocks.add(s);
									found = true;
									xx = x + 5;
									zz = z + 5;
									yy = y + 5;
	                			}
							}
						}
					}
				}				
			}
		}
		if(!found) {
			for(int b = detectedBlocks.size()-1; b >= 0; b--) {
				detectedBlocks.remove(b);
			}
		}
		/**/
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Senses nearby ores"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Senses nearby ores") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Sensitive";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Finding";
	}

	@Override
	public int getTextureBitflags() {
		return 2304;
	}

	@Override
	public int getNegTextureBitflags() {
		return 4863;
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {
		
	}

	public static void addOre(ItemStack ore) {
		oreBlocks.add(ore);
	}
}
