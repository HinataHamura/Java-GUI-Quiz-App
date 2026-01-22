import javax.swing.*;
import java.awt.*;

public class QuizApp extends JFrame {


    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY_BTN = new Color(127, 179, 213);
    private final Color SECONDARY_BTN = new Color(214, 234, 248);
    private final Color BORDER_COLOR = new Color(174, 214, 241);
    private final Color TEXT_COLOR = new Color(60, 60, 60);


    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

    // ================= QUIZ DATA =================
    private Quiz quiz = new Quiz();
    private int currentQuestion = 0;
    private int[] userAnswers;

    // ================= CREATE QUIZ COMPONENTS =================
    private JTextField questionField;
    private JTextField[] optionFields;
    private JRadioButton[] correctButtons;
    private ButtonGroup correctGroup;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;

    // ================= DO QUIZ COMPONENTS =================
    private JLabel questionLabel;
    private JRadioButton[] answerButtons;
    private ButtonGroup answerGroup;
    private JButton prevBtn, nextBtn;

    public QuizApp() {
        setTitle("Quiz Application");
        setSize(780, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showCreateQuizPanel();
    }

    private void reloadQuestionList() {
    questionListModel.clear();
    for (int i = 0; i < quiz.size(); i++) {
        questionListModel.addElement(
                quiz.getQuestion(i).getQuestionText()
        );
    }
}


    // ================= CREATE QUIZ PANEL =================
    private void showCreateQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // -------- INPUT PANEL (CARD) --------
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        "Create Question"
                )
        );

        questionField = new JTextField();
        optionFields = new JTextField[4];
        correctButtons = new JRadioButton[4];
        correctGroup = new ButtonGroup();

        JLabel qLabel = new JLabel("Question:");
        qLabel.setFont(LABEL_FONT);
        qLabel.setForeground(TEXT_COLOR);
        inputPanel.add(qLabel);
        inputPanel.add(questionField);

        for (int i = 0; i < 4; i++) {
            optionFields[i] = new JTextField();

            correctButtons[i] = new JRadioButton("Correct");
            correctButtons[i].setBackground(CARD_COLOR);
            correctButtons[i].setForeground(TEXT_COLOR);
            correctGroup.add(correctButtons[i]);

            JLabel optLabel = new JLabel("Option " + (i + 1) + ":");
            optLabel.setFont(LABEL_FONT);
            optLabel.setForeground(TEXT_COLOR);
            inputPanel.add(optLabel);

            JPanel optionPanel = new JPanel(new BorderLayout(8, 0));
            optionPanel.setBackground(CARD_COLOR);
            optionPanel.add(optionFields[i], BorderLayout.CENTER);
            optionPanel.add(correctButtons[i], BorderLayout.EAST);

            inputPanel.add(optionPanel);
        }

        // -------- BUTTONS --------
        JButton addBtn = new JButton("Add Question");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton startBtn = new JButton("Start Quiz");

        addBtn.setFont(BUTTON_FONT);
        deleteBtn.setFont(BUTTON_FONT);
        startBtn.setFont(BUTTON_FONT);

        addBtn.setBackground(SECONDARY_BTN);
        deleteBtn.setBackground(SECONDARY_BTN);

        startBtn.setBackground(PRIMARY_BTN);
        startBtn.setForeground(Color.WHITE);

        addBtn.setFocusPainted(false);
        deleteBtn.setFocusPainted(false);
        startBtn.setFocusPainted(false);

        startBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(startBtn);

        // -------- QUESTION LIST --------
        if (questionListModel == null) {
            questionListModel = new DefaultListModel<>();
        }
        questionList = new JList<>(questionListModel);
        reloadQuestionList();

        questionList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        questionList.setBackground(CARD_COLOR);
        questionList.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        questionList.setVisibleRowCount(6);

        JScrollPane scrollPane = new JScrollPane(questionList);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        "Questions"
                )
        );

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();

        addBtn.addActionListener(e -> addQuestion());
        deleteBtn.addActionListener(e -> deleteQuestion());
        startBtn.addActionListener(e -> startQuiz());
    }

    private void addQuestion() {
        String qText = questionField.getText().trim();
        String[] options = new String[4];
        int correct = -1;

        for (int i = 0; i < 4; i++) {
            options[i] = optionFields[i].getText().trim();
            if (correctButtons[i].isSelected()) correct = i;
        }

        if (qText.isEmpty() || correct == -1) {
            JOptionPane.showMessageDialog(this, "Please complete the question and select the correct answer.");
            return;
        }

        for (String opt : options) {
            if (opt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All options must be filled.");
                return;
            }
        }

        quiz.addQuestion(new Question(qText, options, correct));
        questionListModel.addElement(qText);

        questionField.setText("");
        correctGroup.clearSelection();
        for (JTextField f : optionFields) f.setText("");
    }

    private void deleteQuestion() {
        int index = questionList.getSelectedIndex();
        if (index >= 0) {
            quiz.removeQuestion(index);
            questionListModel.remove(index);
        }
    }

    private void startQuiz() {
        if (quiz.size() == 0) {
            JOptionPane.showMessageDialog(this, "No questions added!");
            return;
        }

        if (100 % quiz.size() != 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Marks per question must be an integer!",
                    "Invalid Quiz",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        userAnswers = new int[quiz.size()];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;

        currentQuestion = 0;
        showDoQuizPanel();
    }

    // ================= DO QUIZ PANEL =================
    private void showDoQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        questionLabel = new JLabel();
        questionLabel.setFont(TITLE_FONT);
        questionLabel.setForeground(TEXT_COLOR);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 6, 6));
        centerPanel.setBackground(CARD_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        answerButtons = new JRadioButton[4];
        answerGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setFont(LABEL_FONT);
            answerButtons[i].setBackground(CARD_COLOR);
            answerButtons[i].setForeground(TEXT_COLOR);
            answerGroup.add(answerButtons[i]);
            centerPanel.add(answerButtons[i]);
        }

        prevBtn = new JButton("Previous");
        nextBtn = new JButton("Next");
        JButton finishBtn = new JButton("Finish Quiz");

        prevBtn.setBackground(SECONDARY_BTN);
        nextBtn.setBackground(SECONDARY_BTN);
        finishBtn.setBackground(PRIMARY_BTN);
        finishBtn.setForeground(Color.WHITE);

        prevBtn.setFont(BUTTON_FONT);
        nextBtn.setFont(BUTTON_FONT);
        finishBtn.setFont(BUTTON_FONT);

        prevBtn.setFocusPainted(false);
        nextBtn.setFocusPainted(false);
        finishBtn.setFocusPainted(false);

        JPanel navPanel = new JPanel();
        navPanel.setBackground(BG_COLOR);
        navPanel.add(prevBtn);
        navPanel.add(nextBtn);
        navPanel.add(finishBtn);

        panel.add(questionLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(navPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();

        loadQuestion();

        prevBtn.addActionListener(e -> {
            saveAnswer();
            currentQuestion--;
            loadQuestion();
        });

        nextBtn.addActionListener(e -> {
            saveAnswer();
            currentQuestion++;
            loadQuestion();
        });

        finishBtn.addActionListener(e -> finishQuiz());
    }

    private void loadQuestion() {
        Question q = quiz.getQuestion(currentQuestion);
        questionLabel.setText(
                "Question " + (currentQuestion + 1) + " of " + quiz.size() +
                        ": " + q.getQuestionText()
        );

        String[] opts = q.getOptions();
        answerGroup.clearSelection();

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(opts[i]);
            if (userAnswers[currentQuestion] == i) {
                answerButtons[i].setSelected(true);
            }
        }

        prevBtn.setEnabled(currentQuestion > 0);
        nextBtn.setEnabled(currentQuestion < quiz.size() - 1);
    }

    private void saveAnswer() {
        for (int i = 0; i < 4; i++) {
            if (answerButtons[i].isSelected()) {
                userAnswers[currentQuestion] = i;
            }
        }
    }

    private void finishQuiz() {
        saveAnswer();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to finish the quiz?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int score = quiz.calculateScore(userAnswers);

        JOptionPane.showMessageDialog(
                this,
                "Your total score: " + score + " / 100",
                "Quiz Finished",
                JOptionPane.INFORMATION_MESSAGE
        );

        // শুধু quiz attempt reset
        currentQuestion = 0;
        userAnswers = null;

        // আগের questions রেখে Home page এ ফেরা
        showCreateQuizPanel();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizApp().setVisible(true));
    }
}
