package com.mycompany.getdbfromhtml;

import java.util.Collections;
import java.util.List;

public class Questions {

    private String numberQuestion;
    private String nameQuestion;
    private List<String> answerItems;
    private String correctAnswer;

    public Questions(String numberQuestion, String nameQuestion, List<String> answerItems, String correctAnswer) {
        this.numberQuestion = numberQuestion;
        this.nameQuestion = nameQuestion;
        this.answerItems = Collections.unmodifiableList(answerItems);
        this.correctAnswer = correctAnswer;
    }

    public String getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(String numberQuestion) {
        this.numberQuestion = numberQuestion;
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public List<String> getAnswerItems() {
        return answerItems;
    }

    // No setter for answerItems, making it immutable
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "Questions{"
                + "numberQuestion='" + numberQuestion + '\''
                + ", nameQuestion='" + nameQuestion + '\''
                + ", answerItems=" + answerItems
                + ", correctAnswer='" + correctAnswer + '\''
                + '}';
    }
   
    public String generateInsertStatement(String idTest) {
       
        
        StringBuilder insertStatement = new StringBuilder();
        //insertStatement.append("INSERT INTO Questions ( descriptionQ, answer1, answer2, answer3, answer4,correctAnswer, idTest) VALUES ");
        insertStatement.append("(N'").append(numberQuestion.replaceAll("'", "''")).append("<br>").append(nameQuestion.replaceAll("'", "''")).append("', ");

        // Assuming answerItems list has at least 4 elements
        for (int i = 0; i < 4; i++) {
            insertStatement.append("N'").append(answerItems.get(i).replaceAll("'", "''")).append("'");
            if (i < 3) {
                insertStatement.append(", ");
            }
        }

        insertStatement.append(", '").append(correctAnswer.replaceAll("'", "''")).append("', '").append(idTest).append("'),");

        return insertStatement.toString();
    }
}
