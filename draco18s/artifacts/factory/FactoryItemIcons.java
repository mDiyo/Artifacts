package draco18s.artifacts.factory;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import draco18s.artifacts.api.IItemIconAPI;
import draco18s.artifacts.client.RadarParticle;

public class FactoryItemIcons implements IItemIconAPI {
	public int numberAmulets = 0;
	public int numberDaggers = 0;
	public int numberFigurines = 0;
	public int numberRings = 0;
	public int numberStaffs = 0;
	public int numberSwords = 0;
	public int numberTrinkets = 0;
	public int numberWands = 0;
	public int numberBoots = 0;
	public int numberChestplates = 0;
	public int numberHelms = 0;
	public int numberLeggings = 0;
	private final ArrayList<AbstractIcon> iconList = new ArrayList<AbstractIcon>();
	
	public FactoryItemIcons() {
		System.out.println("Instancing default icon list");
		try {
	        registerArtifactIcon("amulet","artifacts:amulet1","artifacts:amulet1_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet2","artifacts:amulet2_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet3","artifacts:amulet3_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet4","artifacts:amulet4_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet5","artifacts:amulet5_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet6","artifacts:amulet6_overlay");
	        registerArtifactIcon("amulet","artifacts:amulet7","artifacts:amulet7_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger1","artifacts:dagger1_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger2","artifacts:dagger2_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger3","artifacts:dagger3_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger4","artifacts:dagger4_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger5","artifacts:dagger5_overlay");
	        registerArtifactIcon("dagger","artifacts:dagger6","artifacts:dagger6_overlay");
	        registerArtifactIcon("figurine","artifacts:figurine1");
	        registerArtifactIcon("figurine","artifacts:figurine2","artifacts:figurine2_overlay");
	        registerArtifactIcon("figurine","artifacts:figurine3");
	        registerArtifactIcon("ring","artifacts:ring1","artifacts:ring1_overlay");
	        registerArtifactIcon("ring","artifacts:ring2","artifacts:ring2_overlay");
	        registerArtifactIcon("ring","artifacts:ring3","artifacts:ring3_overlay");
	        registerArtifactIcon("ring","artifacts:ring4","artifacts:ring4_overlay");
	        registerArtifactIcon("ring","artifacts:ring5","artifacts:ring5_overlay");
	        registerArtifactIcon("ring","artifacts:ring6","artifacts:ring6_overlay");
	        registerArtifactIcon("ring","artifacts:ring7");
	        registerArtifactIcon("ring","artifacts:ring8","artifacts:ring8_overlay");
	        registerArtifactIcon("staff","artifacts:staff1","artifacts:staff1_overlay");
	        registerArtifactIcon("staff","artifacts:staff2","artifacts:staff2_overlay");
	        registerArtifactIcon("staff","artifacts:staff3");
	        registerArtifactIcon("staff","artifacts:staff4","artifacts:staff4_overlay");
	        registerArtifactIcon("staff","artifacts:staff5");
	        registerArtifactIcon("staff","artifacts:staff6","artifacts:staff6_overlay");
	        registerArtifactIcon("sword","artifacts:sword1","artifacts:sword1_overlay");
	        registerArtifactIcon("sword","artifacts:sword2","artifacts:sword2_overlay");
	        registerArtifactIcon("sword","artifacts:sword3","artifacts:sword3_overlay");
	        registerArtifactIcon("sword","artifacts:sword4","artifacts:sword4_overlay");
	        registerArtifactIcon("sword","artifacts:sword5","artifacts:sword5_overlay");
	        registerArtifactIcon("sword","artifacts:sword6");
	        registerArtifactIcon("sword","artifacts:sword7","artifacts:sword7_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket1","artifacts:trinket1_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket2","artifacts:trinket2_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket3");
	        registerArtifactIcon("trinket","artifacts:trinket4");
	        registerArtifactIcon("trinket","artifacts:trinket5");
	        registerArtifactIcon("trinket","artifacts:trinket6","artifacts:trinket6_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket7","artifacts:trinket7_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket8","artifacts:trinket8_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket9","artifacts:trinket9_overlay");
	        registerArtifactIcon("trinket","artifacts:trinket10");
	        registerArtifactIcon("wand","artifacts:wand1","artifacts:wand1_overlay");
	        registerArtifactIcon("wand","artifacts:wand2","artifacts:wand2_overlay");
	        registerArtifactIcon("wand","artifacts:wand3","artifacts:wand3_overlay");
	        registerArtifactIcon("wand","artifacts:wand4","artifacts:wand4_overlay");
	        registerArtifactIcon("wand","artifacts:wand5","artifacts:wand5_overlay");
	        registerArtifactIcon("wand","artifacts:wand6","artifacts:wand6_overlay");
	        registerArtifactIcon("wand","artifacts:wand7","artifacts:wand7_overlay");

	        registerArtifactIcon("boots","artifacts:boots1","artifacts:boots1_overlay");
	        registerArtifactIcon("boots","artifacts:boots2","artifacts:boots2_overlay");
	        registerArtifactIcon("boots","artifacts:boots3","artifacts:boots3_overlay");
	        registerArtifactIcon("boots","artifacts:boots4","artifacts:boots4_overlay");
	        registerArtifactIcon("boots","artifacts:boots5","artifacts:boots5_overlay");
	        registerArtifactIcon("chestplate","artifacts:chestplate1","artifacts:chestplate1_overlay");
	        registerArtifactIcon("chestplate","artifacts:chestplate2","artifacts:chestplate2_overlay");
	        registerArtifactIcon("chestplate","artifacts:chestplate3","artifacts:chestplate3_overlay");
	        registerArtifactIcon("chestplate","artifacts:chestplate4","artifacts:chestplate4_overlay");
	        registerArtifactIcon("chestplate","artifacts:chestplate5","artifacts:chestplate5_overlay");
	        registerArtifactIcon("helm","artifacts:helm1","artifacts:helm1_overlay");
	        registerArtifactIcon("helm","artifacts:helm2","artifacts:helm2_overlay");
	        registerArtifactIcon("helm","artifacts:helm3","artifacts:helm3_overlay");
	        registerArtifactIcon("helm","artifacts:helm4","artifacts:helm4_overlay");
	        registerArtifactIcon("helm","artifacts:helm5","artifacts:helm5_overlay");
	        registerArtifactIcon("leggings","artifacts:leggings1","artifacts:leggings1_overlay");
	        registerArtifactIcon("leggings","artifacts:leggings2","artifacts:leggings2_overlay");
	        registerArtifactIcon("leggings","artifacts:leggings3","artifacts:leggings3_overlay");
	        registerArtifactIcon("leggings","artifacts:leggings4","artifacts:leggings4_overlay");
	        registerArtifactIcon("leggings","artifacts:leggings5","artifacts:leggings5_overlay");
		}
		catch (Exception e) {
			System.out.println("Error!" + e.getMessage());
		}

        /*registerArtifactIcon("boots","artifacts:boots1");
        registerArtifactIcon("chestplate","artifacts:chestplate1");
        registerArtifactIcon("helm","artifacts:helm1");
        registerArtifactIcon("leggings","artifacts:leggings1");*/
	}

