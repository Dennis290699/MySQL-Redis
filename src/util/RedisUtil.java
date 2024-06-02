package util;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    private static Jedis jedis;

    static {
        jedis = new Jedis(HOST, PORT);
    }

    public static Jedis getConnection() {
        return jedis;
    }

    public static void closeConnection() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
