/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.getdbfromhtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlHtmlFetcher {

    public List<Questions> extractData(String html) {
        //
        List<Questions> listQuiz = new ArrayList<>();
        // Parse chuỗi HTML thành một đối tượng Document của Jsoup
        Document document = Jsoup.parse(html);

        // tìm phần tử có class .test-result-detail
        Element testResultDetail = document.select(".test-result-detail").first();

        // Kiểm tra xem phần tử có tồn tại không
        if (testResultDetail != null) {
            // Trích xuất toàn bộ nội dung bên trong phần tử .test-result-detail
            String content = testResultDetail.html();
            //System.out.println("Content within test-result-detail:");
            // System.out.println(content);

            // Trích xuất các phần tử cụ thể bên trong
            // Chọn tất cả các phần tử có class "quiz-answer-item"
            Elements quiz = testResultDetail.select(".quiz-answer-item");

            for (Element q : quiz) {
                String numberQuestion = "";
                String nameQuestion = "";
                List<String> answerItems = new ArrayList<>();
                String correctAnswer = "";

                // Select the .number-question element excluding the <span> element
                numberQuestion = q.select(".number-question").text();

                // Select the .question-name element
                nameQuestion = q.select(".question-name").toString();

                // Select all .anwser-item elements
                Elements answerItemElements = q.select(".anwser-item");
                for (Element answerItem : answerItemElements) {
                    // Extract the answer text
                    String answerText = "";
                    try {
                        answerText = removeStyle(answerItem.select("p"));

                    } catch (Exception e) {
                        answerText = answerItem.select("p").toString();
                        
                    }

                    // Extract the correct class information
                    boolean isCorrect = answerItem.hasClass("correct");
                    // Add the answer text to the list
                    answerItems.add(answerText);
                    // If the answer is correct, set it as the correct answer
                    if (isCorrect) {                        
                        correctAnswer = answerText.substring(answerText.indexOf('.') - 1, answerText.indexOf('.'));
                    }
                }

                // Create a Questions object and print or process the data as needed
                Questions questions = new Questions(extractTextAfterColon(numberQuestion), nameQuestion, answerItems, correctAnswer);
                listQuiz.add(questions);

            }

        } else {
            listQuiz.add(new Questions("thoát", null, null, null));
        }
        return listQuiz;
    }

    public static String extractFirstUnderlineAndLastSpan(Elements spans) {
        StringBuilder result = new StringBuilder();

        // Check if there is at least one <u> tag
        Element firstUnderline = spans.select("u").first();
        if (firstUnderline != null) {
            result.append(firstUnderline.toString());
        }

        // Check if there is at least one <span> tag
        Element lastSpan = spans.select("span").last();
        if (lastSpan != null) {
            result.append(lastSpan.toString());
        }

        return result.toString();
    }

    public static String removeStyle(Elements answerItems) {
        // Create a StringBuilder to store the modified HTML
        StringBuilder modifiedHtml = new StringBuilder();

        // Iterate through each answerItem
        for (Element answerItem : answerItems) {
            // Clone the answerItem to avoid modifying the original element directly
            Element clonedAnswerItem = answerItem.clone();

            // Select all <span>, <strong>, and <p> elements within the cloned answerItem
            Elements spanElements = clonedAnswerItem.select("span");
            Elements strongElements = clonedAnswerItem.select("strong");
            Elements pElements = clonedAnswerItem.select("p");

            // Remove style attribute from all <span> elements
            for (Element spanElement : spanElements) {
                spanElement.removeAttr("style");
            }

            // Remove style attribute from all <strong> elements
            for (Element strongElement : strongElements) {
                strongElement.removeAttr("style");
            }

            // Remove style attribute from all <p> elements
            for (Element pElement : pElements) {
                pElement.removeAttr("style");
            }

            // Append the modified HTML to the StringBuilder
            modifiedHtml.append(clonedAnswerItem.outerHtml());
        }

        // Convert the StringBuilder to a String and return
        return modifiedHtml.toString();
    }

    public static void main(String[] args) {
        UrlHtmlFetcher fet = new UrlHtmlFetcher();
//        Scanner scanner = new Scanner(System.in);
//
//        // Ask user for the file path
//        System.out.print("Enter the HTML file path: ");
//        String filePath = scanner.nextLine();
//
//        // Read HTML content from the file
//        String html = fet.readHtmlFromFile(filePath);
//
//        // Extract data from HTML and get a list of Questions
//        List<Questions> test = fet.extractData(html);
//
//        // Print or process the list of Questions
//        System.out.println("INSERT INTO Questions ( descriptionQ, answer1, answer2, answer3, answer4,CorrectAnswer,idTest) VALUES");
//        for (Questions questions : test) {
//            System.out.println(questions.generateInsertStatement("T1"));
//        }

    }

    public boolean isValidUrl(String url) {
        // A simple URL validation, you might want to enhance it
        return url != null && url.startsWith("http");
    }

    public String readHtmlFromFile(String filePath) {
        String html = "";
        try {
            // Read all lines from the file
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            // Join lines to get the complete HTML content
            html = String.join(System.lineSeparator(), lines);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    public String extractTextAfterColon(String text) {
        // Split the text based on ":" and get the second part
        String[] parts = text.split(":");
        if (parts.length > 1) {
            return parts[0].trim();
        } else {
            // Handle the case where ":" is not found
            return text.trim();
        }
    }

    public String getHtmlFromUrl(String url) {
        try {
            // Kết nối đến URL và lấy dữ liệu HTML
            Document document = Jsoup.connect(url).get();

            // Lấy toàn bộ nội dung HTML
            String html = document.outerHtml();

            return html;
        } catch (IOException e) {
            return "";
        }
    }
}
