package com.builtbroken.mc.logp.data;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class ModData
{
    public final String modName;

    public HashMap<String, Long> loadTimes = new HashMap();

    public ModData(String name)
    {
        modName = name;
    }

    public void addEntry(String action, String load)
    {
        loadTimes.put(action, toMilliSeconds(load.replace("s", "")));
    }

    protected long toMilliSeconds(String timeString)
    {
        double time = Double.parseDouble(timeString);
        time = time * 1000;

        return (long) Math.floor(time);
    }

    public long getTime()
    {
        long total = 0;
        for(long p : loadTimes.values())
        {
            total += p;
        }
        return total;
    }
}
