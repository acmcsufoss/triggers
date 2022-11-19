import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Trigger extends ListenerAdapter {

    HashMap<String, String> triggerMap = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equals("trigger") || !event.isFromGuild()) return;

        switch (event.getSubcommandName()) {
            case "new" -> {

                // Takes string result of option ID matching "word"
                String trigger_phrase = event.getOption("word").getAsString().toLowerCase();

                event.reply("trigger set: \"" + trigger_phrase + "\"").setEphemeral(true).queue();
                triggerMap.put(event.getMember().getId(), trigger_phrase);
            }
            case "reset" -> {

                // Trigger found for member
                if (triggerMap.containsKey(event.getMember().getId())) {
                    triggerMap.remove(event.getMember().getId());
                    event.reply("trigger reset").setEphemeral(true).queue();
                }
                // Member has no trigger
                else {
                    event.reply("no trigger found").setEphemeral(true).queue();
                }

            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

        // Remove leaving member from trigger map
        if (triggerMap.containsKey(event.getMember().getId())) {
            triggerMap.remove(event.getMember().getId());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // Only listen to guild messages from live users
        if (!event.isFromGuild() || event.getMember().getUser().isBot()) return;

        String message_content = event.getMessage().getContentRaw().toLowerCase();

        // Loop through HashMap keySet
        for (String id : triggerMap.keySet()) {

            // If members value contains message_content
            if (message_content.contains(triggerMap.get(id))) {

                // Retrieve triggered member
                RestAction<Member> action = event.getGuild().retrieveMemberById(id);
                action.queue((null),

                    // Handle failure if the member does not exist (or another issue appeared)
                    (error) -> {
                        LoggerFactory.getLogger(Trigger.class).info(error.toString());
                    }
                );
                Member member = event.getGuild().getMemberById(id);

                // Skip if message is self-triggered or member is missing view permissions
                if (event.getMember() == member || !member.hasPermission(event.getGuildChannel(), Permission.VIEW_CHANNEL)) continue;

                // Embed
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Message Trigger")
                        .setColor(Color.green)
                        .setFooter("All timestamps are formatted in PST / UTC+7 !");

                // Retrieve last 4 messages in channel message history
                MessageHistory history = event.getChannel().getHistoryBefore(event.getMessageId(), 4).complete();
                List<String> messages = new ArrayList<>();

                // Add messages to list and reverse messages in order of least -> most recent
                for (Message message : history.getRetrievedHistory()) {
                    String member_name = message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator();
                    messages.add("**[" +  TimeFormat.TIME_LONG.atTimestamp(message.getTimeCreated().toEpochSecond()*1000) + "] " + member_name + ":** " + message.getContentRaw() + "\n");
                }
                Collections.reverse(messages);

                // Add trigger message
                String trigger_member = event.getMessage().getAuthor().getName() + "#" + event.getMessage().getAuthor().getDiscriminator();
                builder.addField("", "**[" + TimeFormat.TIME_LONG.now() + "] " + trigger_member + ":** " + event.getMessage().getContentRaw(), false);

                // Finish embed
                builder.setDescription(String.join("", messages));
                builder.addField("**Source Message**", "[Jump to](" + event.getJumpUrl() + ")" , false);

                // DM triggered member
                member.getUser().openPrivateChannel()
                        .flatMap(channel -> channel.sendMessageEmbeds(builder.build()).addActionRow(
                                Button.secondary("server-id", "Server: " + event.getGuild().getName()).asDisabled()
                        ))
                        .queue();
            }
        }
    }
}
