package com.acmcsuf.crying_counter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;

import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database
{

    // Constants
    private static final String URL = System.getProperty( "DATABASE_URL" );
    private static final String USER = System.getProperty( "DATABASE_USER" );
    private static final String PASSWORD = System.getProperty( "DATABASE_PASSWORD" );

    // SLF4J Logger
    private static final Logger log = LoggerFactory.getLogger( Database.class );

    /**
     * Connects to database
     *
     * @return Connection object
     * @throws SQLException On failure to interact with database
     */
    private static Connection getConnect() throws SQLException
    {
        return DriverManager.getConnection( URL, USER, PASSWORD );
    }

    /**
     * Connects to and initializes database
     *
     * @throws SQLException On failure to interact with database
     */
    public static void initializeDatabase() throws SQLException
    {
        String sql = " CREATE TABLE IF NOT EXISTS triggers(user_id bigint, toggle boolean, phrase text[])";

        try ( Connection conn = getConnect() )
        {
            conn.createStatement().execute( sql );
        }

        log.info( "Connected to PostgreSQL server" );
    }

    /**
     * Appends new phrase to user's trigger ist
     * @param member Event member
     * @param phrase Phrase to add
     * @throws SQLException On failure to interact with database
     */
    public static void appendPhrase( Member member, String phrase ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET phrase = array_append(phrase, ?::text)
                WHERE user_id =""" + userID;

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setString( 1, phrase );
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Resets user's trigger list
     * @param member Event member
     * @throws SQLException On failure to interact with database
     */
    public static void resetTriggers( Member member ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET phrase = '{}'
                WHERE user_id = ?""";

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setLong( 1, Long.parseLong( userID ) );
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Toggle trigger response on/off
     * @param member Event member
     * @param toggle Toggle value
     * @throws SQLException On failure to interact with database
     */
    public static void toggleTrigger( Member member, boolean toggle ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET toggle = ?
                WHERE user_id = ?""";

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setBoolean( 1, toggle );
            preparedStatement.setLong( 2, Long.parseLong( userID ) );
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Checks if a user is registered in the database and inserts if not found
     *
     * @param member Event member
     * @return User is registered
     * @throws SQLException On failure to interact with database
     */
    public static boolean isStoredUser( Member member ) throws SQLException
    {
        String userID = member.getId();
        String sql = String.format( """
                SELECT user_id
                FROM triggers
                WHERE user_id = %s;""", userID );

        try ( Connection conn = getConnect() )
        {

            ResultSet set = conn.createStatement().executeQuery( sql );
            boolean found = false;

            // Insert user if not found
            if ( !set.next() )
            {
                String insert = String.format( """
                        INSERT INTO triggers(user_id, toggle, phrase)
                        VALUES(%s, TRUE, '{}');""", userID );

                conn.createStatement().execute( insert );
            }
            else
            {
                found = true;
            }

            conn.close();
            return found;
        }
    }

    public static void initializeIfNotExists( Member member ) throws SQLException
    {
        if ( !isStoredUser( member ) )
        {
            String userID = member.getId();
            String insert = String.format( """
                    INSERT INTO triggers(user_id, toggle, phrase)
                    VALUES(%s, TRUE, '{}');""", userID );

            try ( Connection conn = getConnect() )
            {
                conn.createStatement().execute( insert );
            }
        }
    }

    /**
     * Sync in-memory triggers with database
     * @param triggerMap In-memory trigger map
     */
    public static void syncMap( HashMap<String, LinkedHashSet<String>> triggerMap )
    {
        // TODO: Implement this
    }
}
