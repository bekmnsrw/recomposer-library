package com.bekmnsrw.recomposer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

data class Cat(val name: String)
data class Dog(val name: String)

@Composable
fun RecomposerSample() {
    var cat by remember { mutableStateOf(Cat(name = "Barsik")) }
    var dog by remember { mutableStateOf(Dog(name = "Rex")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecomposerSampleContent(
            cat = cat,
            dog = dog
        )
        Button(
            onClick = {
                cat = Cat("Barsik${(0..100).random()}")
            }
        ) {
            Text(text = "Create new cat")
        }
        Button(
            onClick = {
                dog = Dog("Rex${(0..100).random()}")
            }
        ) {
            Text(text = "Create new dog")
        }
    }
}

@Composable
fun RecomposerSampleContent(
    cat: Cat,
    dog: Dog
) {
    Recomposer(
        trackingComposableArguments = mapOf(
            "cat" to cat,
            "dog" to dog
        ),
        composableName = "RecomposerSampleContent"
    )
    Column {
        Text(text = "Cat is $cat")
        Text(text = "Dog is $dog")
    }
}
