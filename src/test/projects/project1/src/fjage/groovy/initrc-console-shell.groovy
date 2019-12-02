import org.arl.fjage.*
import org.arl.fjage.remote.*
import org.arl.fjage.shell.*

int port = 5081
try {
    port =  Integer.parseInt(System.properties.getProperty('fjage.port'))
} catch (Exception ex) {
    // do nothing
}

platform = new RealTimePlatform()
container = new MasterContainer(platform, port)
shell = new ShellAgent(new ConsoleShell(), new GroovyScriptEngine())

container.add 'shell', shell
platform.start()
