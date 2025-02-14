package com.worldline.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.worldline.quiz.ui.screens.ScoreScreen
import com.worldline.quiz.data.Question
import com.worldline.quiz.data.Answer
import com.worldline.quiz.data.LeaderboardEntry
import com.worldline.quiz.data.Quiz
import com.worldline.quiz.ui.scoreScreen
import com.worldline.quiz.ui.welcomeScreen
import com.worldline.quiz.ui.screens.CreateQuizScreen
import com.worldline.quiz.ui.screens.LeaderboardScreen
import quiz.composeapp.generated.resources.Res
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


import kotlinx.serialization.Serializable
import questionScreen




@Serializable
object WelcomeRoute



@Serializable
data class ScoreRoute(val score: Int, val questionSize: Int)

@Serializable
data class QuizRoute(val quizIndex: Int) // Permet de passer l'index du quiz sélectionné

@Serializable
object CreateQuizRoute

@Serializable
object LeaderboardRoute


@Composable
fun App(navController: NavHostController = rememberNavController()) {

    val defaultQuiz = Quiz(
        questions = listOf(
            Question(
                id = 1,
                label = "Quelle est la capitale du Monde ?",
                correctAnswerId = 2,
                answers = listOf(
                    Answer(id = 1, label = "Roubaix"),
                    Answer(id = 2, label = "Konoha"),
                    Answer(id = 3, label = "PupuceLand")
                )
            ),
            Question(
                id = 2,
                label = "Quel est le plus grand rêve de tout Homme ?",
                correctAnswerId = 1,
                answers = listOf(
                    Answer(id = 1, label = "Devenir Hokage"),
                    Answer(id = 2, label = "Rater son examen"),
                    Answer(id = 3, label = "42 (pas d'inspi)")
                )
            )
        )
    )

    var quizzes by remember { mutableStateOf(listOf(defaultQuiz)) }
    val leaderboard = remember { mutableStateListOf<LeaderboardEntry>() }

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = WelcomeRoute.toString(),
        ) {
            composable(WelcomeRoute.toString()) {
                welcomeScreen(
                    quizzes = quizzes,
                    onQuizSelected = { selectedQuiz ->
                        val index = quizzes.indexOf(selectedQuiz)
                        navController.navigate("quiz/$index")
                    },
                    onCreateQuiz = {
                        navController.navigate(CreateQuizRoute.toString())
                    }
                )
            }
            composable(CreateQuizRoute.toString()) {
                CreateQuizScreen(
                    onQuizCreated = { newQuiz ->
                        quizzes = quizzes + newQuiz
                        navController.navigate(WelcomeRoute.toString())
                    },
                    onBack = {
                        navController.navigate(WelcomeRoute.toString())
                    }
                )
            }
            composable(
                route = "quiz/{quizIndex}",
                arguments = listOf(navArgument("quizIndex") { type = NavType.IntType })
            ) { backStackEntry ->
                val quizIndex = backStackEntry.arguments?.getInt("quizIndex")
                quizIndex?.let {
                    questionScreen(
                        questions = quizzes[it].questions,
                        onFinishButtonPushed = { score, questionSize ->
                            navController.navigate("score/$score/$questionSize")
                        }
                    )
                }
            }
            composable(
                route = "score/{score}/{questionSize}",
                arguments = listOf(
                    navArgument("score") { type = NavType.IntType },
                    navArgument("questionSize") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val score = backStackEntry.arguments?.getInt("score")
                val questionSize = backStackEntry.arguments?.getInt("questionSize")
                score?.let { scoreValue ->
                    questionSize?.let { questionSizeValue ->
                        scoreScreen(
                            score = scoreValue,
                            total = questionSizeValue,
                            onResetButtonPushed = {
                                navController.navigate(LeaderboardRoute.toString())
                            },
                            onNameEntered = { name ->
                                leaderboard.add(LeaderboardEntry(name, scoreValue))
                                navController.navigate(LeaderboardRoute.toString())
                            }
                        )
                    }
                }
            }
            composable(LeaderboardRoute.toString()) {
                LeaderboardScreen(
                    leaderboard = leaderboard,
                    onBackToWelcome = {
                        navController.navigate(WelcomeRoute.toString())
                    }
                )
            }
        }
    }
}









