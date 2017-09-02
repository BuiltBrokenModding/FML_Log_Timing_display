package com.builtbroken.mc.logp.data;

import java.util.Comparator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class ModDataTimeSorter implements Comparator<ModData>
{
    @Override
    public int compare(ModData o1, ModData o2)
    {
        return Long.compare(o2.getTime(), o1.getTime());
    }
}
