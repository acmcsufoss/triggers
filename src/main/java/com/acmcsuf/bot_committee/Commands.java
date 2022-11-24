package com.acmcsuf.bot_committee;

public final class Commands
{

    // Command List
    public static final String TRIGGER = "trigger";
    public static final String TRIGGER_DESCRIPTION = "Receive a DM when trigger word is mentioned in mutual servers";

    // Subcommands
    public static final String TRIGGER_HELP = "help";
    public static final String TRIGGER_HELP_DESCRIPTION = "Lists all trigger commands";

    public static final String TRIGGER_RESET = "reset";
    public static final String TRIGGER_RESET_DESCRIPTION = "Resets all stored triggers";

    public static final String TRIGGER_LIST = "list";
    public static final String TRIGGER_LIST_DESCRIPTION = "Lists all stored triggers";

    public static final String TRIGGER_NEW = "new";
    public static final String TRIGGER_NEW_DESCRIPTION = "Add a new trigger";

    public static final String TRIGGER_DELETE = "delete";
    public static final String TRIGGER_DELETE_DESCRIPTION = "Delete a stored trigger";

    public static final String TRIGGER_TOGGLE = "toggle";
    public static final String TRIGGER_TOGGLE_DESCRIPTION = "Toggles trigger feature";

    private Commands()
    {

    }

}