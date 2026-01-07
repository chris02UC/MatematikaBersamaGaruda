package com.example.matematikabersamagaruda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType // Added missing import
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument // Added missing import
import com.example.matematikabersamagaruda.view.HomeScreen
import com.example.matematikabersamagaruda.view.QuizDrawPage
import com.example.matematikabersamagaruda.view.QuizModeStart
import com.example.matematikabersamagaruda.view.QuizResultsPage
import com.example.matematikabersamagaruda.view.ZenModeStart
import com.example.matematikabersamagaruda.view.ZenDrawPage
import com.example.matematikabersamagaruda.view.ZenResultsPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // Set up the Navigation Host
            NavHost(navController = navController, startDestination = "home") {

                // 1. Home Screen
                composable("home") {
                    HomeScreen(navController)
                }

                // 3. Zen Mode Difficulty Selection Screen
                composable("zenmodestart") {
                    ZenModeStart(navController)
                }

                // 4. Zen Mode Drawing Screen (with isHard logic)
                composable(
                    route = "zendrawpage/{isHard}",
                    arguments = listOf(navArgument("isHard") { type = NavType.BoolType })
                ) { backStackEntry ->
                    val isHard = backStackEntry.arguments?.getBoolean("isHard") ?: false

                    // Navigates to the drawing page with the difficulty level
                    ZenDrawPage(navController = navController, isHard = isHard)
                }
                composable(
                    route = "zenresults/{score}",
                    arguments = listOf(navArgument("score") { type = NavType.IntType })
                ) { backStackEntry ->
                    val score = backStackEntry.arguments?.getInt("score") ?: 0
                    ZenResultsPage(navController, score)
                }
                // Inside your NavHost in MainActivity.kt
                composable("quizmodestart") {
                    // Pass the navController here to fix the error
                    QuizModeStart(navController = navController)
                }

                composable(
                    route = "quizdrawpage/{isHard}/{time}/{count}",
                    arguments = listOf(
                        navArgument("isHard") { type = NavType.BoolType },
                        navArgument("time") { type = NavType.IntType },
                        navArgument("count") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val isHard = backStackEntry.arguments?.getBoolean("isHard") ?: false
                    val time = backStackEntry.arguments?.getInt("time") ?: 60
                    val count = backStackEntry.arguments?.getInt("count") ?: 10

                    QuizDrawPage(
                        navController = navController,
                        isHard = isHard,
                        time = time,
                        totalQuestions = count
                    )
                }

                composable(
                    route = "quizresults/{right}/{total}",
                    arguments = listOf(
                        navArgument("right") { type = NavType.IntType },
                        navArgument("total") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val right = backStackEntry.arguments?.getInt("right") ?: 0
                    val total = backStackEntry.arguments?.getInt("total") ?: 0

                    QuizResultsPage(navController, right, total)
                }
            }
        }
    }
}