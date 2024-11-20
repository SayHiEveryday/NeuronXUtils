package me.sallyio.neuronutil.core;

import me.sallyio.neuronutil.entities.Pagination;
import me.sallyio.neuronutil.entities.cache.PaginationCache;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.util.concurrent.TimeUnit;

public class PaginationManager extends ListenerAdapter {
    public static final String PAGINATION_PREFIX = "page:";
    public static final String GO_TO_FIRST_PAGE = PAGINATION_PREFIX + "first";
    public static final String GO_TO_PREVIOUS = PAGINATION_PREFIX + "previous";
    public static final String GO_TO_NEXT_PAGE = PAGINATION_PREFIX + "next";
    public static final String GO_TO_LAST_PAGE = PAGINATION_PREFIX + "last";

    public static PaginationCache cache = PaginationCache.getInstance();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Pagination page = cache.getWithDefault(event.getMessage().getId(), null);
        if (page == null) {
            event.editMessage("Sorry this page is expired").setReplace(true).queue();
            event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
            return;
        }

        if (!event.getButton().getId().contains(PAGINATION_PREFIX)) return;
        switch (event.getButton().getId()) {
            case GO_TO_FIRST_PAGE -> page.firstPage();
            case GO_TO_LAST_PAGE -> page.lastPage();
            case GO_TO_NEXT_PAGE -> page.nextPage();
            case GO_TO_PREVIOUS -> page.previousPage();
        }
        if (!event.isAcknowledged()) {
            event.editMessage(MessageEditBuilder.fromCreateData(page.renderMessage()).build()).setReplace(true).queue();
        } else {
            event.getHook().editMessageById(event.getMessageId(),MessageEditBuilder.fromCreateData(page.renderMessage()).build()).setReplace(true).queue();
        }
    }
}
