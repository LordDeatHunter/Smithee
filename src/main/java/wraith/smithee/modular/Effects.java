package wraith.smithee.modular;

import java.util.HashSet;

public class Effects {
    /*
        Effects are responsible for what the tool does upon breaking a block or hitting a mob
        example: instant smelt or fire damage
        Effects can be modified depending on the incoming material and its priority.
        If the new material's priority (say, fire essence) is higher than an effect present (will not override all effects, only those in its group),
        then the tool will acquire the incoming material's effect. Conversely, should an effect be of lower priority than ones present on the tool, the
        application of the new effect won't happen and instead will disappear.
        Keep in mind: Multiple effects with different priorities from different groups will not affect each other.
        Elaboration on groups: which part of the weapon's usage they will modify - Utility, Offense, Defense & Mining; where Offense XOR Defense,
        and only one effect per type can be present
     */

    public static HashSet<String> Utility = new HashSet<String>(){{
        add("Magnetic"); //Pull in nearby mobs on the last mob you hit, passively pull in items from the ground
        add("Lucky"); //Works like vanilla Looting & Fortune, has additional synergy with Smelter in the Mining effects list
        add("Mending"); //Essentially vanilla Mending
        add("Haste"); //Increases mining/attack speed
    }};

    public static HashSet<String> Defense = new HashSet<String>(){{
        add("Denial"); //Has a chance to block all incoming melee damage
        add("Dazing"); //Has a chance to push away and stun the attacker when they hit you for a short period of time
        add("Evasive"); //Gives you the ability to dodge ranged attacks entirely -- arrows, fireballs, any projectile
        add("Rift"); //Teleports the melee attacker away from you when you're hit
        add("Fleeting"); //Run faster for a short period of time after getting hit
    }};

    public static HashSet<String> Offense = new HashSet<String>(){{
        add("Serrated"); //Causes bleeding, some mobs are immune
        add("Crippling"); //Causes slowness
        add("Blazing"); //Causes enemy to catch on fire, some are immune
        add("Necrotic"); //Deals bonus damage to living things
        add("Holy"); //Deals bonus damage to undead things
        add("Bash"); //Attacks have a chance to deal bonus damage and stun the enemy for a minuscule duration
        add("Executioner"); //Killing blows now have an additional chance to behead the enemy
    }};

    public static HashSet<String> Mining = new HashSet<String>(){{
        add("Smelter"); //Instantly smelts a given ore if possible
        add("Silky"); //Essentially vanilla Silk Touch, Silky XOR Lucky
    }};

}
