package draco18s.artifacts; 

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.WeightedRandomArtifact;
import draco18s.artifacts.arrowtrapbehaviors.DispenserBehaviors;
import draco18s.artifacts.block.*;
import draco18s.artifacts.client.*;
import draco18s.artifacts.entity.*;
import draco18s.artifacts.factory.*;
import draco18s.artifacts.item.*;
import draco18s.artifacts.network.PacketHandlerClient;
import draco18s.artifacts.network.PacketHandlerServer;
import draco18s.artifacts.worldgen.PlaceTraps;

@Mod(modid = "Artifacts", name = "Unique Artifacts", version = "0.12.6")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
	clientPacketHandlerSpec = @SidedPacketHandler(channels = {"Artifacts"}, packetHandler = PacketHandlerClient.class),
	serverPacketHandlerSpec = @SidedPacketHandler(channels = {"Artifacts"}, packetHandler = PacketHandlerServer.class))
public class DragonArtifacts {
	@Instance("Artifacts")
    public static DragonArtifacts instance;
	public static boolean renderNamesInPedestals = false;
	public static boolean renderInvis = false;
	public static boolean boundingInvis = true;
    public static PlaceTraps worldGen;
    
    @SidedProxy(clientSide = "draco18s.artifacts.client.ClientProxy", serverSide = "draco18s.artifacts.CommonProxy")
    public static CommonProxy proxy;
    
    public static CreativeTabs tabTraps = new CreativeTabs("tabTraps") {
        public ItemStack getIconItemStack() {
                return new ItemStack(Block.dispenser, 1, 0);
        }
	};
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
			config.load();
			int armorIDh1 = config.getItem("artifact_helm1", 3980).getInt();
			int armorIDh2 = config.getItem("artifact_helm2", 3981).getInt();
			int armorIDh3 = config.getItem("artifact_helm3", 3982).getInt();
			int armorIDh4 = config.getItem("artifact_helm4", 3983).getInt();
			int armorIDh5 = config.getItem("artifact_helm5", 3984).getInt();
			
			int armorIDc1 = config.getItem("artifact_chest1", 3985).getInt();
			int armorIDc2 = config.getItem("artifact_chest2", 3986).getInt();
			int armorIDc3 = config.getItem("artifact_chest3", 3987).getInt();
			int armorIDc4 = config.getItem("artifact_chest4", 3988).getInt();
			int armorIDc5 = config.getItem("artifact_chest5", 3989).getInt();
			
			int armorIDl1 = config.getItem("artifact_legs1", 3990).getInt();
			int armorIDl2 = config.getItem("artifact_legs2", 3991).getInt();
			int armorIDl3 = config.getItem("artifact_legs3", 3992).getInt();
			int armorIDl4 = config.getItem("artifact_legs4", 3993).getInt();
			int armorIDl5 = config.getItem("artifact_legs5", 3994).getInt();
			
			int armorIDb1 = config.getItem("artifact_boots1", 3995).getInt();
			int armorIDb2 = config.getItem("artifact_boots2", 3996).getInt();
			int armorIDb3 = config.getItem("artifact_boots3", 3997).getInt();
			int armorIDb4 = config.getItem("artifact_boots4", 3998).getInt();
			int armorIDb5 = config.getItem("artifact_boots5", 3999).getInt();
			int artifactID = config.getItem("artifact", 4000).getInt();
			int tb1 = config.getItem("trapblade_wood", 4001).getInt();
			int tb2 = config.getItem("trapblade_stone", 4002).getInt();
			int tb3 = config.getItem("trapblade_iron", 4003).getInt();
			int tb4 = config.getItem("trapblade_gold", 4004).getInt();
			int tb5 = config.getItem("trapblade_diamond", 4005).getInt();
			int lightID = config.getBlock("lightblock", 4000).getInt();
			int pedID = config.getBlock("pedestal", 4001).getInt();
    		int orichalcumID = config.getItem("orichalcumDust", 4020).getInt();
    		int calendarID = config.getItem("calendar", 4021).getInt();
			
