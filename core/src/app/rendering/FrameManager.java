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
            if (index >= frames.size() || getFrame(index) == null) {
                render(index, world);
            }
            return getFrame(index);
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

    Queue<Texture> pendingDispose = new Queue<>();

    // processEvents is called in context owning thread to be able to use OpenGL

    public void processEvents() {
        editedEvents.forEach( event -> {
            Texture tex = getFrameOrRender(event.getIndex(), event.getWorld());
            renderer.redrawCell(tex, event.getWorld(), event.getPosition());
            // since generations after edited one become unreachable, textures are also invalid, delete them
            frames.subList(event.getIndex() + 1, frames.size()).clear();
        });
        editedEvents.clear();

        // clear texture awaiting removal
        pendingDispose.forEach(Texture::dispose);
        pendingDispose.clear();
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
            if (frames.get(i) != null)
                pendingDispose.addLast(frames.get(i));
            frames.set(i, null);
        }
    }

    @Override
    public void dispose() {
        reset();
        renderer.dispose();
    }

    public void reset() {
        frames.forEach( t -> {
            if (t != null) t.dispose();
        });
        System.gc();
        frames.clear();
    }

    public void setPalette(Palette palette) {
        reset();
        renderer.setPalette(palette);
    }

    public Texture getFrame(int index) {
        return frames.get(index);
    }
}
