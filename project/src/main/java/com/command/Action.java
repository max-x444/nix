package com.command;

public enum Action {
    CREATE(new Create()),
    READ(new Read()),
    UPDATE(new Update()),
    DELETE(new Delete()),
    EXIT(null);

    private final Command command;

    Action(Command command) {
        this.command = command;
    }

    public Command execute() {
        if (command != null) {
            command.execute();
        }
        return command;
    }
}
