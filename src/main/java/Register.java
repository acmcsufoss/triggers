import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {

        // Register commands (#updateCommands will CLEAR all commands, don't do this more than once per startup)
        updateCommands(event);
    }

    /**
     * Updates bot commands in guild
     * @param event GuildReadyEvent or GuildJoinEvent
     */
    private void updateCommands(GenericEvent event) {

        if (event instanceof GuildReadyEvent guildReadyEvent) {
            guildReadyEvent.getGuild().updateCommands().addCommands(guildCommands()).queue((null), (null));
        }
        else if (event instanceof GuildJoinEvent guildJoinEvent) {
            guildJoinEvent.getGuild().updateCommands().addCommands(guildCommands()).queue((null), (null));
        }
    }

    /**
     * Guild Commands List
     * <p>
     *     All commands intended ONLY for guild usage are returned in a List
     * </p>
     * @return List containing bot commands
     */
    private List<CommandData> guildCommands() {

        // List holding all guild commands
        List<CommandData> guildCommandData = new ArrayList<>();

        // Trigger command with subcommands "reset" and "new_trigger"
        SubcommandData reset = new SubcommandData("reset", "Removes trigger");
        SubcommandData list = new SubcommandData("list", "Lists triggers");
        SubcommandData new_trigger = new SubcommandData("new", "Add/Replace trigger").addOption(OptionType.STRING, "word", "Trigger word", true);
        guildCommandData.add(Commands.slash("trigger", "Receive a DM when trigger word is mentioned in mutual servers").addSubcommands(new_trigger, list, reset));

        return guildCommandData;
    }
}
