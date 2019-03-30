package com.example.prilogulka.data.userData

import java.io.Serializable


class QuestionnaireInfo (var questionnaire: Questionnaire) : Serializable {
    override fun toString(): String {
        return questionnaire.toString()
    }
}