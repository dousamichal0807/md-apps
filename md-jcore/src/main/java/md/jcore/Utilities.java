package md.jcore;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import md.jcore.debug.Debugger;

public final class Utilities {
	public static final Attributes.Name JAR_MANIFEST_IMPL_VERSION = new Attributes.Name("Implementation-Version");

	private Utilities() {
		// Do not create any instances
	}

	private static String jCoreVersion;

	public static Path getCWD(final Class<?> cwdClass) {
		try {
			Path cwd = Paths.get(cwdClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			cwd = Paths.get("/").resolve(cwd.subpath(0, cwd.getNameCount() - 1));
			return cwd;
		} catch (URISyntaxException exc) {
			StringWriter msg = new StringWriter();
			msg.write("getCurrentWorkingDirectory() encountered an unexpected error:\n");
			exc.printStackTrace(new PrintWriter(msg));
			Debugger.error(Utilities.class, msg);
			return null;
		}
	}

	public static Attributes getJARManifestData(final Class<?> clazz) {
		try (InputStream inputStream = clazz.getResourceAsStream("/META-INF/MANIFEST.MF")) {
			Manifest manifest = new Manifest(inputStream);
			return manifest.getMainAttributes();
		} catch(IOException exc) {
			StringWriter msg = new StringWriter();
			msg.write("getMainfest() encountered an unexpected error:\n");
			exc.printStackTrace(new PrintWriter(msg));
			Debugger.error(Utilities.class, msg);
			return null;
		}
	}

	public static String jCoreVersion() {
		if(jCoreVersion == null)
			jCoreVersion = getVersion(Utilities.class);
		return jCoreVersion;
	}

	public static String getVersion(final Class<?> clazz) {
		return getJARManifestData(clazz).getValue(Utilities.JAR_MANIFEST_IMPL_VERSION);
	}
}
