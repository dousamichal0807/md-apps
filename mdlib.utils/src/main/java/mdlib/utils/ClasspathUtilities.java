package mdlib.utils;

import mdlib.utils.debug.Debugger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public final class ClasspathUtilities {

    // Do not create any instance
    private ClasspathUtilities() {
    }

    public static Path currentWorkingDirectory(final Class<?> cwdClass) {
        try {
            Path cwd = Paths.get(cwdClass.getProtectionDomain().getCodeSource().getLocation().toURI());
            cwd = Paths.get("/").resolve(cwd.subpath(0, cwd.getNameCount() - 1));
            return cwd;
        } catch (URISyntaxException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static Attributes jarMfAttributes(final Class<?> clazz) {
        try (InputStream inputStream = clazz.getResourceAsStream("/META-INF/MANIFEST.MF")) {
            Manifest manifest = new Manifest(inputStream);
            return manifest.getMainAttributes();
        } catch(IOException exc) {
            StringWriter msg = new StringWriter();
            msg.write("getMainfest() encountered an unexpected error:\n");
            exc.printStackTrace(new PrintWriter(msg));
            Debugger.error(ClasspathUtilities.class, msg);
            throw new RuntimeException(exc);
        }
    }
}
