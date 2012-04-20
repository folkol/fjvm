package folkol.fjvm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackFrame {
    private int instructionPointer;
    private List<Integer> localVariables = new ArrayList<Integer>();
    private MethodStore methodStore;
    public Stack<Integer> operandStack = new Stack<Integer>();
    private String method;

    StackFrame(String method, MethodStore methodStore) {
        instructionPointer = 0;
        this.methodStore = methodStore;
        this.method = method;
    }

    public byte currentInstruction() {
        byte instruction = methodStore.getCode(method)[instructionPointer];
        instructionPointer++;
        return instruction;
    }

    public String getMethodName() {
        return method;
    }
}
