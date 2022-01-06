package app.rendering;

import ca.event.AutomatonEventListener;
import ca.event.GenerationEditedEvent;
import ca.event.GenerationReplacedEvent;
import ca.world.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;

import java.util.Vector;

public class FrameManager implements AutomatonEventListener, Disposable {

    private final Renderer renderer;
    private final Vector<Texture> frames = new Vector<>();

    public FrameManager(Renderer renderer) {
        this.renderer = renderer;
    }

    public Texture getFrameOrRender(int index, World world) {
        ensureCapacity(index+1);
        synchronized (frames) {
            if (index >= frames.size() || frames.get(index) == null) {
                render(index, world);
            }
            return frames.get(index);
        }
    }

    public void render(int index, World world) {
        frames.set(index, renderer.render(world));
    }

    private void ensureCapacity(int size) {
        frames.ensureCapacity(size);
        while (frames.size() < size)
            frames.add(null);
    }

    Queue<GenerationEditedEvent> editedEvents = new Queue<>();

    // processEvents is called in context owning thread to be able to use OpenGL
    public void processEvents() {
        // TODO: 03.01.2022 fix editing while evolving
        editedEvents.forEach( event -> {
            Texture tex = getFrameOrRender(event.getIndex(), event.getWorld());
            renderer.redrawCell(tex, event.getWorld(), event.getPosition());
            // since generations after edited one become unreachable, textures are also invalid, delete them
            frames.subList(event.getIndex() + 1, frames.size()).clear();
        });
        editedEvents.clear();
    }

    @Override
    public void generationEdited(GenerationEditedEvent event) {
        editedEvents.addLast(event);
    }

    @Override
    public void generationReplaced(GenerationReplacedEvent event) {
        int i = event.getIndex();
        if (i > frames.size() - 1)
            ensureCapacity(i+1);
        // invalidate frame
        synchronized (frames) {
            frames.set(i, null);
        }
    }

    @Override
    public void dispose() {
        frames.forEach( tex -> {
            if (tex != null) tex.dispose();
        });
        renderer.dispose();
    }
}
