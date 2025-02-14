import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.worldline.quiz.data.Question
import com.worldline.quiz.ui.screens.ScoreScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun questionScreen(questions: List<Question>, onFinishButtonPushed: (Int, Int) -> Unit) {
    var questionProgress by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(10) }
    val coroutineScope = rememberCoroutineScope()

    val primaryColor = Color(0xFF6200EE)
    val secondaryColor = Color(0xFF03DAC6)
    val backgroundColor = Color(0xFFF5F5F5)
    val cardColor = Color(0xFFBB86FC)

    // Compte à rebours
    LaunchedEffect(Unit) {
        while (questionProgress < questions.size && !quizFinished) {
            timeRemaining = 10
            while (timeRemaining > 0 && !quizFinished) {
                delay(1000)
                timeRemaining--
            }
            if (timeRemaining == 0 && !quizFinished) {
                if (questionProgress < questions.size - 1) {
                    questionProgress++
                    selectedAnswer = 1
                } else {
                    onFinishButtonPushed(score, questions.size)
                    quizFinished = true
                }
            }
        }
    }

    if (quizFinished) {
        ScoreScreen(score = score, totalQuestions = questions.size)
        return
    }

    val alpha by animateFloatAsState(
        targetValue = if (quizFinished) 0f else 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(modifier = Modifier.fillMaxSize().alpha(alpha)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
                backgroundColor = cardColor
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(all = 10.dp),
                        text = questions[questionProgress].label,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = primaryColor
                    )
                }
            }
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                questions[questionProgress].answers.forEach { answer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = (selectedAnswer == answer.id),
                            onClick = { selectedAnswer = answer.id },
                            colors = RadioButtonDefaults.colors(selectedColor = secondaryColor)
                        )
                        Text(
                            text = answer.label,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    modifier = Modifier.padding(bottom = 20.dp),
                    onClick = {
                        if (selectedAnswer == questions[questionProgress].correctAnswerId) {
                            score++
                        }
                        if (questionProgress < questions.size - 1) {
                            questionProgress++
                            selectedAnswer = 1
                            timeRemaining = 10
                        } else {
                            onFinishButtonPushed(score, questions.size)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
                ) {
                    if (questionProgress < questions.size - 1) {
                        nextOrDoneButton(Icons.AutoMirrored.Filled.ArrowForward, "Next")
                    } else {
                        nextOrDoneButton(Icons.Filled.Done, "Done")
                    }
                }
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 16.dp),
                    progress = questionProgress.toFloat() / questions.size.toFloat(),
                    color = secondaryColor
                )
                Text(
                    text = "Temps Restant: $timeRemaining secondes",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun nextOrDoneButton(iv: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = iv,
            contentDescription = label,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(label)
    }
}