			ConfigCategory worldGenConf = config.getCategory("worldgen");
			worldGenConf.setComment("By default this mod alters worldgen slightly, adding more and different traps to\npyramids, temples, and strongholds as well as quicksand 'lakes'.\nThese may be disabled individually.");
			boolean pyrm = config.get("WorldGen","Pyramids",true).getBoolean(true);
			boolean temp = config.get("WorldGen","Temples",true).getBoolean(true);
			boolean strn = config.get("WorldGen","Strongholds",true).getBoolean(true);
			boolean quik = config.get("WorldGen","QuicksandPits",true).getBoolean(true);
			boolean towers = config.get("WorldGen","WizardTowers",true).getBoolean(true);
			Property conf = config.get("WorldGen","dimensionWhitelistEnable",false);
			conf.comment = "Enables the whitelist for worldgen.  If enabled, world gen objects will only generate in whitelisted dimensions.";
			boolean usewhite = conf.getBoolean(false);
			conf = config.get("WorldGen","dimensionBlacklistEnable",false);
			conf.comment = "Enables the blacklist for worldgen.  If enabled, world gen objects will never generate in blacklisted dimensions.\nBlacklist will override whitelist.  -1 and 1 (Nether and End) are always blacklisted.";
			boolean useblack = conf.getBoolean(false);
			
			Property cw = config.get("WorldGen","dimensionWhitelistList", new int[] {0});
			Property cb = config.get("WorldGen","dimensionBlacklistList", new int[] {-1,1});
			int[] white = cw.getIntList();
			int[] black = cb.getIntList();
			
			Arrays.sort(white);
			Arrays.sort(black);
			
			String a=Arrays.toString(white);
			String whitestring[]=a.substring(1,a.length()-1).split(", ");
			String b=Arrays.toString(black);
			String blackstring[]=b.substring(1,b.length()-1).split(", ");

			cw.set(whitestring);
			cb.set(blackstring);
			
    		boolean useAntibuild = config.get("WorldGen","UseAntibuilders",true).getBoolean(true);
			
			ConfigCategory longNames = config.getCategory("general");
			longNames.setComment("These settings dictate how item names are displayed.");
			Property enchName = config.get("general","Enchantments",true);
			Property matName = config.get("general","Material",true);
			Property adjName = config.get("general","Adjectives",true);
			Property renderNames = config.get("rendering","RenderNames",false);
			renderNames.comment = "Set to false to disable rendering of item names on display pedesals";
			renderNamesInPedestals = renderNames.getBoolean(true);
			
			ConfigCategory renderConf = config.getCategory("rendering");
			renderConf.setComment("Determines some options on invisible blocks");
			conf = config.get("rendering", "RenderInvis", false);
    		conf.comment = "Set this to true to render invisible blocks.  Even when false, they will render in your inventory.";
    		renderInvis = conf.getBoolean(false);
    		
    		conf = config.get("rendering", "BoundInvis", true);
    		conf.comment = "Set this to false to disable bounding boxes on invisible blocks.\nALERT: without bounding boxes you will not be able to destroy them!\nThis is only recommended for playing adventure maps.";
    		boundingInvis = conf.getBoolean(true);

    		conf = config.get("rendering", "TrapSwordPackage", "artifacts");
    		conf.comment = "Sets the package the icons should be pulled from.\nDefault is 'artifacts' which pulls the default icons.\nNot sure where this points otherwise.";
    		String bladePackage = conf.getString();
    		conf = config.get("rendering", "TrapSwordIcon", "blade");
    		conf.comment = "Sets the rendering type for swords in arrow traps.\nDefault is 'blade' which attempts to maintain the jaggy nature of the vanilla sword.\n'blade_alt' uses a smaller texture, maintaining strait lines and mirroring the vanilla item as closely as possible.\nAdditional textures can be created and set here as well, if desired, without replacing existing textures.";
    		String bladeRender = conf.getString();
    		