	@Override
	public void registerArtifactIcon(String type, String icon) throws Exception {
		if(type.toLowerCase().equals("amulet")) {
			iconList.add(new AbstractIcon("amulet"+(++numberAmulets),icon));
		}
		else if(type.toLowerCase().equals("dagger")) {
			iconList.add(new AbstractIcon("dagger"+(++numberDaggers),icon));
		}
		else if(type.toLowerCase().equals("figurine")) {
			iconList.add(new AbstractIcon("figurine"+(++numberFigurines),icon));
		}
		else if(type.toLowerCase().equals("ring")) {
			iconList.add(new AbstractIcon("ring"+(++numberRings),icon));
		}
		else if(type.toLowerCase().equals("staff")) {
			iconList.add(new AbstractIcon("staff"+(++numberStaffs),icon));
		}
		else if(type.toLowerCase().equals("sword")) {
			iconList.add(new AbstractIcon("sword"+(++numberSwords),icon));
		}
		else if(type.toLowerCase().equals("trinket")) {
			iconList.add(new AbstractIcon("trinket"+(++numberTrinkets),icon));
		}
		else if(type.toLowerCase().equals("wand")) {
			iconList.add(new AbstractIcon("wand"+(++numberWands),icon));
		}
		else if(type.toLowerCase().equals("boots")) {
			iconList.add(new AbstractIcon("boots"+(++numberBoots),icon));
		}
		else if(type.toLowerCase().equals("chestplate")) {
			iconList.add(new AbstractIcon("chestplate"+(++numberChestplates),icon));
		}
		else if(type.toLowerCase().equals("helm")) {
			iconList.add(new AbstractIcon("helm"+(++numberHelms),icon));
		}
		else if(type.toLowerCase().equals("leggings")) {
			iconList.add(new AbstractIcon("leggings"+(++numberLeggings),icon));
		}
		else {
			throw new Exception("Invalid artifact icon type for " + icon + " with type " + type + ".  Only valid types are: amulet, dagger, figurine, ring, staff, sword, trinket, wand");
		}
	}

