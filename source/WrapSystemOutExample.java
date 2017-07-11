package com.example;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This example describe how to wrap system out or redirect system out to file
 */
public class WrapSystemOutExample {

    private static final String STR = "Hello My Wonderful World!";

    public static void main(String[] args) {
        System.out.println(STR);
        change();
        System.out.println(STR);
        changeByUsingFileDescription();
        System.out.println(STR);
    }

    private static void change() {
        System.setOut(new WrapPrintStream(System.out));
    }

    private static void changeByUsingFileDescription() {
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
