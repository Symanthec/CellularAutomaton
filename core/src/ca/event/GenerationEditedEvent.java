package ca.event;

import ca.world.World;

public class GenerationEditedEvent {

    private World world;
    private int index;
    private int[] pos;

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
