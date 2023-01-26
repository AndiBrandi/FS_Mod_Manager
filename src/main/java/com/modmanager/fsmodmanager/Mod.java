package com.modmanager.fsmodmanager;

import java.io.File;
import java.net.URI;

public class Mod extends File {

    public Mod(String pathname) {
        super(pathname);
    }

    public Mod(String parent, String child) {
        super(parent, child);
    }

    public Mod(File parent, String child) {
        super(parent, child);
    }

    public Mod(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