	@Override
	public void registerArtifactIcon(String type, String icon, String overlay) throws Exception {
		if(type.toLowerCase().equals("amulet")) {
			iconList.add(new AbstractIcon("amulet"+(++numberAmulets),icon,overlay));
		}
		else if(type.toLowerCase().equals("dagger")) {
			iconList.add(new AbstractIcon("dagger"+(++numberDaggers),icon,overlay));
		}
		else if(type.toLowerCase().equals("figurine")) {
			iconList.add(new AbstractIcon("figurine"+(++numberFigurines),icon,overlay));
		}
		else if(type.toLowerCase().equals("ring")) {
			iconList.add(new AbstractIcon("ring"+(++numberRings),icon,overlay));
		}
		else if(type.toLowerCase().equals("staff")) {
			iconList.add(new AbstractIcon("staff"+(++numberStaffs),icon,overlay));
		}
		else if(type.toLowerCase().equals("sword")) {
			iconList.add(new AbstractIcon("sword"+(++numberSwords),icon,overlay));
		}
		else if(type.toLowerCase().equals("trinket")) {
			iconList.add(new AbstractIcon("trinket"+(++numberTrinkets),icon,overlay));
		}
		else if(type.toLowerCase().equals("wand")) {
			iconList.add(new AbstractIcon("wand"+(++numberWands),icon,overlay));
		}
		else if(type.toLowerCase().equals("boots")) {
			iconList.add(new AbstractIcon("boots"+(++numberBoots),icon,overlay));
		}
		else if(type.toLowerCase().equals("chestplate")) {
			iconList.add(new AbstractIcon("chestplate"+(++numberChestplates),icon,overlay));
		}
		else if(type.toLowerCase().equals("helm")) {
			iconList.add(new AbstractIcon("helm"+(++numberHelms),icon,overlay));
		}
		else if(type.toLowerCase().equals("leggings")) {
			iconList.add(new AbstractIcon("leggings"+(++numberLeggings),icon,overlay));
		}
		else {
			throw new Exception("Invalid artifact icon type '" + icon + "' with type '" + type + "'.  Only valid types are: amulet, dagger, figurine, ring, staff, sword, trinket, wand");
		}
	}

	@Override
	public Icon registerIcons(IconRegister iconReg) {
		Icon defaultIcon = iconReg.registerIcon("artifacts:artifact1");
		icons.put("artifact1", defaultIcon);
		icons.put("artifact2", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact3", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact4", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact5", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact6", iconReg.registerIcon("artifacts:artifact2"));
		Icon overlay = iconReg.registerIcon("artifacts:blank_overlay");
		icons.put("overlay_artifact1",overlay);
		icons.put("overlay_artifact2",overlay);
		icons.put("overlay_artifact3",overlay);
		icons.put("overlay_artifact4",overlay);
		icons.put("overlay_artifact5",overlay);
		icons.put("overlay_artifact6",overlay);
		AbstractIcon a;
		for(int i=0; i < iconList.size(); ++i) {
			a = (AbstractIcon)iconList.get(i);
			Icon ico, ico2;
			if(a.overlay != null) {
				ico2 = iconReg.registerIcon(a.overlay);
				icons.put("overlay_" + a.type, ico2);
			}
			ico = iconReg.registerIcon(a.icon);
			icons.put(a.type, ico);
		}
		icons.put("radar",iconReg.registerIcon("artifacts:radarparticle"));
		return defaultIcon;
	}
	
	private class AbstractIcon {
		public String type;
		public String icon;
		public String overlay;
		
		private AbstractIcon(String t, String i) {
			type = t;
			icon = i;
		}

		private AbstractIcon(String t, String i, String o) {
			type = t;
			icon = i;
			overlay = o;
		}
	}
}
