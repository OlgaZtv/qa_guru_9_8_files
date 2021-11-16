package guru.qa;


import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Selenide.*;

public class UgolTests {


    @DisplayName("Проверка поиска Ugol")
    @Tag("Major")
    @Tag("WEB")
    @ParameterizedTest(name = "Поиск в каталоге товаров для ремонта сайта Ugol.me")
    @ValueSource(strings = {"Kerama marazzi", "IKEA"})
    void selenideSearchTest(String searchQwery){
        open("https://ugol.me/catalog");
        $("#mat-input-0").setValue(searchQwery).pressEnter();
        $$("h1").shouldHave(CollectionCondition.texts(new String[]{searchQwery}));
    }

    @ValueSource(strings = {"Другие товары из коллекции «Макарена»"})
    @Tag("Major")
    @Tag("WEB")
    @DisplayName("Проверка коллекции")
    @ParameterizedTest(name = "Проверить отображение других товаров из коллекции в каталоге сайта Ugol.me")
    void collectionShouldBeInCardTest(String collectionName){
        open("https://ugol.me/catalog/keramika/plitka-nastennaya");
        $(".product-content").click();
        $$("h2").shouldHave(CollectionCondition.texts(collectionName));
    }

}
