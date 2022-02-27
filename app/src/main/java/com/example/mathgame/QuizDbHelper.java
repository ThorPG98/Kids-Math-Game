package com.example.mathgame;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mathgame.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 4;

    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT" +
                " ) ";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    // To update Schema (Change DATABASE_VERSION to 2) and it will run this function
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        // Easy Mode Question
        Question q1 = new Question("2 + 1",
                " 3", " 4", " 5", 1, Question.DIFFICULTY_EASY);
        addQuestion(q1);
        Question q2 = new Question("2 + 3",
                "6", "7", "5", 3, Question.DIFFICULTY_EASY);
        addQuestion(q2);
        Question q3 = new Question("3 + 4",
                "10", "7", "4", 2, Question.DIFFICULTY_EASY);
        addQuestion(q3);
        Question q4 = new Question("6 - 4",
                "2", "10", "24", 1, Question.DIFFICULTY_EASY);
        addQuestion(q4);
        Question q5 = new Question("3 - 3",
                "0", "3", "6", 1, Question.DIFFICULTY_EASY);
        addQuestion(q5);

        // Medium Mode Question
        Question q6 = new Question("2 * 5",
                "10", "7", "3", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q6);
        Question q7 = new Question("3 * 3",
                "3", "6", "9", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q7);
        Question q8 = new Question("1 * 10",
                "10", "11", "20", 1, Question.DIFFICULTY_MEDIUM);
        addQuestion(q8);
        Question q9 = new Question("6 / 3",
                "3", "2", "9", 2, Question.DIFFICULTY_MEDIUM);
        addQuestion(q9);
        Question q10 = new Question("10 / 10",
                "20", "10", "1", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q10);

        // Hard Mode Question
        Question q11 = new Question("6 + 6 - 3",
                "15", "9", "12", 2, Question.DIFFICULTY_HARD);
        addQuestion(q11);
        Question q12 = new Question("7 - 3 + 2",
                "6", "12", "5", 1, Question.DIFFICULTY_HARD);
        addQuestion(q12);
        Question q13 = new Question("1 * 2 + 3",
                "5", "6", "4", 1, Question.DIFFICULTY_HARD);
        addQuestion(q13);
        Question q14 = new Question("3 + 2 * 2",
                "10", "9", "7", 3, Question.DIFFICULTY_HARD);
        addQuestion(q14);
        Question q15 = new Question("6 + 6 / 3",
                "4", "8", "12", 2, Question.DIFFICULTY_HARD);
        addQuestion(q15);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
