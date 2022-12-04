package com.acmcsuf.crying_counter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
     * Checks if a user is registered in the database.
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

        try ( ResultSet set = getConnect().createStatement().executeQuery( sql ) )
        {
            return set.next();
        }
    }

    /**
     * Checks if a user is registered in the database.
     *
     * @param userID Event member ID
     * @return User is registered
     * @throws SQLException On failure to interact with database
     */
    public static boolean isStoredUser( String userID ) throws SQLException
    {
        String sql = String.format( """
                SELECT user_id
                FROM triggers
                WHERE user_id = %s;""", userID );

        try ( ResultSet set = getConnect().createStatement().executeQuery( sql ) )
        {
            return set.next();
        }
    }
}
