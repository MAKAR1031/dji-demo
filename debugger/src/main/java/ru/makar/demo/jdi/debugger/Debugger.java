package ru.makar.demo.jdi.debugger;

import com.sun.jdi.*;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;
import ru.makar.demo.jdi.debugger.config.DebuggerConfiguration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Debugger {
    public static void main(String[] args) throws Exception {
        final DebuggerConfiguration config = new DebuggerConfiguration();
        config.load();

        new Debugger().run(config);
    }

    private void run(DebuggerConfiguration config) throws Exception {
        final VirtualMachineManager manager = Bootstrap.virtualMachineManager();
        final AttachingConnector connector = getConnectorByTransportName(manager, config.getTransport());
        if (connector == null) {
            System.err.printf("Connector with transport %s not found%n", config.getTransport());
            return;
        }

        final VirtualMachine vm = attachToVirtualMachine(connector, config);
        final ThreadReference mainThread = getMainThread(vm);
        if (mainThread == null) {
            System.err.println("Main thread not found");
            return;
        }

        mainThread.suspend();
        final EventRequestManager eventRequestManager = vm.eventRequestManager();
        final Location locationForBreakpoint = mainThread.frames().get(3).location();
        final BreakpointRequest breakpoint = eventRequestManager.createBreakpointRequest(locationForBreakpoint);
        mainThread.resume();
        breakpoint.enable();

        while (!mainThread.isAtBreakpoint()) {
            TimeUnit.MILLISECONDS.sleep(10);
        }

        final LocalVariable var = mainThread.frames().get(0).visibleVariableByName(config.getVariableName());
        mainThread.frames().get(0).setValue(var, vm.mirrorOf(config.getNewValue()));

        breakpoint.disable();
        mainThread.resume();
        vm.resume();
        vm.dispose();
        System.out.println("Complete");
    }

    @SuppressWarnings("SameParameterValue")
    private AttachingConnector getConnectorByTransportName(VirtualMachineManager manager, String transport) {
        return manager.attachingConnectors()
                .stream()
                .filter(c -> transport.equals(c.transport().name()))
                .findFirst()
                .orElse(null);
    }

    private VirtualMachine attachToVirtualMachine(AttachingConnector connector, DebuggerConfiguration config)
            throws IOException, IllegalConnectorArgumentsException {
        final Map<String, Connector.Argument> arguments = connector.defaultArguments();
        arguments.get("hostname").setValue(config.getHost());
        arguments.get("port").setValue(config.getPort());
        return connector.attach(arguments);
    }

    private ThreadReference getMainThread(VirtualMachine vm) {
        return vm.allThreads()
                .stream()
                .filter(t -> "main".equals(t.name()))
                .findFirst()
                .orElse(null);
    }
}
