import java.io.*;

/**
 * This example describe how to wrap system out.
 * Field System.out is static final, it means that we can not change value of this field. But we can do it
 * by using System.setOut. It calls native method setOut0 that change static final field.
 */
public class WrapSystemOutExample {

    private static final String STR = "Hello My Wonderful World!";

    public static void main(String[] args) {
        System.out.println(STR);
        wrapPrintStream();
        System.out.println(STR);
        wrapPrintStreamByUsingFileDescription();
        System.out.println(STR);

    }

    private static void wrapPrintStream() {
        System.setOut(new WrapPrintStream(System.out));
    }

    private static void wrapPrintStreamByUsingFileDescription() {
        FileOutputStream fileOutputStream = new FileOutputStream(FileDescriptor.out) {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                //check new line (\n)
                if (len == 1 && b[0] == 10) {
                    super.write(b, off, len);
                    return;
                }
                String newMsg = "TIME or LOG LEVEL: " + new String(b, off, len);
                super.write(newMsg.getBytes(), off, newMsg.length());
                //flush will be performed by PrintStream
            }
        };
        System.setOut(new PrintStream(fileOutputStream));
    }
}

class WrapPrintStream extends PrintStream {

    public WrapPrintStream(final PrintStream printStream) {
        super(printStream);
    }

    @Override
    public void println(String x) {
        StringBuilder sb = new StringBuilder("LEVEL MESSAGE: ");
        sb.append(" current time: ")
                .append(System.currentTimeMillis())
                .append(" Message: ")
                .append(x);
        super.println(sb.toString());
    }

    //override other method
}
