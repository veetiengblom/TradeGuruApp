package com.example.tradeguruapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MentorQuotes {
    private static List<String> bjornWahlroosQuotes;
    private static List<String> elonMuskQuotes;
    private static List<String> jordanBelfortQuotes;
    private static List<String> wallStreetBetsQuotes;
    private static List<String> warrenBuffettQuotes;
    private static Random random;

    public MentorQuotes() {
        bjornWahlroosQuotes = new ArrayList<>();
        bjornWahlroosQuotes.add("Investing is about patience, not about quick gains.");
        bjornWahlroosQuotes.add("Diversify your portfolio, and you'll weather any storm.");
        bjornWahlroosQuotes.add("Financial discipline is the key to long-term success.");
        bjornWahlroosQuotes.add("A well-researched decision is better than a hasty one.");
        bjornWahlroosQuotes.add("Don't follow the crowd; make your own investment choices.");
        bjornWahlroosQuotes.add("Success in finance requires a calm mind and steady hand.");
        bjornWahlroosQuotes.add("Never invest in something you don't understand.");
        bjornWahlroosQuotes.add("Take calculated risks, not reckless ones.");
        bjornWahlroosQuotes.add("The stock market is a reflection of human nature.");
        bjornWahlroosQuotes.add("Learn from your mistakes, but don't dwell on them.");
        bjornWahlroosQuotes.add("The best investment you can make is in yourself.");
        bjornWahlroosQuotes.add("Focus on the long-term, and short-term fluctuations won't bother you.");
        bjornWahlroosQuotes.add("Economic cycles are a fact of life; don't panic during downturns.");
        bjornWahlroosQuotes.add("In finance, slow and steady wins the race.");

        elonMuskQuotes = new ArrayList<>();
        elonMuskQuotes.add("Mars is looking pretty good right now.");
        elonMuskQuotes.add("Innovation is the path to the future.");
        elonMuskQuotes.add("Electric cars are just the beginning.");
        elonMuskQuotes.add("Dream big, work hard, and never give up.");
        elonMuskQuotes.add("We're going to make life multi-planetary.");
        elonMuskQuotes.add("When something is important enough, you do it even if the odds are not in your favor.");
        elonMuskQuotes.add("Failure is an option here. If things are not failing, you are not innovating enough.");
        elonMuskQuotes.add("Great companies are built on great products.");
        elonMuskQuotes.add("If you get up in the morning and think the future is going to be better, it is a bright day. Otherwise, it's not.");
        elonMuskQuotes.add("I think it is possible for ordinary people to choose to be extraordinary.");
        elonMuskQuotes.add("The first step is to establish that something is possible; then probability will occur.");
        elonMuskQuotes.add("Some people don't like change, but you need to embrace change if the alternative is disaster.");
        elonMuskQuotes.add("When Henry Ford made cheap, reliable cars, people said, 'Nah, what's wrong with a horse?' That was a huge bet he made, and it worked.");
        elonMuskQuotes.add("I could either watch it happen or be a part of it.");
        elonMuskQuotes.add("You want to be extra rigorous about making the best possible thing you can. Find everything that's wrong with it and fix it. Seek negative feedback, particularly from friends.");

        jordanBelfortQuotes = new ArrayList<>();
        jordanBelfortQuotes.add("The only thing standing between you and your goal is the story you keep telling yourself.");
        jordanBelfortQuotes.add("Sell me this pen.");
        jordanBelfortQuotes.add("Money doesn't buy happiness, but it sure makes misery easier to live with.");
        jordanBelfortQuotes.add("Stay hungry, stay foolish, stay out of jail.");
        jordanBelfortQuotes.add("Work until your bank account looks like a phone number.");
        jordanBelfortQuotes.add("The easiest way to make money is - create something of such value that everybody wants and go out and give and create value, the money comes automatically.");
        jordanBelfortQuotes.add("Winners use words that say 'must' and 'will.'");
        jordanBelfortQuotes.add("The only thing standing between you and your goal is the story you keep telling yourself.");
        jordanBelfortQuotes.add("Successful people are 100% convinced that they are masters of their own destiny, they’re not creatures of circumstance, they create circumstance.");
        jordanBelfortQuotes.add("No matter what happened to you in your past, you are not your past, you are the resources and the capabilities you glean from it. And that is the basis for all change.");
        jordanBelfortQuotes.add("Act as if! Act as if you’re a wealthy man, rich already, and then you’ll surely become rich.");
        jordanBelfortQuotes.add("Money is the oxygen of capitalism and I wanna breathe more than any human being alive.");
        jordanBelfortQuotes.add("The only thing standing between you and your goal is the story you keep telling yourself.");
        jordanBelfortQuotes.add("Without action, the best intentions in the world are nothing more than that: intentions.");
        jordanBelfortQuotes.add("The only thing standing between you and your goal is the story you keep telling yourself.");

        wallStreetBetsQuotes = new ArrayList<>();
        wallStreetBetsQuotes.add("YOLO! All in on [stock]!");
        wallStreetBetsQuotes.add("Diamond hands only! 💎🙌");
        wallStreetBetsQuotes.add("To the moon!");
        wallStreetBetsQuotes.add("Hold the line, fellow apes!");
        wallStreetBetsQuotes.add("Stonks only go up!");
        wallStreetBetsQuotes.add("Buy high, sell higher!");
        wallStreetBetsQuotes.add("Do your own due diligence.");
        wallStreetBetsQuotes.add("The trend is your friend.");
        wallStreetBetsQuotes.add("I'm not a financial advisor, but...");
        wallStreetBetsQuotes.add("Options trading is like gambling, but with more steps.");
        wallStreetBetsQuotes.add("YOLOing my life savings.");
        wallStreetBetsQuotes.add("I'd rather go broke than sell.");
        wallStreetBetsQuotes.add("We're all in this together!");
        wallStreetBetsQuotes.add("This is the way.");

        warrenBuffettQuotes = new ArrayList<>();
        warrenBuffettQuotes.add("The stock market is designed to transfer money from the impatient to the patient.");
        warrenBuffettQuotes.add("Risk comes from not knowing what you're doing.");
        warrenBuffettQuotes.add("Buy businesses, not stocks.");
        warrenBuffettQuotes.add("Our favorite holding period is forever.");
        warrenBuffettQuotes.add("Price is what you pay; value is what you get.");
        warrenBuffettQuotes.add("It's far better to buy a wonderful company at a fair price than a fair company at a wonderful price.");
        warrenBuffettQuotes.add("Someone's sitting in the shade today because someone planted a tree a long time ago.");
        warrenBuffettQuotes.add("The difference between successful people and really successful people is that really successful people say no to almost everything.");
        warrenBuffettQuotes.add("The best investment you can make is in yourself.");
        warrenBuffettQuotes.add("Opportunities come infrequently. When it rains gold, put out the bucket, not the thimble.");
        warrenBuffettQuotes.add("Risk comes from not knowing what you're doing.");
        warrenBuffettQuotes.add("Honesty is a very expensive gift. Don't expect it from cheap people.");
        warrenBuffettQuotes.add("The most important quality for an investor is temperament, not intellect.");
        warrenBuffettQuotes.add("The chains of habit are too light to be felt until they are too heavy to be broken.");
        warrenBuffettQuotes.add("In the business world, the rearview mirror is always clearer than the windshield.");

        random = new Random();
    }

    public static String getRandomQuote(String mentor) {
        List<String> mentorQuotes = getMentorQuotes(mentor);
        if (mentorQuotes == null) {
            return "Mentor not found.";
        }
        int randomIndex = random.nextInt(mentorQuotes.size());
        return mentorQuotes.get(randomIndex);
    }

    private static List<String> getMentorQuotes(String mentor) {
        switch (mentor.toLowerCase()) {
            case "björn wahlroos":
                return bjornWahlroosQuotes;
            case "elon musk":
                return elonMuskQuotes;
            case "jordan belfort":
                return jordanBelfortQuotes;
            case "wallstreetbets":
                return wallStreetBetsQuotes;
            case "warren buffett":
                return warrenBuffettQuotes;
            default:
                return null;
        }
    }
}


