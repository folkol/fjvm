package folkol.fjvm;

import java.util.ArrayList;
import java.util.List;

public class VirtualMachine {

    private List<folkol.fjvm.Thread> threads = new ArrayList<folkol.fjvm.Thread>();
    private List<folkol.fjvm.Class> classes = new ArrayList<folkol.fjvm.Class>();

    public VirtualMachine(String mainClassName) {
        classes.add(new folkol.fjvm.Class(mainClassName));

    }

    public int run() {
        folkol.fjvm.Thread thread = new folkol.fjvm.Thread();
        threads.add(thread);
        return 0;
    }
}
