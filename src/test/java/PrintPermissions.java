import java.lang.reflect.Field;

import com.jeremiahbl.bfcmod.config.PermissionsHandler;

import net.minecraftforge.server.permission.nodes.PermissionNode;

public class PrintPermissions {
	public static void main(String[] args) {
		System.out.println("Permission node | Name | Description | Type | Default");
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
		}
	}
}
