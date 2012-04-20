package folkol.fjvm;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualMachine {
    private List<Thread> threads = new ArrayList<Thread>();
    private Map<String, java.lang.Class> classes = new HashMap<String, java.lang.Class>();
    private URL[] classPath = new URL[1];
    private URLClassLoader myClassLoader;
    private MethodStore myMethodStore = new MethodStore();

    // MNEMONICS
    public static final byte instruction_nop = 0x0;
    public static final byte instruction_aconst_null = (byte) 0x1;
    public static final byte instruction_iconst_0 = (byte) 0x3;
    public static final byte instruction_iconst_3 = (byte) 0x6;
    public static final byte instruction_iconst_4 = (byte) 0x7;
    public static final byte instruction_return = (byte) 0xb1;

    public VirtualMachine(String mainClassName) {
        StackFrame stackFrame = null;
        StackFrame stackFrame2 = null;
        try {
            classPath[0] = new URL("file:/Users/folkol/labbar/fjvm/src/resources/");
            myClassLoader = new URLClassLoader(classPath);
            classes.put(mainClassName, myClassLoader.loadClass(mainClassName));
            java.lang.Class<?> clazz = classes.get(mainClassName);
            myMethodStore.readMethods(clazz);
            stackFrame = new StackFrame("doStuff", myMethodStore);
            stackFrame2 = new StackFrame("doMoreStuff", myMethodStore);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // stackFrame.
        Thread thread = new Thread("Main");
        thread.stack.push(stackFrame);
        System.out.println("Entering method: doStuff (This is fake!)");
        thread.stack.push(stackFrame2);
        System.out.println("Entering method: doMoreStuff (This is fake!)");

        threads.add(thread);
    }

    public int run() {
        while (threads.size() > 0) {
            for (int i = 0; i < threads.size(); i++) {
                Thread thread = threads.get(i);
                if (thread.stack.size() > 0) {
                    executeNextInstruction(thread);
                } else {
                    threads.remove(thread);
                }
            }
        }
        return 0;
    }

    private void executeNextInstruction(Thread thread) {
        byte instr = thread.currentStackFrame().currentInstruction();

        switch (instr) {
            case instruction_nop:
                break;

            case instruction_aconst_null:
                thread.currentStackFrame().operandStack.push(null);
                break;

            case instruction_iconst_0:
                thread.currentStackFrame().operandStack.push(0);
                break;

            case instruction_iconst_3:
                thread.currentStackFrame().operandStack.push(3);
                break;

            case instruction_iconst_4:
                thread.currentStackFrame().operandStack.push(4);
                break;

            case instruction_return:
                System.out.println("Returning from method: " + thread.currentStackFrame().getMethodName());
                thread.stack.pop();
                break;

            default:
                throw new RuntimeException("Unrecognized instruction! (" + (((int)instr) & 0xFF));
        }
    }
}
