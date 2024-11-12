package me.sallyio.neuronutil.commands;

import me.sallyio.neuronutil.common.SystemInfo;
import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

public class Sysinfo extends BaseCommand {
    public Sysinfo() {
        this
                .setName("sysinfo")
                .setDescription("no")
                .sallyOnly();
    }

    @Override
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        EmbedBuilder mainEmbed = new EmbedBuilder();
        SystemInfo systemInfo = new SystemInfo();
        mainEmbed.addField("Cpu",""
                + "Cpu name: " + systemInfo.getCpuName() + "\n"
                + "Cpu usage: " + systemInfo.getCpuUsage() + "% / 100%\n",
                false
        );
        mainEmbed.addField("Ram & Disk", ""
                + "Ram usage: " + systemInfo.getCurrentRamUsage() + " MB / " + systemInfo.getTotalRam() + " MB\n"
                + "Disk usage: " + systemInfo.getUsedDiskSpace() + " GB / " + systemInfo.getTotalDiskSpace() + " GB\n",
                false
        );
        mainEmbed.addField("Process", ""
                + "Process PID: " + systemInfo.getCurrentPid() + "\n"
                + "Java Version: " + Runtime.version().toString() + "\n",
                false
        );
        mainEmbed.setColor(0xFFFFFF);
        builder.addEmbeds(mainEmbed.build());
        event.getMessage().reply(builder.build()).queue();
    }
}
