package wraith.smithee.items.tools;

public interface BaseSmitheeMeleeWeapon extends BaseSmitheeItem {

    @Override
    default String getBindingType() {
        return "sword_guard";
    }

}
