package me.sallyio.neuronutil.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.JDALogger;

public class Client {
    private JDABuilder jdaBuilder;
    public Client(String token, String[] arg)
    {
        JDALogger.setFallbackLoggerEnabled(false);
        jdaBuilder = JDABuilder.createLight(token)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES
                )
                .setEventPassthrough(true);
    }
    public Client addEventListeners(ListenerAdapter adapter) {
        this.jdaBuilder.addEventListeners(adapter);
        return this;
    }
    public JDA start() {
        return jdaBuilder.build();
    }

}