package ca;

import ca.event.AutomatonEventListener;
import ca.event.GenerationEditedEvent;
import ca.event.GenerationReplacedEvent;
import ca.rules.Rule;
import ca.world.World;

import java.util.ArrayList;
import java.util.List;

public class Automaton<V> {

    protected boolean save = false;

    private final ArrayList<World<V>> generations = new ArrayList<>();
    private final Rule<V> rule;

    protected int current_gen = 0;
    private boolean evolving;

    public Automaton(Rule<V> newRule) {
        rule = newRule;
    }

    public void setRoot(World<V> seed) {
        if (generations.size() == 0)
            generations.add(seed);
        else
            generations.set(0, seed);
    }

    public int size() {
        return generations.size();
    }

    public World<V> get(int index) {
        return generations.get(index);
    }

    public World<V> last() {
        return get(generations.size() - 1);
    }

    public World<V> current() {
        return get(current_gen);
    }

    public World<V> seed() {
        return get(0);
    }

    public void setSaveAll(boolean saveAll) {
        this.save = saveAll;
    }

    public void evolve() {
        if (current_gen != size() - 1) {
            // met past generations, select next gen without evolution
            current_gen++;
            return;
        }

        evolving = true;

        World<V> oldGen, newGen;

        oldGen = last();
        newGen = oldGen.copy();

        for (int[] pos: newGen) {
            newGen.setCell(
                    rule.produceValue(oldGen, pos),
                    pos
            );
        }

        if (!save && current_gen > 0) {
            // if not saving any intermediate generation and not at the root
            generations.set(current_gen, newGen);
            listeners.forEach( l -> l.generationReplaced(new GenerationReplacedEvent(current(), at())));
        } else {
            // otherwise add generation at the end and increment current_gen
            generations.add(newGen);
            current_gen++;
        }

        evolving = false;
    }

    public int at() {
        return current_gen;
    }

    public void select(int position) {
        if (position < 0 || position > size() - 1)
            // not in range
            throw new ArrayIndexOutOfBoundsException(String.format("Index %d should be in range [0; %d]", position, size() - 1));
        current_gen = position;
    }

    private void edit() {
        // because editing current generation makes future generations impossible
        // we delete everything past current gen

        int offset = current_gen + 1;
        List<World<V>> sub = generations.subList(current_gen + 1, size());
        for (int i = 0; i < sub.size(); i++)
        {
            GenerationReplacedEvent event = new GenerationReplacedEvent(sub.get(i), offset + i);
            listeners.forEach(
                l -> l.generationReplaced(event)
            );
        }
        if (!evolving && at() != size() - 1 /* not at last */) sub.clear();

    }

    public V getCell(int... pos) {
        return current().getCell(pos);
    }

    public void setCell(V cell, int... pos) {
        edit();
        current().setCell(cell, pos);
        listeners.forEach( l -> l.generationEdited(new GenerationEditedEvent(current(), at(), pos)));
    }

    private final ArrayList<AutomatonEventListener> listeners = new ArrayList<>();

    public void addListener(AutomatonEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AutomatonEventListener listener) {
        listeners.remove(listener);
    }

    public void reset(int which) {
        // cut off previous
        edit();
        get(which).reset();
        listeners.forEach( l -> l.generationReplaced(
                new GenerationReplacedEvent(current(), at())
        ));
    }

    public void resetCurrent() {
        reset(at());
    }
}
