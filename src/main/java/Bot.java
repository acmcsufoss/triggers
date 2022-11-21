import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Bot {

    public static void main(String[] args) throws InterruptedException {

        // Loads .env file and stores all values into system properties
        Dotenv dotenv = Dotenv.configure()
                .systemProperties()
                .load();

        // Bot Token
        String token = System.getProperty("DISCORD_TOKEN");

        // List of authorized roles
        List<String> authorizedRoleIDs = Arrays.asList(System.getProperty("AUTHORIZED_ROLE_ID").split(","));

        // JDA Builder
        JDA jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)

                // Event listeners (new instances of other classes extending ListenerAdapter)
                .addEventListeners(new Register(), new Trigger(authorizedRoleIDs))

                .build()
                .awaitReady();

        LoggerFactory.getLogger(Bot.class).info(jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator());

        // Status
        jda.getPresence().setActivity( Activity.listening("/trigger help"));
    }
}