    		int invis2ID = config.getBlock("InvisBedrock", 4002).getInt();
    		int spikesID = config.getBlock("SpikeBlock", 4003).getInt();
    		int arrowSlotID = config.getBlock("ArrowTrap", 4004).getInt();
    		int dropperID = config.getBlock("DeadDrop", 4005).getInt();
    		int coverplateID = config.getBlock("CoverPlate", 4006).getInt();
    		int quickID = config.getBlock("Quicksand", 4007).getInt();
    		int pseudoATID = config.getBlock("ArrowTrapItemBlock", 4008).getInt();
    		int pseudoCPID = config.getBlock("CoverPlateItemBlock", 4013).getInt();
    		int pseudoFBID = config.getBlock("FakeBlockItemBlock", 4014).getInt();
    		int wallplateID = config.getBlock("WallPressurePlate", 4009).getInt();
    		int owallplateID = config.getBlock("ObsWallPressurePlate", 4010).getInt();
    		int invisppID = config.getBlock("InvisPressurePlate", 4011).getInt();
    		int oinvisppID = config.getBlock("ObsInvisPressurePlate", 4012).getInt();
    		int fakeID = config.getBlock("FakeBlock", 4013).getInt();
    		int invisID = config.getBlock("InvisBlock", 4014).getInt();
    		int teSwordID = config.getBlock("FakeTileEntity", 4015).getInt();
    		int floatID = config.getBlock("FloatingBlock", 4016).getInt();
    		int antiID = config.getBlock("Antibuilder", 4017).getInt();
    		int ignoreID = config.getBlock("Antianti", 4018).getInt();
    		int laserSourceID = config.getBlock("LaserSource", 4021).getInt();
    		int laserBeamID = config.getBlock("LaserBeam", 4022).getInt();
    		
    		ConfigCategory spawnConf = config.getCategory("spawning");
    		spawnConf.setComment("These settings alter the spawning rarity of artifacts in the various chests.\nLower is rarer, higher is more common.  By default pyramids and temples generate ~2 total.\nCross-Mod Treasure String ('ProceeduralGeneration') is for inter-mod treasure gen.");
    		int dungRare = config.get("spawning","Dungeons",0).getInt(0);
    		int pyrRare = config.get("spawning","Pyramids",4).getInt(4);
    		int tempRare = config.get("spawning","Temples",8).getInt(8);
    		int strong1Rare = config.get("spawning","Stronghold_Library",6).getInt(6);
    		int strong2Rare = config.get("spawning","Stronghold_Corridor",1).getInt(1);
    		int strong3Rare = config.get("spawning","Stronghold_Crossing",3).getInt(3);
    		int mineRare = config.get("spawning","Mineshafts",0).getInt(0);
    		int villRare = config.get("spawning","Blacksmith",1).getInt(1);
    		int wizRare = config.get("spawning","WizTowers",10).getInt(10);
    		int procRare = config.get("spawning","crossModTreasureString_ProceeduralGeneration",5).getInt(5);
		config.save();
        ArtifactsAPI.artifacts = new FactoryArtifact();
        ArtifactsAPI.itemicons = new FactoryItemIcons();
        ArtifactsAPI.traps = new FactoryTrapBehaviors();
		ItemArtifact.instance = new ItemArtifact(artifactID);

		ArtifactsAPI.artifacts.registerUpdateNBTKey("orePingDelay");
		ArtifactsAPI.artifacts.registerUpdateNBTKey("resCooldown");
		ArtifactsAPI.artifacts.registerUpdateNBTKey("medkitDelay");
		ArtifactsAPI.artifacts.registerUpdateNBTKey("adrenDelay");
		
