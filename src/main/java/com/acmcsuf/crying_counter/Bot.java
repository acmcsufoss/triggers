package com.acmcsuf.crying_counter;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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

    public static void main( String[] args ) throws InterruptedException
    {

        // Bot Token
        final String token = System.getenv( "DISCORD_TOKEN" );

        // SLF4J Logger
        final Logger log = LoggerFactory.getLogger( Bot.class );

        // List of authorized roles
        final List<String> authorizedRoleIDs = Arrays.asList( System.getenv( "AUTHORIZED_ROLE_ID" ).split( "," ) );

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

        try
        {
            Database.initializeDatabase();
        }
        catch ( SQLException e )
        {
            log.error( "Failed to initialize database" );
            e.printStackTrace();
        }
    }
}