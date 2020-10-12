package wraith.smithee;

import net.minecraft.util.Identifier;

public class RepairType {

    //Item or Item-Tag
    public final String type;
    //The identifier for the item/tag
    public final Identifier material;

    public RepairType(String type, Identifier material) {
        this.type = type;
        this.material = material;
    }

    public RepairType(String type, String namespace, String path) {
        this.type = type;
        this.material = new Identifier(namespace, path);
    }

}
