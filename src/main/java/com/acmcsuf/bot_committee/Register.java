package com.acmcsuf.bot_committee;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter
{

    @Override
    public void onGenericEvent( @NotNull GenericEvent event )
    {

        // Register commands (#updateCommands will CLEAR all commands, don't do this more than once per startup)
        updateCommands( event );
    }

    /**
     * Updates bot commands in guild
     *
     * @param event GuildReadyEvent or GuildJoinEvent
     */
    private void updateCommands( GenericEvent event )
    {

        Guild guild;

        if ( event instanceof GuildReadyEvent guildReadyEvent )
        {
            guild = guildReadyEvent.getGuild();
        }
        else if ( event instanceof GuildJoinEvent guildJoinEvent )
        {
            guild = guildJoinEvent.getGuild();
        }
        else
        {
            return;
        }

        guild.updateCommands().addCommands( guildCommands() )
                .queue( ( null ), ( ( error ) -> LoggerFactory.getLogger( Bot.class )
                        .info( "Failed to update commands for " + guild.getName() + " (" + guild.getId() + ")" ) ) );
    }

    /**
     * Guild Commands List
     * <p>
     * All commands intended ONLY for guild usage are returned in a List
     * </p>
     *
     * @return List containing bot commands
     */
    private List<CommandData> guildCommands()
    {

        // List holding all guild commands
        List<CommandData> guildCommandData = new ArrayList<>();

        // Trigger command + subcommands
        SubcommandData help = new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_HELP,
                com.acmcsuf.bot_committee.Commands.TRIGGER_HELP_DESCRIPTION );
        SubcommandData reset = new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_RESET,
                com.acmcsuf.bot_committee.Commands.TRIGGER_RESET_DESCRIPTION );
        SubcommandData list = new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_LIST,
                com.acmcsuf.bot_committee.Commands.TRIGGER_LIST_DESCRIPTION );
        SubcommandData toggle =
                new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_TOGGLE,
                        com.acmcsuf.bot_committee.Commands.TRIGGER_TOGGLE_DESCRIPTION )
                        .addOption( OptionType.BOOLEAN, "switch", "Toggles feature", true );
        SubcommandData newTrigger =
                new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_NEW,
                        com.acmcsuf.bot_committee.Commands.TRIGGER_NEW_DESCRIPTION )
                        .addOption( OptionType.STRING, "word", "Trigger word", true );
        SubcommandData delete =
                new SubcommandData( com.acmcsuf.bot_committee.Commands.TRIGGER_DELETE,
                        com.acmcsuf.bot_committee.Commands.TRIGGER_DELETE_DESCRIPTION )
                        .addOption( OptionType.STRING, "word", "Trigger word", true, true );
        guildCommandData.add(
                Commands.slash( com.acmcsuf.bot_committee.Commands.TRIGGER,
                                com.acmcsuf.bot_committee.Commands.TRIGGER_DESCRIPTION )
                        .addSubcommands( help, reset, list, toggle, newTrigger, delete ) );

        return guildCommandData;
    }
}