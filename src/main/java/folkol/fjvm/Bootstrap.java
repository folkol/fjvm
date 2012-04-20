package folkol.fjvm;

/**
 * This is the bootstrap class of the Folkol Java Virtual Machine, it will
 * attempt to find out a class where the main entrypoint is and launch a new
 * virtual machine.
 */
public class Bootstrap {
    public static void main(String[] args) {
        String mainClass = "Example";
        if (args.length > 0) {
            mainClass = args[0];
        }
        new VirtualMachine(mainClass).run();
        System.out.println("Done!");
    }
}
