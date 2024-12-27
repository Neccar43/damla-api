package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object AnswerTable : IntIdTable("answer") {
    val questionId = reference("question_id", QuestionTable)
    val appointmentId = reference("appointment_id", AppointmentTable, onDelete = ReferenceOption.CASCADE)
    val answerText = text("answer_text")
}

class AnswerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AnswerEntity>(AnswerTable)

    var questionEntity by QuestionEntity referencedOn AnswerTable.questionId
    var appointmentEntity by AppointmentEntity referencedOn AnswerTable.appointmentId
    var answerText by AnswerTable.answerText


    fun toAnswer() = Answer(
        id = id.value,
        question = questionEntity.toQuestion(),
        appointment = appointmentEntity.toAppointment(),
        answerText = answerText,
    )
}

@Serializable
data class Answer(
    val id: Int,
    val question: Question,
    val appointment: Appointment,
    val answerText: String,
)