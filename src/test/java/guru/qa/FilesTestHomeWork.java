package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.selector.ByText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTestHomeWork {

    @Test
    @DisplayName("Загрузка файла по абсолютному пути")
    void filenameShouldDisplayedAfterUploadActionAbsolutePathTest() {
        open("https://dropmefiles.com/");
        File exampleFile = new File("C:\\Users\\zatul\\IdeaProjects\\qa_guru_9_8_files\\src\\test\\resources\\example.txt");
        $("input[type='file']").uploadFile(exampleFile);
        $("[class='expand']").click();
        $("[id='upfiles']").shouldHave(text("example.txt"));

    }

    @Test
    @DisplayName("Загрузка файла по относительному пути")
    void filenameShouldDisplayedAfterUploadActionFromClasspathTest() {
        open("https://dropmefiles.com/");
        $("input[type='file']").uploadFromClasspath("example.txt");
        $("[class='expand']").click();
        $("[id='upfiles']").shouldHave(text("example.txt"));
    }

    @Test
    @DisplayName("Скачивание текстового файла и проверка его содержимого")
    void downloadSimpleTextFileTest() throws IOException {
        open("https://github.com/OlgaZtv/python_training/blob/main/README.md");
        File download = $("#raw-url").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("Repository for Python training"));
    }

    @Test
    @DisplayName("Скачивание PDF файла")
    void pdfFileDownloadTest() throws IOException {
        open("https://www.rulit.me/books/arhivarius-get-386934.html");
        File pdf = $(new ByText("В ФОРМАТЕ PDF")).parent().download();
        PDF parsedPdf = new PDF(pdf);
        Assertions.assertEquals(232, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Скачивание XLS файла")
    void xlsFileDownloadTest() throws IOException {
        open("https://kub-24.ru/prajs-list-shablon-prajs-lista-2020-v-excel-word-pdf/");
        File file = $(byText("шаблон прайс-листа в Excel")).download();

        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue()
                .contains("Кирпич лицевой одинарный (размер 250х120х65) М150 цвет абрикос");

        assertTrue(checkPassed);
    }

    @Test
    @DisplayName("Парсинг CSV файлов")
    void parseCsvFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("csv.csv");
             Reader reader = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(reader);

            List<String[]> strings = csvReader.readAll();
            assertEquals(3, strings.size());
        }
    }

    @Test
    @DisplayName("Парсинг ZIP файлов")
    void parseZipFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("example.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }
    }
}
