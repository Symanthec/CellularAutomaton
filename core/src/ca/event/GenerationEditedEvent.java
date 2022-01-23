package ca.event;

import ca.world.World;

public class GenerationEditedEvent {

    private final World world;
    private final int index;
    private final int[] pos;

    public World getWorld() {
        return world;
    }

    public int getIndex() {
        return index;
    }

    public int[] getPosition() {
        return pos;
    }

    public GenerationEditedEvent(World world, int i, int... pos) {
        this.world = world;
        this.index = i;
        this.pos = pos;
    }

}
