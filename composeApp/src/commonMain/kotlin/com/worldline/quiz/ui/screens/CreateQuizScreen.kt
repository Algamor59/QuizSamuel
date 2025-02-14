package com.worldline.quiz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.worldline.quiz.data.Answer
import com.worldline.quiz.data.Question
import com.worldline.quiz.data.Quiz

@Composable
fun CreateQuizScreen(
    onQuizCreated: (Quiz) -> Unit,
    onBack: () -> Unit
) {
    var questions by remember { mutableStateOf(listOf<Question>()) }
    var currentQuestion by remember { mutableStateOf("") }
    var currentAnswers by remember { mutableStateOf(listOf<Answer>()) }
    var currentAnswerText by remember { mutableStateOf("") }
    var correctAnswerId by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Your Quiz", fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        // Input for question
        OutlinedTextField(
            value = currentQuestion,
            onValueChange = { currentQuestion = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Input for answers
        currentAnswers.forEachIndexed { index, answer ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (correctAnswerId == answer.id),
                    onClick = { correctAnswerId = answer.id }
                )
                TextField(
                    value = answer.label,
                    onValueChange = { newLabel ->
                        currentAnswers = currentAnswers.toMutableList().also {
                            it[index] = it[index].copy(label = newLabel)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Add answer input field
        OutlinedTextField(
            value = currentAnswerText,
            onValueChange = { currentAnswerText = it },
            label = { Text("New Answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Add answer button
        Button(onClick = {
            if (currentAnswerText.isNotBlank()) {
                currentAnswers = currentAnswers + Answer(currentAnswers.size + 1, currentAnswerText)
                currentAnswerText = ""
            }
        }) {
            Text("Add Answer")
        }

        // Add question button
        Button(onClick = {
            if (currentQuestion.isNotBlank() && currentAnswers.isNotEmpty()) {
                questions = questions + Question(
                    id = questions.size + 1,
                    label = currentQuestion,
                    correctAnswerId = correctAnswerId,
                    answers = currentAnswers
                )
                currentQuestion = ""
                currentAnswers = listOf()
                correctAnswerId = 1
            }
        }) {
            Text("Add Question")
        }

        // Finish creating quiz
        Button(onClick = {
            if (currentQuestion.isNotBlank() && currentAnswers.isNotEmpty()) {
                questions = questions + Question(
                    id = questions.size + 1,
                    label = currentQuestion,
                    correctAnswerId = correctAnswerId,
                    answers = currentAnswers
                )
            }
            onQuizCreated(Quiz(questions = questions))
        }) {
            Text("Finish")
        }

        // Back button
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
