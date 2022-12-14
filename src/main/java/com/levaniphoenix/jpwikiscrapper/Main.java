package com.levaniphoenix.jpwikiscrapper;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.levaniphoenix.jpwikiscrapper.DAL.*;
import com.levaniphoenix.jpwikiscrapper.models.Kanji;
import com.levaniphoenix.jpwikiscrapper.models.Webpage;
import com.levaniphoenix.jpwikiscrapper.models.Word;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

@Component
public class Main implements CommandLineRunner {

    @Autowired
    WebpageRepository webpageRepository;

    @Autowired
    KanjiRepository kanjiRepository;

    @Autowired
    Neo4jKanjirepository neo4jKanjirepository;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    Neo4jWordRepository neo4jWordRepository;

    HashSet<Webpage> visitedLinks;

    WebDriver driver;

    Properties properties;

    Map<Character,Kanji> foundKanji;

    Map<String,Word> foundWords;

    @Override
    public void run(String... args) {

        Logger logger = Logger.getGlobal();
        //load visited Webpages
        visitedLinks = new HashSet<Webpage>(webpageRepository.findAll());

        initProperties();

        //init
        foundKanji = kanjiRepository.findAll().stream().collect(Collectors.toMap(Kanji::getKanji,Kanji::getThis));

        foundWords = wordRepository.findAll().stream().collect(Collectors.toMap(Word::getWord,Word::getThis));

        //init selenium driver
        //System.setProperty("webdriver.chrome.driver",properties.getProperty("webdriver.chrome.driver"));

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        driver = new ChromeDriver(options);



        Webpage targetWebsite  = new Webpage("https://ja.wikipedia.org/wiki/%E3%82%B3%E3%83%B3%E3%83%94%E3%83%A5%E3%83%BC%E3%82%BF%E3%82%B2%E3%83%BC%E3%83%A0");

        scrapWebpage(targetWebsite);


        driver.get(targetWebsite.getURL());
        ArrayList<String> links =  new ArrayList<>();

        for(WebElement link : driver.findElements(By.tagName("a"))){
            if(link.getAttribute("href")!=null)
                links.add(link.getAttribute("href"));
        }

        links = links.stream().filter((s)->{return s.contains("https://ja.wikipedia.org/wiki/") && !s.contains("#bodyContent");}).collect(Collectors.toCollection(ArrayList::new));

        Integer total = links.size();
        Integer counter = 0;

            for (String link : links) {
                try {
                    scrapWebpage(new Webpage(link));
                    logger.log(Level.INFO, "completed " + counter.toString() + " out of " + total.toString());
                    counter++;
                }catch (Exception e){
                    logger.log(Level.SEVERE,"Could not scrap the website: " + link);
                    e.printStackTrace();
                    driver.close();
                    driver = new ChromeDriver(options);
                }
            }
            kanjiRepository.saveAll(foundKanji.values());

            wordRepository.saveAll(foundWords.values());

            List<com.levaniphoenix.jpwikiscrapper.neo4jModels.Kanji> topKanji = foundKanji.values().stream().sorted(Comparator.comparingInt(Kanji::getOccurrence).reversed()).limit(100).map(com.levaniphoenix.jpwikiscrapper.neo4jModels.Kanji::new).toList();

            //List<Word> topWords = foundWords.values().stream().sorted(Comparator.comparingInt(Word::getOccurrence).reversed()).limit(200).toList();

            List<com.levaniphoenix.jpwikiscrapper.neo4jModels.Word>topWords = foundWords.values().stream().sorted(Comparator.comparingInt(Word::getOccurrence).reversed()).limit(2000).map(com.levaniphoenix.jpwikiscrapper.neo4jModels.Word::new).toList();


            counter  = 1;
            total = topKanji.size();

            //terribly slow code below

            for(com.levaniphoenix.jpwikiscrapper.neo4jModels.Kanji kanji : topKanji){
                kanji.setInWords(topWords.stream().filter(Word -> Word.getWord().contains(kanji.getKanji().toString())).toList());
                logger.log(Level.INFO,"completed " + counter + " kanji out of " + total);
                counter++;
                //neo4jKanjirepository.save(kanji);
            }

            neo4jKanjirepository.saveAll(topKanji);
            neo4jWordRepository.saveAll(topWords);

            driver.close();
    }

    public void scrapWebpage(Webpage targetWebsite){

        if(!visitedLinks.contains(targetWebsite)){

            visitedLinks.add(targetWebsite);

            driver.get(targetWebsite.getURL());

            WebElement webElement = driver.findElement(By.id("mw-content-text"));

            String text = webElement.getText();

            for (char character : text.toCharArray()){
                if(Character.UnicodeBlock.of(character) == CJK_UNIFIED_IDEOGRAPHS){
                    Kanji kanji = foundKanji.getOrDefault(character,new Kanji(character));
                    kanji.setOccurrence(kanji.getOccurrence()+1);
                    foundKanji.put(character,kanji);
                }
            }

            Tokenizer tokenizer = new Tokenizer();
            List<Token> tokens = tokenizer.tokenize(text);
            for (Token token : tokens) {
                if(token.getPartOfSpeechLevel1().equals("名詞") || token.getPartOfSpeechLevel1().equals("動詞")){
                    if(token.getBaseForm().equals("*")) continue;
                    Word word = foundWords.getOrDefault(token.getBaseForm(),new Word(token.getBaseForm(),token.getPartOfSpeechLevel1(),token.getPartOfSpeechLevel2()));
                    word.setOccurrence(word.getOccurrence()+1);
                    foundWords.put(token.getBaseForm(),word);
                }
            }

            webpageRepository.save(targetWebsite);
        }
    }
    public void initProperties(){
        properties = new Properties();
        java.net.URL url = ClassLoader.getSystemResource("application.properties");
        try  {
            properties.load(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
