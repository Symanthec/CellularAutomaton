package ca.rules;

import ca.collecting.RadialCollector1D;
import ca.values.Binary;
import ca.world.World;

import static ca.values.Binary.OFF;
import static ca.values.Binary.ON;

public class WolframRule implements Rule<Binary> {

    private static final RadialCollector1D<Binary> collector = new RadialCollector1D<>(Binary.class);
    private final Binary[] rule_values = new Binary[8];

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
    public Binary produceValue(World<Binary> world, int... position) {
        Binary[] neighbors = collector.collect(world, position);

        int lut_i = 0;
        for (int i = 0; i < 3; i ++)
            // put 1 on 2-i bit if cell is alive
            lut_i |= neighbors[i].value ? 1 << (2-i): 0;

        return rule_values[lut_i];
    }

}
