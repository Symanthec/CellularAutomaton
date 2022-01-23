package app.automaton;

import ca.Automaton;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class EvolutionThread extends Thread {

    private final AtomicLong evolveCount;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private Automaton automaton;

    public void setAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }

    public long getEvolveCount() {
        return evolveCount.get();
    }

    public void setEvolveCount(long count) {
        evolveCount.set(count);
    }

    public EvolutionThread(Automaton automaton) {
        this(automaton, 0L);
    }

    public EvolutionThread(Automaton automaton, long evolveCount) {
        this.automaton = automaton;
        this.evolveCount = new AtomicLong(evolveCount);
    }

    public void killThread() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            if (automaton != null & evolveCount.get() != 0) {
                evolveCount.decrementAndGet();
                automaton.evolve();
            }
        }
    }
}