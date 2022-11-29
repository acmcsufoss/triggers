package com.acmcsuf.crying_counter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bot
{

    public static void main( String[] args ) throws InterruptedException, SQLException
    {

        // Loads .env file and stores all values into system properties
        final Dotenv dotenv = Dotenv.configure()
                .systemProperties()
                .load();

        // Bot Token
        final String token = System.getProperty( "DISCORD_TOKEN" );

        // SLF4J Logger
        final Logger log = LoggerFactory.getLogger( Bot.class );

        // List of authorized roles
        final List<String> authorizedRoleIDs = Arrays.asList( System.getProperty( "AUTHORIZED_ROLE_ID" ).split( "," ) );

        if ( authorizedRoleIDs.get( 0 ).equals( "" ) )
        {
            log.error( "No authorized roles found. Please add at least one role ID to the .env file." );
        }

        // Gateway Intents
        final List<GatewayIntent> gatewayIntents = Arrays.asList(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES
        );

        // JDA Builder
        final JDA jda = JDABuilder.createLight( token, gatewayIntents )
                .setStatus( OnlineStatus.ONLINE )
                .setMemberCachePolicy( MemberCachePolicy.ALL )

                // Event listeners (new instances of other classes extending ListenerAdapter)
                .addEventListeners( new Register(), new Trigger( authorizedRoleIDs ) )

                .build()
                .awaitReady();

        log.info( jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator() );

        // Status
        jda.getPresence().setActivity( Activity.listening( "/trigger help" ) );

        try {
            Properties props = new Properties();
            props.setProperty( "user", System.getProperty( "DATABASE_USER" ) );
            props.setProperty( "password", System.getProperty( "DATABASE_PASSWORD" ) );

            Connection db = DriverManager.getConnection(System.getProperty( "DATABASE_URL" ), props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}