		ItemArtifactArmor.hcloth = new ItemArtifactArmor(armorIDh1, EnumArmorMaterial.CLOTH, 0, 2, 0);
		ItemArtifactArmor.hchain = new ItemArtifactArmor(armorIDh2, EnumArmorMaterial.CHAIN, 1, 2, 0);
		ItemArtifactArmor.hiron = new ItemArtifactArmor(armorIDh3, EnumArmorMaterial.IRON, 2, 2, 0);
		ItemArtifactArmor.hgold = new ItemArtifactArmor(armorIDh4, EnumArmorMaterial.GOLD, 4, 2, 0);
		ItemArtifactArmor.hdiamond = new ItemArtifactArmor(armorIDh5, EnumArmorMaterial.DIAMOND, 3, 2, 0);
		ItemArtifactArmor.ccloth = new ItemArtifactArmor(armorIDc1, EnumArmorMaterial.CLOTH, 0, 3, 1);
		ItemArtifactArmor.cchain = new ItemArtifactArmor(armorIDc2, EnumArmorMaterial.CHAIN, 1, 3, 1);
		ItemArtifactArmor.ciron = new ItemArtifactArmor(armorIDc3, EnumArmorMaterial.IRON, 2, 3, 1);
		ItemArtifactArmor.cgold = new ItemArtifactArmor(armorIDc4, EnumArmorMaterial.GOLD, 4, 3, 1);
		ItemArtifactArmor.cdiamond = new ItemArtifactArmor(armorIDc5, EnumArmorMaterial.DIAMOND, 3, 3, 1);
		ItemArtifactArmor.lcloth = new ItemArtifactArmor(armorIDl1, EnumArmorMaterial.CLOTH, 0, 4, 2);
		ItemArtifactArmor.lchain = new ItemArtifactArmor(armorIDl2, EnumArmorMaterial.CHAIN, 1, 4, 2);
		ItemArtifactArmor.liron = new ItemArtifactArmor(armorIDl3, EnumArmorMaterial.IRON, 2, 4, 2);
		ItemArtifactArmor.lgold = new ItemArtifactArmor(armorIDl4, EnumArmorMaterial.GOLD, 4, 4, 2);
		ItemArtifactArmor.ldiamond = new ItemArtifactArmor(armorIDl5, EnumArmorMaterial.DIAMOND, 3, 4, 2);
		ItemArtifactArmor.bcloth = new ItemArtifactArmor(armorIDb1, EnumArmorMaterial.CLOTH, 0, 5, 3);
		ItemArtifactArmor.bchain = new ItemArtifactArmor(armorIDb2, EnumArmorMaterial.CHAIN, 1, 5, 3);
		ItemArtifactArmor.biron = new ItemArtifactArmor(armorIDb3, EnumArmorMaterial.IRON, 2, 5, 3);
		ItemArtifactArmor.bgold = new ItemArtifactArmor(armorIDb4, EnumArmorMaterial.GOLD, 4, 5, 3);
		ItemArtifactArmor.bdiamond = new ItemArtifactArmor(armorIDb5, EnumArmorMaterial.DIAMOND, 3, 5, 3);
		ItemArtifactArmor.setupArrays();
		ItemFakeSwordRenderable.wood = new ItemFakeSwordRenderable(tb1, EnumToolMaterial.WOOD, bladePackage+":wood_"+bladeRender);
		ItemFakeSwordRenderable.stone = new ItemFakeSwordRenderable(tb2, EnumToolMaterial.STONE, bladePackage+":stone_"+bladeRender);
		ItemFakeSwordRenderable.iron = new ItemFakeSwordRenderable(tb3, EnumToolMaterial.IRON, bladePackage+":iron_"+bladeRender);
		ItemFakeSwordRenderable.gold = new ItemFakeSwordRenderable(tb4, EnumToolMaterial.GOLD, bladePackage+":gold_"+bladeRender);
		ItemFakeSwordRenderable.diamond = new ItemFakeSwordRenderable(tb5, EnumToolMaterial.EMERALD, bladePackage+":diamond_"+bladeRender);
		ItemArtifactArmor.doEnchName = ItemArtifact.doEnchName = enchName.getBoolean(true);
		ItemArtifactArmor.doMatName = ItemArtifact.doMatName = matName.getBoolean(true);
		ItemArtifactArmor.doAdjName = ItemArtifact.doAdjName = adjName.getBoolean(true);
		
