package com.novacodestudios.repository

import com.novacodestudios.entity.*
import com.novacodestudios.model.AddAnswer
import com.novacodestudios.model.UpdateAnswer
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

class AnswerRepository {

    suspend fun addAnswers(answers: List<AddAnswer>): List<Answer> = suspendTransaction {
        answers.mapNotNull { answer ->
            val isExist = AnswerEntity.find {
                (AnswerTable.questionId eq answer.questionId) and
                        (AnswerTable.appointmentId eq answer.appointmentId)
            }.any()
            if (!isExist) {
                val answerEntity = AnswerEntity.new {
                    questionEntity =
                        QuestionEntity.findById(answer.questionId) ?: throw Exception("QuestionEntity not found")
                    appointmentEntity = AppointmentEntity.findById(answer.appointmentId)
                        ?: throw Exception("AppointmentEntity not found")
                    answerText = answer.answerText
                }
                answerEntity.toAnswer()
            } else {
                null
            }
        }
    }


    suspend fun updateAnswer(updatedAnswer: UpdateAnswer): Answer? = suspendTransaction {
        AnswerEntity.findByIdAndUpdate(updatedAnswer.id) { answerEntity ->
            updatedAnswer.answerText?.let { answerEntity.answerText = it }
            updatedAnswer.questionId?.let {
                answerEntity.questionEntity = QuestionEntity.findById(it) ?: throw Exception("QuestionEntity not found")
            }
            updatedAnswer.appointmentId?.let {
                answerEntity.appointmentEntity =
                    AppointmentEntity.findById(it) ?: throw Exception("AppointmentEntity not found")
            }
        }?.toAnswer()
    }

    suspend fun deleteAnswer(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = AnswerTable.deleteWhere { AnswerTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getAnswerById(id: Int): Answer? = suspendTransaction {
        AnswerEntity.findById(id)?.toAnswer()
    }

    suspend fun getAnswersByAppointmentId(appointmentId: Int): List<Answer> = suspendTransaction {
        AnswerEntity.find { AnswerTable.appointmentId eq appointmentId }.map { it.toAnswer() }
    }

    suspend fun getAnswersByQuestionId(questionId: Int): List<Answer> = suspendTransaction {
        AnswerEntity.find { AnswerTable.questionId eq questionId }.map { it.toAnswer() }
    }

    suspend fun getAllAnswers(): List<Answer> = suspendTransaction {
        AnswerEntity.all().map { it.toAnswer() }
    }
}