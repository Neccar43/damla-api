package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object QuestionTable : IntIdTable("question") {
    val questionText = text("question_text")

}

class QuestionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionEntity>(QuestionTable)

    var questionText by QuestionTable.questionText

    fun toQuestion() = Question(
        id.value,
        questionText
    )
}

@Serializable
data class Question(
    val id: Int,
    val questionText: String,
)