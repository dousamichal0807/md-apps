package md.jcore.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import md.jcore.debug.Debugger;

/**
 * Represents a program process.
 * 
 * @author Michal Douša
 */
public final class ExecutableProcess extends Thread implements Closeable {
	private final File executablePath;
	private Process process;
	private BufferedReader reader;
	private BufferedWriter writer;
	private final ArrayList<Function<String, Boolean>> delegates;

	public ExecutableProcess(final File executablePath, final String name) {
		if (executablePath == null || name == null)
			throw new NullPointerException("Passed null as at least one parameter");
		if (!name.matches("[_A-Za-z][_0-9A-Za-z]*"))
			throw new IllegalArgumentException("Process name is illegal: '" + name + "'");
		this.executablePath = executablePath;
		this.delegates = new ArrayList<>();
		this.setName(name);
	}

	public ExecutableProcess(final Path executablePath, final String name) {
		this(executablePath.toFile(), name);
	}

	/**
	 * Starts the process.
	 * 
	 * @throws IllegalStateException when there's problem with accessing the
	 *                               executable or does not exist or process is
	 *                               already alive
	 */
	@Override
	public void start() {
		if (process != null && process.isAlive()) throw new IllegalStateException("Process already running");
		ProcessBuilder pb = new ProcessBuilder(executablePath.toString());
		// Paths.get(Utilities.getCurrentWorkingDirectory(getClass()).toString(),
		// p.toString()).toString());
		try {
			process = pb.start();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
			writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8));
			Debugger.info(getClass(), "Process started [" + getName() + "]");
		} catch (IOException exc) {
			throw new IllegalStateException(exc);
		}

		super.start();
	}

	@Override
	public void run() {
		while (process != null && process.isAlive()) {
			String nextLine = null;
			try {
				nextLine = reader.readLine();
				Debugger.info(getClass(), "Read line [" + getName() + "]: '" + nextLine + "'");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Iterator<Function<String, Boolean>> iterator = delegates.iterator();
			while (iterator.hasNext()) {
				Function<String, Boolean> next = iterator.next();
				if (!next.apply(nextLine))
					iterator.remove();
			}
		}
	}

	/**
	 * Kills the process and starts a new process again.
	 */
	public void restart() {
		close();
		start();
	}

	/**
	 * Shuts down the process.
	 */
	public void close() {
		if (process != null) if (process.isAlive()) {
			process.destroy();
			process = null;
			reader = null;
			writer = null;
		}
	}

	/**
	 * Sends a new command to the program. If this method does not satisfy your
	 * needs you can write directly to the writer.
	 * 
	 * @param command The command to be sent
	 * @return if the command was sent successfully
	 */
	public boolean send(final String command) {
		if (command == null)
			return false;
		try {
			writer.write(command + "\n");
			writer.flush();
			Debugger.info(getClass(), "Sent command [" + getName() + "]: '" + command + "'");
			return true;
		} catch (IOException exc) {
			return false;
		}
	}

	/**
	 * Allows reading the executable's output by using the
	 * {@code Function<String, Boolean>} delegate.
	 * 
	 * @param delegate Always accepts the next line of the program's output and
	 *                 returns a boolean if you want to read next line too.
	 */
	public void read(final Function<String, Boolean> delegate) {
		if (delegate == null)
			throw new NullPointerException();
		delegates.add(delegate);
	}
}
