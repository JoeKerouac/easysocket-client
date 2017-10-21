package com.joe.test.easysocket.client;

import com.joe.easysocket.client.Client;
import com.joe.easysocket.client.core.EventListener;
import com.joe.easysocket.client.ext.EventListenerAdapter;
import com.joe.easysocket.client.data.InterfaceData;
import com.joe.easysocket.client.ext.Logger;
import com.joe.easysocket.client.ext.Serializer;
import com.joe.parse.json.JsonObject;
import com.joe.parse.json.JsonParser;

/**
 * @author joe
 */
public class ClientTest {
    private static final JsonParser parser = JsonParser.getInstance();

    public static void main(String[] args) {
        //序列化器
        Serializer serializer = new Serializer() {
            @Override
            public byte[] write(Object obj) {
                return parser.toJson(obj).getBytes();
            }

            @Override
            public <T> T read(byte[] data, Class<T> clazz) {
                return parser.readAsObject(data, clazz);
            }
        };

        //日志
        Logger logger = new Logger() {
            @Override
            public void debug(String msg) {
                System.out.println(msg);
            }

            @Override
            public void info(String msg) {
                System.out.println(msg);
            }

            @Override
            public void warn(String msg) {
                System.out.println(msg);
            }

            @Override
            public void error(String msg) {
                System.out.println(msg);
            }

            @Override
            public void debug(String flag, String msg) {
                System.out.println(flag + ":" + msg);
            }

            @Override
            public void info(String flag, String msg) {
                System.out.println(flag + ":" + msg);
            }

            @Override
            public void warn(String flag, String msg) {
                System.out.println(flag + ":" + msg);
            }

            @Override
            public void error(String flag, String msg) {
                System.out.println(flag + ":" + msg);
            }
        };

        EventListener listener = new EventListenerAdapter() {
            @Override
            public void faild(Throwable cause) {
                System.out.println("faild");
            }

            @Override
            public void register(Client client) {
                System.out.println("register");
            }

            @Override
            public void reconnect(Client client) {
                System.out.println("reconnect");
            }

            @Override
            public void unregister() {
                System.out.println("unregister");
            }

            @Override
            public void receive(InterfaceData data) {
                System.out.println("receive:" + data);
            }
        };

        //构建client对象，其中logger和listener都是非必须的，但是没有listener就无法处理服务器消息，所以正式使用时该对象必须有
        Client client = Client.builder().heartbeat(30).host("127.0.0.1").port(10051).serializer(serializer).listener
                (listener).logger
                (logger).build();
        client.start();
        JsonObject object = new JsonObject().data("openid", 120).data("account", "456").data("password", "789");
        client.write("user/login", object.toJson());
    }
}
