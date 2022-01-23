package app.automaton;

import ca.rules.BrianBrain;
import ca.rules.LifeRule;
import ca.rules.Rule;
import ca.rules.WireworldRule;

import java.util.Vector;

public class RuleRegistry {

    private static final Vector<Rule> values = new Vector<>();

    static {
        values.add(new BrianBrain());
        values.add(new LifeRule());
        values.add(new WireworldRule());
    }

    public static void registerRule(Rule rule) {
        if (!values.contains(rule))
            values.add(rule);
    }

    public static Rule[] getAvailableRules() {
        return values.toArray(new Rule[0]);
    }

}
