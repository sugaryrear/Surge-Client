package com.client;

import com.client.sign.Signlink;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemBonusDefinition {

	private static Map<Integer, ItemBonusDefinition> itemBonusDefinitions =
			new HashMap<Integer, ItemBonusDefinition>();

	private static XStream xStream = new XStream(new Sun14ReflectionProvider());
	private short id;
	private short[] bonuses;

	public short[] getBonuses() {
		return bonuses;
	}

	public int getId() {
		return id;
	}

	public static ItemBonusDefinition getItemBonusDefinition(int i) {
		return itemBonusDefinitions.get(i);
	}

	public static short[] getItemBonuses(int id) {
		ItemBonusDefinition def = getItemBonusDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	@Override
	public String toString() {
		return "ItemBonusDefinition [id=" + id + ", bonuses=" + Arrays.toString(bonuses) + "]";
	}

	public static void loadItemBonusDefinitions() throws IOException {
		xStream.addPermission(NoTypePermission.NONE);
		xStream.addPermission(AnyTypePermission.ANY); // You can be more specific here
		xStream.allowTypes(new Class[]{ItemBonusDefinition.class});


		xStream.alias("ItemBonusDefinition", ItemBonusDefinition.class);
		// file added?

		List<ItemBonusDefinition> list = (List<ItemBonusDefinition>) xStream.fromXML(
				new FileInputStream(Signlink.getCacheDirectory() + "ItemBonusDefinitions.xml"));
		for (ItemBonusDefinition definition : list) {
			itemBonusDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " item bonus definitions.");
	}
}