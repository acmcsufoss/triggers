package com.acmcsuf.triggers;

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
    public static final String TRIGGER_NEW_OPTION_NAME = "word";
    public static final String TRIGGER_NEW_OPTION_DESCRIPTION = "Trigger word";

    public static final String TRIGGER_DELETE = "delete";
    public static final String TRIGGER_DELETE_DESCRIPTION = "Delete a stored trigger";
    public static final String TRIGGER_DELETE_OPTION_NAME = "word";
    public static final String TRIGGER_DELETE_OPTION_DESCRIPTION = "Trigger word";

    public static final String TRIGGER_TOGGLE = "toggle";
    public static final String TRIGGER_TOGGLE_OPTION_NAME = "switch";
    public static final String TRIGGER_TOGGLE_OPTION_DESCRIPTION = "Toggles feature";
    public static final String TRIGGER_TOGGLE_DESCRIPTION = "Toggles trigger feature";

    public static final String VIEW = "view";
    public static final String VIEW_DESCRIPTION = "View a members triggers";
    public static final String VIEW_OPTION_NAME = "user";
    public static final String VIEW_OPTION_DESCRIPTION = "Member to view";

    private Commands()
    {

    }

}