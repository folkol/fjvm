package folkol.fjvm;

/**
 * This is the bootstrap class of the Folkol Java Virtual "Machine"
 *
 * The machine will bootstrap an entrypoint, and after that loop through the active
 * threads, and for each thread perform one instruction.
 * This scheduler might not be very efficient, but still! :)
 *
 * Most instructions are not yet implemented, among them {invokespecial} which means
 * that I at this point will have to fake the method calls, so the two callframes that
 * exist are added manually.
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
