package ca.rules;

import ca.values.Binary;
import ca.world.World;

import static ca.values.Binary.OFF;
import static ca.values.Binary.ON;

public class WolframRule implements Rule {

    private final short[] rule_values = new short[8];
    private final int radius = 1;

    public WolframRule(int representation) {
        for (int shift = 0; shift < 8; shift++) {
            rule_values[shift] = (representation & (int) Math.pow(2, shift)) > 0 ? ON : OFF;
        }
    }

    @Override
    public Class<Binary> supportedCells() {
        return Binary.class;
    }

    @Override
    public short produceValue(World world, int... position) {
        int lut_i = 0;
        for (int i = -radius; i < radius; i ++)
            // put 1 on 2-i bit if cell is alive
            lut_i |= world.getCell(position[0] + i) == ON ? 1 << (2-i): 0;

        return rule_values[lut_i];
    }

}
