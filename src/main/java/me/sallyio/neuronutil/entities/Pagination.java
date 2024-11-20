package me.sallyio.neuronutil.entities;

import me.sallyio.neuronutil.core.PaginationManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pagination {
    private final String title;
    private final Integer dataPerPage;
    private List<PaginationData<? extends String>> paginationData = new ArrayList<>();
    private int currentPage;

    public Pagination(String title, Integer dataPerPage) {
        this.title = title;
        this.dataPerPage = dataPerPage;
        this.currentPage = 1;
    }

    public Pagination(String title) {
        this.title = title;
        this.dataPerPage = 10;
        this.currentPage = 1;
    }

    public Pagination addData(PaginationData<?> data) {
        this.paginationData.add(data);
        return this;
    }

    public Pagination addData(String title, String data) {
        this.addData(new PaginationData<>(title,data));
        return this;
    }

    public Pagination setData(List<PaginationData<? extends String>> data) {
        this.paginationData = data;
        return this;
    }

    private List<PaginationData<? extends String>> getCurrentPageData() {
        int untilItem = currentPage * this.dataPerPage;
        int fromItem = untilItem - this.dataPerPage;

        if (currentPage == 1) {
            fromItem = 0;
            untilItem = this.dataPerPage;
        }

        if (currentPage == (int) Math.ceil((double) this.paginationData.size() / this.dataPerPage)) {
            fromItem = currentPage * this.dataPerPage - this.dataPerPage;
            untilItem = this.paginationData.size();
        }

        return this.paginationData.subList(fromItem, untilItem);
    }

    private boolean isLastPage() {
        return currentPage == (paginationData.size() / dataPerPage) + 1;
    }

    private MessageEmbed renderEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(this.title);
        List<PaginationData<? extends String>> data = this.getCurrentPageData();
        data.forEach((v) -> {
            builder.addField(v.title(), v.value(), false);
        });
        builder.setColor(0xFFFFFF);
        return builder.build();
    }

    private Collection<ItemComponent> renderComponent() {
        Button firstPage = Button.primary(PaginationManager.GO_TO_FIRST_PAGE, "|<");
        Button previous = Button.primary(PaginationManager.GO_TO_PREVIOUS, "<");
        Button nextPage = Button.primary(PaginationManager.GO_TO_NEXT_PAGE, ">");
        Button lastPage = Button.primary(PaginationManager.GO_TO_LAST_PAGE, ">|");
        if (currentPage == 1) {
            firstPage = firstPage.withStyle(ButtonStyle.SECONDARY).asDisabled();
            previous = previous.withStyle(ButtonStyle.SECONDARY).asDisabled();
        }
        if (isLastPage()) {
            nextPage = nextPage.withStyle(ButtonStyle.SECONDARY).asDisabled();
            lastPage = lastPage.withStyle(ButtonStyle.SECONDARY).asDisabled();
        }
        return List.of(firstPage, previous, nextPage, lastPage);
    }

    public MessageCreateData renderMessage() {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        builder.setEmbeds(this.renderEmbed());
        builder.addActionRow(this.renderComponent());
        return builder.build();
    }

    public void nextPage() {
        this.currentPage = this.currentPage + 1;
    }

    public void firstPage() {
        this.currentPage = 1;
    }

    public void previousPage() {
        this.currentPage = currentPage - 1;
    }

    public void lastPage() {
        this.currentPage = (paginationData.size() / dataPerPage) + 1;
    }

    public record PaginationData<T extends String>(String title, T value) {
    }
}
