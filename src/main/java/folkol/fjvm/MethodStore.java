package folkol.fjvm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MethodStore {
    Map<String, byte[]> methods = new HashMap<String, byte[]>();
    Map<Integer, Object> constantTable = new HashMap<Integer, Object>();


    java.lang.reflect.Method method;

    public void readMethods(java.lang.Class clazz) {
        URL location = clazz.getProtectionDomain().getCodeSource().getLocation();
        File file = new File(location.getFile() + clazz.getName() + ".class");
        try {
            DataInputStream classFileStream = new DataInputStream(new FileInputStream(file));

            // Skip through the class file stream...

            // ...magic number (0xCAFEBABE)
            classFileStream.skipBytes(4);

            // ...minor version
            classFileStream.skipBytes(2);

            // ...major Version
            classFileStream.skipBytes(2);

            // ...constant pool table
            short constantCount = classFileStream.readShort();
            for(int i = 0; i < (constantCount - 1); i++) {
                String constant = skipConstant(classFileStream);
                constantTable.put(new Integer(i+1), constant);
            }

            // ...access flags
            classFileStream.skipBytes(2);

            // ...this class reference
            classFileStream.skipBytes(2);

            // ...super class reference
            classFileStream.skipBytes(2);

            // ...interface table
            short interfaceCount = classFileStream.readShort();
            for(int i = 0; i < interfaceCount; i++) {
                skipInterface(classFileStream);
            }

            // ...field table
            short fieldCount = classFileStream.readShort();
            for(int i = 0; i < fieldCount; i++) {
                skipField(classFileStream);
            }

            // ...method table
            short methodCount = classFileStream.readShort();
            for(int i = 0; i < methodCount; i++) {
                skipMethod(classFileStream);
            }

            // ...attribute table
            short attributeCount = classFileStream.readShort();
            for(int i = 0; i < attributeCount; i++) {
                skipAttribute(classFileStream);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] getCode(String method) {
        return methods.get(method);
    }

    private String skipConstant(DataInputStream classFileStream) throws IOException {
        byte tag = classFileStream.readByte();
        String returnString = null;
        switch (tag) {
        case 1: // UTF-8 String (But not quite)
            returnString = classFileStream.readUTF();
            break;
        case 3: // Integer
        case 4: // Float
        case 9: // Field reference
        case 10: // Method reference
        case 11: // Interface method reference
        case 12: // Name and type descriptor
            classFileStream.skipBytes(4);
            break;
        case 5: // Long
        case 6: // Double
            classFileStream.skipBytes(8);
            break;
        case 7: // Class reference
        case 8: // String reference
            classFileStream.skipBytes(2);
            break;
        default:
            throw new RuntimeException("Unknown constant type tag!");
        }
        return returnString;
    }

    private void skipMethod(DataInputStream classFileStream) throws IOException {
        // Access flags
        classFileStream.skipBytes(2);

        // Name index
        short nameIndex = classFileStream.readShort();
        String methodName = (String) constantTable.get(new Integer(nameIndex));

        // Descriptor index
        classFileStream.skipBytes(2);

        // Attributes count
        short attributesCount = classFileStream.readShort();
        for(int i = 0; i < attributesCount; i++) {
            skipAttribute(classFileStream, methodName);
        }

        // If neither ACC_ABSTRACT nor ACC_NATIVE is set, method byte code will follow here

    }

    private void skipField(DataInputStream classFileStream) throws IOException {
        // Access flags
        classFileStream.skipBytes(2);

        // Name index
        classFileStream.skipBytes(2);

        // Descriptor index
        classFileStream.skipBytes(2);

        // Attributes count
        short attributesCount = classFileStream.readShort();
        for(int i = 0; i < attributesCount; i++) {
            skipAttribute(classFileStream);
        }
    }

    private void skipAttribute(DataInputStream classFileStream) throws IOException {
        skipAttribute(classFileStream, null);
    }

    private void skipAttribute(DataInputStream classFileStream, String method) throws IOException {
        // Attribute name index
        short attributeName = classFileStream.readShort();
        String attributeNameResolved = (String) constantTable.get(new Integer(attributeName));

        // Attribute length
        int attributeCount = classFileStream.readInt();

        // Attributes
        byte[] bytes = new byte[attributeCount];
        classFileStream.read(bytes);
        if("Code".equals(attributeNameResolved)) {
            methods.put(method, bytes);
        }
    }

    private void skipInterface(DataInputStream classFileStream) throws IOException {
        classFileStream.skipBytes(2);
    }
}

