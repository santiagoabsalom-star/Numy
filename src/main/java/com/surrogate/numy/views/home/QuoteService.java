package com.surrogate.numy.views.home;

import com.vaadin.flow.component.html.Span;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private final String[] quotes= {"“I have no idea what to do with myself. And while I wait for my epiphany, I feel the toxins collecting in my body.”", "“Freedom without any purpose feels a whole lot like boredom.” ", "“I wondered if the Demon that whispered \"Why not be free?\" was Freedom itself.” ","\n" +
            "“Happiness\" describes moments, and it's never permanent.” ","\n" +
            "“The world is drowning in weirdness and lies......and here we are, so used to it that we're actually bored!” ", };

    @Scheduled(cron="0 0 0 * * *")
    public void crontab() {
        loadQuote();
    }

    public void loadQuote() {
        int index = (int) (Math.random() * quotes.length);
        String nuevaFrase = quotes[index];


        QuoteBroadcaster.broadcast(nuevaFrase);
    }
    public void loadFirstQuote(Span quote) {
        int index = (int) (Math.random() * quotes.length);
        String nuevaFrase = quotes[index];
        quote.setText(nuevaFrase);
    }
}
