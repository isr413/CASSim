package com.seat.rescuesim.simserver;

import com.seat.rescuesim.common.Map;
import com.seat.rescuesim.common.ScenarioConfig;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        ScenarioConfig config = new ScenarioConfig(
            100,
            1.37,
            1.0,
            0.0,
            new Map(13, 13, 10)
        );
        String json = config.encode();
        System.out.println(ScenarioConfig.decode(json).encode());
    }
}
