package br.edu.utfpr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {
            if (!Files.isDirectory(path)) {
                throw new UnsupportedOperationException("Comando exclusivo para abertura de pastas.");
            } else
                Files.list(Paths.get(path.toUri()))
                        .map(Path::getFileName)
                        .forEach(System.out::println);
            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) throws IOException {
            path = Path.of(path.toString() + File.separator + parameters[1]);
            if(!Files.exists(path)){
                throw new UnsupportedOperationException("Arquivo não encontrado.");
            } else FileReader.read(path);
            return path.getParent();
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {
            if (path.equals(Path.of(FileSystem.ROOT))) {
                throw new UnsupportedOperationException("Já no diretório raiz.");
            } else path = path.getParent();
            new File(path.toUri());
            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {
            path = Path.of(path.toString() + File.separator + parameters[1]);
            if (!Files.isDirectory(path)) {
                throw new UnsupportedOperationException("Comando exclusivo para abertura de pastas.");
            } else new File(path.toUri());
            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) throws IOException {
            Path file = Path.of(path.toString() + File.separator + parameters[1]);
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.printf("""
                            Creation time: %s
                            Last modified: %s
                            Last access: %s
                            Is directory: %s
                            Is regular file: %s\s
                            Is symbolic link: %s
                            Is other: %s\s
                            Size: %s bytes""",
                    attributes.creationTime(), attributes.lastModifiedTime(),
                    attributes.lastAccessTime(), attributes.isDirectory(), attributes.isRegularFile(),
                    attributes.isSymbolicLink(), attributes.isOther(), attributes.size());
            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...\n");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }


}
