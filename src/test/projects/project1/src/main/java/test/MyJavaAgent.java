package test;

import org.arl.fjage.Agent;
import org.arl.fjage.OneShotBehavior;
import org.yaml.snakeyaml.Yaml;

public class MyJavaAgent extends Agent {

    @Override
    public void init() {
        add(new OneShotBehavior() {

            @Override
            public void action() {
                System.out.println("MyJavaAgent says 'Hello, world!'");
                System.out.println(new Yaml().dump(System.getProperties()));
            }
        });
    }
}
