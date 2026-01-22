import java.util.ArrayList;

public class Quiz {
    private ArrayList<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public void removeQuestion(int index) {
        questions.remove(index);
    }

    public int size() {
        return questions.size();
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int calculateScore(int[] userAnswers) {
        int marksPerQuestion = 100 / questions.size();
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectIndex()) {
                score += marksPerQuestion;
            }
        }
        return score;
    }
}