		ItemOrichalcumDust.instance = new ItemOrichalcumDust(orichalcumID);
		ItemCalendar.instance = new ItemCalendar(calendarID);
		
		BlockLight.instance = new BlockLight(lightID);
		BlockPedestal.instance = new BlockPedestal(pedID);
		BlockIllusionary.instance = new BlockIllusionary(fakeID);
		BlockInvisibleBlock.instance = new BlockInvisibleBlock(invisID);
		BlockInvisibleBedrock.instance = new BlockInvisibleBedrock(invis2ID);
		BlockInvisiblePressurePlate.instance = new BlockInvisiblePressurePlate(invisppID, "Invisible Pressure Plate", Material.rock, EnumMobType.mobs).setUnlocalizedName("Invisible Pressure Plate");
		BlockInvisiblePressurePlate.obsidian = new BlockInvisiblePressurePlate(oinvisppID, "Invisible Obsidiplate", Material.rock, EnumMobType.players).setUnlocalizedName("Invisible Obsidiplate");
		BlockSpikes.instance = new BlockSpikes(spikesID);
		BlockTrap.instance = new BlockTrap(arrowSlotID);
		BlockCoverPlate.instance = new BlockCoverPlate(coverplateID);
		BlockQuickSand.instance = new BlockQuickSand(quickID);
		BlockWallPlate.instance = new BlockWallPlate(wallplateID, "Wallplate", Material.circuits, EnumMobType.mobs).setUnlocalizedName("Wallplate");
		BlockWallPlate.obsidian = new BlockWallPlate(owallplateID, "Wall Obsidiplate", Material.circuits, EnumMobType.players).setUnlocalizedName("Wall Obsidiplate");
		PseudoBlockTrap.instance = new PseudoBlockTrap(pseudoATID);
		PseudoCoverplate.instance = new PseudoCoverplate(pseudoCPID);
		PseudoBlockIllusionary.instance = new PseudoBlockIllusionary(pseudoFBID);
		BlockSword.instance = new BlockSword(teSwordID);
		BlockSolidAir.instance = new BlockSolidAir(floatID);
		BlockAntibuilder.instance = new BlockAntibuilder(antiID);
		BlockStoneBrickMovable.instance = new BlockStoneBrickMovable(ignoreID);
		BlockLaserBeamSource.instance = new BlockLaserBeamSource(laserSourceID);
		BlockLaserBeam.instance = new BlockLaserBeam(laserBeamID);
		
		GameRegistry.registerBlock(BlockWallPlate.instance, "Wallplate");
		GameRegistry.registerBlock(BlockWallPlate.obsidian, "Wall Obsidiplate");
        GameRegistry.registerBlock(BlockPedestal.instance, "Display Pedestal");
		GameRegistry.registerBlock(BlockIllusionary.instance, "Illusionary Block");
		GameRegistry.registerBlock(BlockInvisibleBlock.instance, "Invisible Block");
		GameRegistry.registerBlock(BlockInvisibleBedrock.instance, "Invisible Bedrock");
		GameRegistry.registerBlock(BlockInvisiblePressurePlate.instance, "Invisible Pressure Plate");
		GameRegistry.registerBlock(BlockInvisiblePressurePlate.obsidian, "Invisible Obsidiplate");
		GameRegistry.registerBlock(BlockSpikes.instance, "Upright Spikes");
		GameRegistry.registerBlock(BlockTrap.instance, "Arrow Trap");
		GameRegistry.registerBlock(BlockCoverPlate.instance, "Cover Plate");
		GameRegistry.registerBlock(BlockQuickSand.instance, "Quicksand");
		GameRegistry.registerBlock(BlockSolidAir.instance, "Floating Block");
		GameRegistry.registerBlock(BlockAntibuilder.instance, "Anti-Builder");
		GameRegistry.registerBlock(BlockStoneBrickMovable.instance, "Anti Anti-Builder Stone");
		GameRegistry.registerBlock(BlockLaserBeamSource.instance, "Laser Beam Source");
		GameRegistry.registerBlock(BlockLaserBeam.instance, "Laser Beam");
        
