package md.jgames.jchess.io;

public class NotUCIEngineException extends RuntimeException {
    private final Process process;

    public NotUCIEngineException(final Process process) {
        this.process = process;
    }

    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder(100);
        message.append("Not a UCI chess engine process: [PID: ");
        // Append PID
        message.append(process.pid());
        message.append("(0x");
        message.append(Long.toHexString(process.pid()));
        message.append(")");
        // Append command, if avalilable
        if (process.info().commandLine().isPresent()) {
            message.append(", command line: \"");
            message.append(process.info().commandLine().get());
            message.append("\"");
        }
        // Ending square bracket ']'
        message.append("]");
        // Return message
        return message.toString();
    }
}
