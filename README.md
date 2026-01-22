# Java GUI Quiz App

A simple desktop multiple-choice quiz application written in Java using Swing. This repository contains three source files:

- `Question.java` — model for a single question
- `Quiz.java` — model and scoring logic for a quiz
- `QuizApp.java` — Swing-based GUI that lets you create a quiz, take it, and view the score

This README documents how the application works, how to compile/run it, the important design decisions, current limitations, and suggested improvements.

---

Table of contents
- Features
- Quick summary of code & responsibilities
- How scoring works
- Build and run (command-line and IDE)
- Usage walkthrough
- Known limitations
- Suggested improvements
- Contributing
- License

---

Features
- Create questions at runtime using a GUI:
  - Question text
  - Exactly 4 answer options
  - Select which option is correct
- Maintain an in-memory list of questions (add / delete)
- Take the quiz with Previous / Next navigation
- Answers saved as you navigate; quiz finished with confirmation
- Score calculated and reported out of 100
- Simple, clean Swing UI with basic styling

Quick summary of code & responsibilities
- Question.java
  - Fields: `questionText` (String), `options` (String[4]), `correctIndex` (int)
  - Constructor and getters
- Quiz.java
  - Internally stores questions in an `ArrayList<Question>`
  - Methods: `addQuestion`, `removeQuestion`, `size`, `getQuestion`
  - `calculateScore(int[] userAnswers)` — computes score based on correct answers. Score is normalized to 100 (see "How scoring works").
- QuizApp.java
  - Swing JFrame that implements two main flows:
    1. "Create Question" UI where the user can add/delete questions and start the quiz
    2. "Do Quiz" UI where the user answers questions, navigates with Previous/Next, and finishes the quiz
  - Enforces validation when adding questions:
    - Question text required
    - All 4 options must be filled
    - Exactly one option must be selected as correct
  - Prevents starting the quiz if `100 % numberOfQuestions != 0` (marks-per-question must be integer)
  - After finishing, shows score and returns to create-question screen

How scoring works
- The app distributes 100 points equally among all questions:
  - marksPerQuestion = 100 / numberOfQuestions
  - Final score = sum of marksPerQuestion for each correctly answered question
- Because integer division is used, the UI blocks starting a quiz unless 100 divides evenly by the number of questions. (E.g., 4, 5, 10, 20 questions allowed; 3 is not allowed.)

Build and run

Prerequisites
- Java JDK 8+ (Swing is part of the JDK)
- Source files are in the default (unnamed) package

Compile & run (simple)
1. Place `Question.java`, `Quiz.java`, and `QuizApp.java` in the same folder.
2. From that folder, compile:
   - javac Question.java Quiz.java QuizApp.java
3. Run:
   - java QuizApp

Create an executable JAR
1. Compile:
   - javac Question.java Quiz.java QuizApp.java
2. Create a JAR (set entry point to `QuizApp`):
   - jar cfe QuizApp.jar QuizApp *.class
3. Run:
   - java -jar QuizApp.jar

Run from an IDE
- Import the folder as a Java project or create a new Java project, add the three `.java` files, and run the `QuizApp` main method.

Usage walkthrough
1. Launch the app.
2. In the "Create Question" card, fill:
   - Question text
   - All four options
   - Select which option is correct (radio buttons labeled "Correct")
   - Click "Add Question" — the question appears in the question list.
3. Repeat until you have the desired number of questions (must divide 100 evenly).
4. Click "Start Quiz" to begin.
5. In the quiz UI:
   - Answer the question using radio buttons
   - Use "Previous" / "Next" to navigate. Answers are saved automatically.
   - Click "Finish Quiz" to confirm and view your total score (out of 100).
6. After finishing, the app returns to the create-question screen. Questions remain in memory until deleted or the app is closed.

Known limitations (current implementation)
- No persistence: questions and scores are only kept in memory for the running session and are not saved to disk.
- Exactly 4 options required per question (hard-coded).
- No import/export of question banks (JSON/CSV).
- No per-question timer or global timer.
- UI is in the default package and not internationalized.
- Uses integer arithmetic for scoring and requires `100 % n == 0`.
- No unit tests provided.
- Single-user, single-session only.

Suggested improvements (next steps)
- Persistence:
  - Save/load question banks and high scores as JSON or CSV.
  - Add a file-based repository or simple SQLite persistence.
- Flexible question types:
  - Support true/false, multiple-correct, image-based questions, and open-ended answers.
- Configurable scoring:
  - Allow fractional points, per-question weight, or negative marking.
  - Remove the restriction that 100 must divide evenly by question count.
- Timers:
  - Add optional per-question and/or quiz timers; implement auto-submit on timeout.
- Import/Export:
  - Implement JSON/CSV import/export GUIs and file dialogs.
- Refactor:
  - Move classes into packages (e.g., `model`, `ui`, `controller`).
  - Follow MVC or MVVM to separate UI and logic.
  - Add unit tests for `Quiz.calculateScore` and question validation logic.
- UX enhancements:
  - Confirm deletion with a dialog.
  - Show immediate feedback (correct/incorrect) after answering.
  - Improve layout responsiveness and support themes.
- Build:
  - Add Maven/Gradle for dependency and packaging management.
  - Provide CI pipeline and tests.


