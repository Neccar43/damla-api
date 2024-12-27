package com.novacodestudios.repository

import com.novacodestudios.entity.Question
import com.novacodestudios.entity.QuestionEntity
import com.novacodestudios.entity.QuestionTable
import com.novacodestudios.model.AddQuestion
import com.novacodestudios.model.UpdateQuestion
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class QuestionRepository {

    suspend fun addQuestion(question: AddQuestion): Question = suspendTransaction {
        val questionEntity = QuestionEntity.new {
            questionText = question.questionText
        }
        questionEntity.toQuestion()
    }

    suspend fun addQuestions(questions: List<AddQuestion>): List<Question> = suspendTransaction {
        val questionEntities = questions.map { question ->
            QuestionEntity.new {
                questionText = question.questionText
            }
        }

        questionEntities.map { it.toQuestion() }
    }


    suspend fun updateQuestion(updatedQuestion: UpdateQuestion): Question? = suspendTransaction {
        QuestionEntity.findByIdAndUpdate(updatedQuestion.id) { questionEntity ->
            updatedQuestion.questionText?.let { questionEntity.questionText = it }
        }?.toQuestion()
    }

    suspend fun deleteQuestion(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = QuestionTable.deleteWhere { QuestionTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getQuestionById(id: Int): Question? = suspendTransaction {
        QuestionEntity.findById(id)?.toQuestion()
    }

    suspend fun searchQuestionsByText(keyword: String): List<Question> = suspendTransaction {
        QuestionEntity.find {
            QuestionTable.questionText like "%$keyword%"
        }.map { it.toQuestion() }
    }

    suspend fun getAllQuestions(): List<Question> = suspendTransaction {
        QuestionEntity.all().map { it.toQuestion() }
    }
}
