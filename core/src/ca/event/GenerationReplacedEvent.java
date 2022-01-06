package ca.event;

import ca.world.World;

public class GenerationReplacedEvent {

    public World getWorld() {
        return world;
    }

    public int getIndex() {
        return index;
    }

    private World world;
    private int index;

    public GenerationReplacedEvent(World world, int index) {
        this.world = world;
        this.index = index;
    }

}
