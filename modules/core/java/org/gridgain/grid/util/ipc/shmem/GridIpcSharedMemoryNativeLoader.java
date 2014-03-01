/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.util.ipc.shmem;

import org.gridgain.grid.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author @java.author
 * @version @java.version
 */
@SuppressWarnings("ErrorNotRethrown")
public class GridIpcSharedMemoryNativeLoader {
    /** */
    private static volatile boolean loaded;

    /** */
    private static final String LIB_NAME = "ggshmem";

    /**
     * @return Operating system name to resolve path to library.
     */
    private static String os() {
        String name = System.getProperty("os.name").toLowerCase().trim();

        if (name.startsWith("win"))
            throw new IllegalStateException("IPC shared memory native loader should not be called on windows.");

        if (name.startsWith("linux"))
            return "linux";

        if (name.startsWith("mac os x"))
            return "osx";

        return name.replaceAll("\\W+", "_");
    }

    /**
     * @return Platform.
     */
    private static String platform() {
        return os() + bitModel();
    }

    /**
     * @return Bit model.
     */
    private static int bitModel() {
        String prop = System.getProperty("sun.arch.data.model");

        if (prop == null)
            prop = System.getProperty("com.ibm.vm.bitmode");

        if (prop != null)
            return Integer.parseInt(prop);

        // We don't know.
        return -1;
    }

    /**
     * @throws GridException If failed.
     */
    public static void load() throws GridException {
        if (loaded)
            return;

        synchronized (GridIpcSharedMemoryNativeLoader.class) {
            if (loaded)
                return;

            doLoad();

            loaded = true;
        }
    }

    /**
     * @throws GridException If failed.
     */
    private static void doLoad() throws GridException {
        Collection<Throwable> errs = new LinkedList<>();

        try {
            // Load native library (the library directory should be in java.library.path).
            System.loadLibrary(LIB_NAME);

            return;
        }
        catch (UnsatisfiedLinkError e) {
            errs.add(e);
        }

        if (extractAndLoad(errs, platformSpecific()))
            return;

        if (extractAndLoad(errs, osSpecificResourcePath()))
            return;

        if (extractAndLoad(errs, resourcePath()))
            return;

        // Failed to find the library.
        assert !errs.isEmpty();

        throw new GridException("Failed to load native IPC library: " + errs);
    }

    /**
     * @return OS resource path.
     */
    private static String osSpecificResourcePath() {
        return "META-INF/native/" + os() + "/" + mapLibraryName();
    }

    /**
     * @return Platform resource path.
     */
    private static String platformSpecific() {
        return "META-INF/native/" + platform() + "/" + mapLibraryName();
    }

    /**
     * @return Maps library name to file name.
     */
    private static String mapLibraryName() {
        String libName = System.mapLibraryName(LIB_NAME);

        if (U.isMacOs() && libName.endsWith(".jnilib"))
            return libName.substring(0, libName.length() - "jnilib".length()) + "dylib";

        return libName;
    }

    /**
     * @return Resource path.
     */
    private static String resourcePath() {
        return "META-INF/native/" + mapLibraryName();
    }

    /**
     * @param errs Errors collection.
     * @param rsrcPath Path.
     * @return {@code True} if library was found and loaded.
     */
    private static boolean extractAndLoad(Collection<Throwable> errs, String rsrcPath) {
        ClassLoader clsLdr = U.detectClassLoader(GridIpcSharedMemoryNativeLoader.class);

        URL rsrc = clsLdr.getResource(rsrcPath);

        if (rsrc != null) {
            return extract(errs,
                rsrc,
                new File(System.getProperty("java.io.tmpdir"), mapLibraryName()));
        }
        else
            errs.add(new IllegalStateException("Failed to find resource with specified class loader " +
                "[rsrc=" + rsrcPath + ", clsLdr=" + clsLdr + ']'));

        return false;
    }

    /**
     * @param errs Errors collection.
     * @param src Source.
     * @param target Target.
     * @return {@code True} if resource was found and loaded.
     */
    private static boolean extract(Collection<Throwable> errs, URL src, File target) {
        FileOutputStream os = null;
        InputStream is = null;

        boolean err = true;

        try {
            if (!target.exists() || isStale(src, target)) {
                is = src.openStream();

                if (is != null) {
                    os = new FileOutputStream(target);

                    int read;

                    byte[] buf = new byte[4096];

                    while ((read = is.read(buf)) != -1)
                        os.write(buf, 0, read);
                }
            }

            // chmod 775.
            if (!U.isWindows())
                Runtime.getRuntime().exec(
                    new String[] {"chmod", "775", target.getCanonicalPath()}).waitFor();

            System.load(target.getPath());

            err = false;
        }
        catch (IOException | UnsatisfiedLinkError | InterruptedException e) {
            errs.add(e);
        } finally {
            U.closeQuiet(os);
            U.closeQuiet(is);

            if (err && target.exists())
                target.delete();
        }

        return !err;
    }

    /**
     * @param src Source.
     * @param target Target.
     * @return {@code True} if target is stale.
     */
    private static boolean isStale(URL src, File target) {
        if ("jar".equals(src.getProtocol())) {
            // Unwrap the jar protocol.
            try {
                String parts[] = src.getFile().split(Pattern.quote("!"));

                src = new URL(parts[0]);
            }
            catch (MalformedURLException ignored) {
                return false;
            }
        }

        File srcFile = null;

        if ("file".equals(src.getProtocol()))
            srcFile = new File(src.getFile());

        return srcFile != null && srcFile.exists() && srcFile.lastModified() > target.lastModified();
    }
}
