package folkol.fjvm;

import java.util.Stack;

public class Thread {
    private String name;
    public Stack<StackFrame> stack = new Stack<StackFrame>();

    public Thread(String string) {
        name = string;
    }

    public void doStep() {
    }

    public StackFrame currentStackFrame() {
        return stack.peek();
    }
}
