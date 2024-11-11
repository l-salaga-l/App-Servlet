package controller;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.IOException;
import java.io.InputStream;

public class DelegatingServletInputStream extends ServletInputStream {

    private final InputStream sourceStream;

    public DelegatingServletInputStream(InputStream sourceStream) {
        this.sourceStream = sourceStream;
    }

    @Override
    public int read() throws IOException {
        return this.sourceStream.read();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }
}
