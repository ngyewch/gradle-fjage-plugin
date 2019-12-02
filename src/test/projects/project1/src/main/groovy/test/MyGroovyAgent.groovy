package test

import org.arl.fjage.Agent
import org.arl.fjage.OneShotBehavior
import org.yaml.snakeyaml.Yaml

class MyGroovyAgent extends Agent {

    @Override
    void init() {
        add new OneShotBehavior({
            println 'MyGroovyAgent says "Hello, world!"'
            println new Yaml().dump(System.getProperties())
        })
    }
}
