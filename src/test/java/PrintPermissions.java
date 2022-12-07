import java.lang.reflect.Field;

import com.jeremiahbl.bfcmod.config.PermissionsHandler;
import com.jeremiahbl.bfcmod.config.PlayerData;

import net.minecraftforge.server.permission.nodes.PermissionNode;

public class PrintPermissions {
	public static void main(String[] args) {
		String enc = PlayerData.encodeStr("hello \\ world");
		System.out.println(enc);
		System.out.println(PlayerData.decodeStr(enc));
		System.out.println(PlayerData.decodeStr("\"\\u0049\""));
		System.out.println(PlayerData.decodeStr("\"..\\u004A\""));
		System.out.println(PlayerData.decodeStr("\"\\u004B..\""));
		System.out.println(PlayerData.decodeStr("\"..\\u004C..\""));
		/* System.out.println("Permission node | Name | Description | Type | Default");
		System.out.println("--------------- | ---- | ----------- | ---- | ------");
		for(Field fld : PermissionsHandler.class.getDeclaredFields()) {
			if(fld.getType() == PermissionNode.class) {
				try { // Fuck adding all these nodes manually
					PermissionNode<?> node = (PermissionNode<?>) fld.get(PermissionNode.class);
					System.out.println(node.getNodeName() + " | " + 
							node.getReadableName().getString() + " | " + 
							node.getDescription().getString() + " | " + 
							node.getType().typeName() + " | " + 
							String.valueOf(node.getDefaultResolver().resolve(null, null, null)));
				} catch (Exception e) {}
			}
		}*/
	}
}
