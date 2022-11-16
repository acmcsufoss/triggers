import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Trigger extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equals("trigger") || !event.isFromGuild()) return;

        switch (event.getSubcommandName()) {
            case "new" -> {

                // Takes option ID matching "word"
                String trigger_phrase = event.getOption("word").getAsString().toLowerCase();

                break;
            }
            case "replace" -> {
                break;
            }
        }
    }
}
