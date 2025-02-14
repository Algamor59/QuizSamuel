package com.worldline.quiz.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.worldline.quiz.data.Quiz
import org.jetbrains.compose.resources.painterResource
import quiz.composeapp.generated.resources.Res

@Composable
fun welcomeScreen(
    quizzes: List<Quiz>,
    onQuizSelected: (Quiz) -> Unit,
    onCreateQuiz: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose a Quiz!", fontSize = 20.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(quizzes) { quiz ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onQuizSelected(quiz) } // Rend la carte cliquable
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Quiz ${quizzes.indexOf(quiz) + 1}", fontSize = 18.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCreateQuiz) {
            Text("Create your Quiz !")
        }
    }
}




