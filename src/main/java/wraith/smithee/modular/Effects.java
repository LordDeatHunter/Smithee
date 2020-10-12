package wraith.smithee.modular;

public class Effects {
    /*
        Effects are responsible for what the tool does upon breaking a block or hitting a mob
        example: instant smelt or fire damage
        Effects can be modified depending on the incoming material and its priority.
        If the new material's priority (say, fire essence) is higher than an effect present (will not override all effects, only those in its group),
        then the tool will acquire the incoming material's effect. Conversely, should an effect be of lower priority than ones present on the tool, the
        application of the new effect won't happen and instead will disappear.
        Keep in mind: Multiple effects with different priorities from different groups will not affect each other.
        Elaboration on groups: which part of the weapon's usage they will modify - Utility, Offense, Defense & Mining; where Offense XOR Defense
     */



}