        GameRegistry.registerTileEntity(TileEntityDisplayPedestal.class, "artifacts.pedestal");
		GameRegistry.registerTileEntity(TileEntitySword.class, "artifacts.tesword");
		GameRegistry.registerTileEntity(TileEntityTrap.class, "artifacts.arrowtrap");
		GameRegistry.registerTileEntity(EntitySpikes.class, "artifacts.spiketrap");
		GameRegistry.registerTileEntity(TileEntityAntibuilder.class, "artifacts.antibuilder");
        EntityRegistry.registerModEntity(EntityClayGolem.class, "EntClayGolem", 0, this, 350, 5, false);
        EntityRegistry.registerModEntity(EntitySpecialArrow.class, "SpecialArrow", 1, this, 64, 20, true);
        EntityList.addMapping(EntityClayGolem.class, "Clay Golem", 3, 13347172, 7033635);//13347172 is pale
		
        worldGen = new PlaceTraps(pyrm, temp, strn, quik, towers, usewhite, useblack, white, black, useAntibuild);
        GameRegistry.registerWorldGenerator(worldGen);
        
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, Math.max(6, wizRare)));
        //ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, 2));//would a second, rarer chance, be any different than a single large chance?
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(Item.enchantedBook.itemID, 0, 1, 1, 5));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(Item.diamond.itemID, 0, 2, 5, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(Item.goldNugget.itemID, 0, 3, 7, 5));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 0, 1, 1, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 1, 1, 1, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 2, 1, 1, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 3, 1, 1, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 4, 1, 1, 3));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(Item.expBottle.itemID, 0, 1, 1, 2));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomChestContent(ItemOrichalcumDust.instance.itemID, 0, 4, 12, 2));
        
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hcloth.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.ccloth.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.lcloth.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.bcloth.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hchain.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.cchain.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.lchain.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.bchain.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hiron.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.ciron.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.liron.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.biron.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hgold.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.cgold.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.lgold.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.bgold.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hdiamond.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.cdiamond.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.ldiamond.itemID, 0, 1, 1, procRare));
        ChestGenHooks.getInfo("ProceeduralGeneration").addItem(new WeightedRandomArtifact(ItemArtifactArmor.bdiamond.itemID, 0, 1, 1, procRare));
        
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, pyrRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, tempRare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, strong1Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, strong2Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, strong3Rare));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, villRare));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, mineRare));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomArtifact(ItemArtifact.instance.itemID, 0, 1, 1, dungRare));

        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifactArmor.hcloth.itemID, 0, 1, 1, Math.max(6, wizRare)));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifactArmor.ccloth.itemID, 0, 1, 1, Math.max(6, wizRare)));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifactArmor.lcloth.itemID, 0, 1, 1, Math.max(6, wizRare)));
        ChestGenHooks.getInfo("A_WIZARD_DID_IT").addItem(new WeightedRandomArtifact(ItemArtifactArmor.bcloth.itemID, 0, 1, 1, Math.max(6, wizRare)));

        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hchain.itemID, 0, 1, 1, dungRare));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cchain.itemID, 0, 1, 1, dungRare));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.lchain.itemID, 0, 1, 1, dungRare));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.bchain.itemID, 0, 1, 1, dungRare));

        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hiron.itemID, 0, 1, 1, villRare));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomArtifact(ItemArtifactArmor.ciron.itemID, 0, 1, 1, villRare));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomArtifact(ItemArtifactArmor.liron.itemID, 0, 1, 1, villRare));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomArtifact(ItemArtifactArmor.biron.itemID, 0, 1, 1, villRare));

        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hgold.itemID, 0, 1, 1, pyrRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cgold.itemID, 0, 1, 1, pyrRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.lgold.itemID, 0, 1, 1, pyrRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.bgold.itemID, 0, 1, 1, pyrRare));

        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hdiamond.itemID, 0, 1, 1, strong2Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cdiamond.itemID, 0, 1, 1, strong2Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomArtifact(ItemArtifactArmor.ldiamond.itemID, 0, 1, 1, strong2Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomArtifact(ItemArtifactArmor.bdiamond.itemID, 0, 1, 1, strong2Rare));

        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hdiamond.itemID, 0, 1, 1, strong3Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cdiamond.itemID, 0, 1, 1, strong3Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.ldiamond.itemID, 0, 1, 1, strong3Rare));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.bdiamond.itemID, 0, 1, 1, strong3Rare));

        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.ccloth.itemID, 0, 1, 1, mineRare));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hiron.itemID, 0, 1, 1, mineRare));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomArtifact(ItemArtifactArmor.hchain.itemID, 0, 1, 1, mineRare));

        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cdiamond.itemID, 0, 1, 1, tempRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.cgold.itemID, 0, 1, 1, tempRare));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomArtifact(ItemArtifactArmor.ciron.itemID, 0, 1, 1, tempRare));
        
        ItemStack thinglass = new ItemStack(Block.thinGlass);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockPedestal.instance,2), "ggg","g g","sss",'g', thinglass, 's', "stone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockSpikes.instance, 2), "i i","sss", 'i', "ingotIron", 's', new ItemStack(Block.stoneSingleSlab)));
		GameRegistry.addShapedRecipe(new ItemStack(BlockSpikes.instance, 2), "i i","sss", 'i', new ItemStack(Item.ingotIron), 's', new ItemStack(Block.stoneSingleSlab));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockTrap.instance), new ItemStack(Item.painting), new ItemStack(Block.dispenser));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockWallPlate.instance, 2), "s", "s", "s", 's', "stone"));
		GameRegistry.addShapedRecipe(new ItemStack(BlockWallPlate.obsidian, 2), "s", "s", "s", 's', new ItemStack(Block.obsidian));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 1), "logWood", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 2), "cobblestone", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));
		//These oredict recipes don't work with vanilla items
		/*GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 1, 2), "ingotIron", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 1, 3), "gemDiamond", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 1, 4), "ingotGold", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemOrichalcumDust.instance, 1, 5), "leather", new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget));*/
		GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 6), new ItemStack(Item.leather), new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 3), new ItemStack(Item.ingotIron), new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 5), new ItemStack(Item.ingotGold), new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 2, 4), new ItemStack(Item.diamond), new ItemStack(ItemOrichalcumDust.instance, 1, 0), Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifact.instance));
		
		for(int i = 0; i < 4; ++i) {
			GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifactArmor.clothArray[i]));
			GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifactArmor.chainArray[i]));
			GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifactArmor.ironArray[i]));
			GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifactArmor.goldArray[i]));
			GameRegistry.addShapelessRecipe(new ItemStack(ItemOrichalcumDust.instance, 4, 0), new ItemStack(ItemArtifactArmor.diamondArray[i]));
		}
		
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 2), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 3), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 4), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 5), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 6), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 7), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addShapelessRecipe(new ItemStack(BlockQuickSand.instance, 8), new ItemStack(Item.potion, 1, 8204), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt), new ItemStack(Block.dirt));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLaserBeamSource.instance), "sss", "rog", "sss", 'o', new ItemStack(ItemOrichalcumDust.instance, 4, 0), 'g', thinglass, 's', "stone", 'r', new ItemStack(Item.redstone)));
		GameRegistry.addShapedRecipe(new ItemStack(ItemCalendar.instance), "ppp","pcp","ppp", 'p', new ItemStack(Item.paper), 'c', new ItemStack(Item.pocketSundial));
		
		MinecraftForge.setToolClass(ItemArtifact.instance, "pickaxe", 3);
		DamageSourceSword.instance = new DamageSourceSword("trapsword");
    }
	
	@EventHandler
    public void load(FMLInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new ArtifactEventHandler());
    }
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.registerRenders();
		DispenserBehaviors.registerBehaviors();
		ArtifactTickHandler tickHandler = new ArtifactTickHandler();
	    TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
	}
}
