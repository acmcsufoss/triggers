import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        // List holding all guild commands
        List<CommandData> guildCommandData = new ArrayList<>();

        // Trigger command with subcommands "reset" and "new_trigger"
        SubcommandData reset = new SubcommandData("reset", "Removes trigger");
        SubcommandData new_trigger = new SubcommandData("new", "Add/Replace trigger").addOption(OptionType.STRING, "word", "Trigger word", true);
        guildCommandData.add(Commands.slash("trigger", "Receive a DM when trigger word is mentioned in mutual servers").addSubcommands(new_trigger, reset));

        // Register commands (#updateCommands will CLEAR all commands, don't do this more than once per startup)
        event.getJDA().updateCommands().addCommands(guildCommandData).queue();
    }
}
