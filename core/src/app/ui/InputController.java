package app.ui;

import app.automaton.EvolutionThread;
import app.ui.panes.Values;
import ca.Automaton;
import ca.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;

@SuppressWarnings("rawtypes")
public class InputController extends InputAdapter {

    private final OrthographicCamera camera;
    private Automaton automaton = null;
    private final GUI gui;

    private Values values;

    public InputController(GUI gui, OrthographicCamera cam) {
        this.camera = cam;
        this.gui = gui;
        camera.zoom = zoom;
        camera.update();
    }

    public void setAutomaton(Automaton automaton, Values values) {
        this.automaton = automaton;
        this.values = values;
    }

    private final Vector2 camera_speed = new Vector2();
    private final float speed = 300/*px*/;

    private final Vector2 zoom_limits = new Vector2(1/16f, 1.0f);// min and max
    private float zoom = (zoom_limits.x + zoom_limits.y) / 2; // average min max

    private boolean painting = false;
    private float last_x = 0, last_y = 0;

    @Override
    public boolean scrolled(float amountX, float amountY) {
        zoom = MathUtils.clamp(zoom + amountY / 16f, zoom_limits.x, zoom_limits.y);
        camera.zoom = zoom;
        camera.update();
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (automaton != null)
        switch (keycode) {
            case Keys.W:
                camera_speed.y += speed;
                return true;
            case Keys.S:
                camera_speed.y -= speed;
                return true;
            case Keys.A:
                camera_speed.x -= speed;
                return true;
            case Keys.D:
                camera_speed.x += speed;
                return true;
            case Keys.E:
                if (values != null) {
                    values.next();
                    System.out.println(values.get());
                }
                return true;
            case Keys.SPACE:
                EvolutionThread evo = gui.getEvolver();
                evo.setEvolveCount(
                        evo.getEvolveCount() != 0 ? 0: -1L
                );
                return true;
            case Keys.LEFT:
                int pos = Math.max(automaton.at() - 1, 0);
                automaton.select(pos);
                return true;
            case Keys.RIGHT:
                if (automaton.at() == automaton.size() - 1) {
                    automaton.evolve();
                } else {
                    automaton.select(automaton.at() + 1);
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // editing while evolving causes ConcurrentModificationException
        if (automaton != null) {
            EvolutionThread evo = gui.getEvolver();
            if (evo.getEvolveCount() != 0) return true;
            Ray ray = camera.getPickRay(screenX, screenY);
            int x = (int) ray.origin.x;
            int y = (int) ray.origin.y;

            last_x = x;
            last_y = y;
            painting = true;
            drawCell(x, y);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (painting) {
            Ray ray = camera.getPickRay(screenX, screenY);
            float ray_x = ray.origin.x;
            float ray_y = ray.origin.y;

            // interpolating by more changed coordinate
            if (Math.abs(ray_x - last_x) > Math.abs(ray_y - last_y)) {
                float k = (ray_y - last_y) / (ray_x - last_x), b = ray_y - k * ray_x;

                for (float dx = Math.min(ray_x, last_x); dx < Math.max(ray_x, last_x); dx += 1) {
                    float y = k*dx + b;
                    drawCell((int) dx, (int) y);
                }
            } else {
                // use x = ky + b
                float k = (ray_x - last_x) / (ray_y - last_y), b = ray_x - k * ray_y;

                for (float dy = Math.min(ray_y, last_y); dy < Math.max(ray_y, last_y); dy += 1) {
                    float x = k*dy + b;
                    drawCell((int) x, (int) dy);
                }
            }

            last_x = ray_x;
            last_y = ray_y;
            drawCell((int) ray_x, (int) ray_y);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        painting = false;
        return true;
    }

    protected void drawCell(int x, int y) {
        if (automaton != null && values != null) {
            World world = automaton.current();
            if (0 <= x && x < world.getBounds()[0] && 0 <= y && y < world.getBounds()[1]) {
                automaton.setCell(values.get(), x, y);
            }
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                camera_speed.y -= speed;
                return true;
            case Keys.S:
                camera_speed.y += speed;
                return true;
            case Keys.A:
                camera_speed.x += speed;
                return true;
            case Keys.D:
                camera_speed.x -= speed;
                return true;
        }
        return false;
    }

    public void update() {
        camera.position.add(camera_speed.x * Gdx.graphics.getDeltaTime(), camera_speed.y * Gdx.graphics.getDeltaTime(), 0);
        camera.update();
    }

}
