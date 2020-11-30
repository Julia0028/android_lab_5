# Лабораторная работа №5. UI Tests.
### Цели
* Ознакомиться с принципами и получить практические навыки разработки UI тестов для Android приложений.

### Задачи
Предполагается, что все задачи решаются с помощью Instrumentation (Android) tests и Espresso Framework, если иное явно не указано в тексте задания.

#### Задача 1. Простейший UI тест

**Задание:** ознакомиться с Espresso Framework: https://developer.android.com/training/testing/espresso. Разработать приложение, в котором есть одна кнопка (Button) и одно текстовое поле (EditText). При (первом) нажатии на кнопку текст на кнопке должен меняться.
Написать Espresso тест, который проверяет, что при повороте экрана содержимое текстового поля (каким бы оно ни было) сохраняется, а надпись на кнопке сбрасывается в исходное состояние.

**Решение:** 

Код приложения:
**MainAktivity.kt**
```
package com.example.lab_5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val but: Button = findViewById(R.id.button)
        but.setOnClickListener {
            but.text = getString(R.string.string)
        }
    }


}
```

**activity_main.xml**
```
  
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button"
        android:layout_width="138dp"
        android:layout_height="106dp"
        android:text="@string/button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editText" />

    <EditText
        android:id="@+id/editText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:ems="10"
        android:inputType="textEmailAddress"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

Полученное приложение:

До поворота экрана:
![text_1]()

После поворота экрана:
![text_2]()

При повороте экрана текст кнопки сбрасывается, а текст в EditText сохраняется. 

Espresso тест:
Сначала в EditText записывается слово "Hello", проверяется, что слово действительно там записалось. Далее идет взаимодействие с компонентом - click(), т.е. нажатие кнопки и проверка измении текста на ней. Далее меняем ориентацию устройства: идет проверка на сохранения слова в EditText и на текст кнопки (он сохраняться не должен).

Код теста:

**MainActivityTest.kt**
```
package com.example.lab_5

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun orientationTest(){
        onView(withId(R.id.editText)).perform(typeText("Hello"), closeSoftKeyboard())
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.button)).check(matches(withText("Hell")))

        onView(withId(R.id.editText)).check(matches(withText("Hello")))

        activityRule.scenario.onActivity {
           it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
       }

        onView(withId(R.id.editText)).check(matches(withText("Hello")))
        onView(withId(R.id.button)).check(matches(withText("World!")))
    }
}
```


#### Задача 2. Тестирование навигации.

**Задание:** взять приложение из Лаб №3 о навигации (любое из решений). Написать UI тесты, проверяющие навигацию между 4мя исходными Activity/Fragment (1-2-3-About). В отчете написать, что проверяет каждый тест.

**Решение:**
Написала методы, которые помогут определить, находимся ли мы в  той activity, которую предполагаем. Они проверяютналичие или отсутсвие определнных кнопок по id.
```
package com.example.lab_5

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.Rule
import org.junit.Test

class EspressoTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(FirstActivity::class.java)

    private fun firstActivityExist() {
        onView(withId(R.id.butToSecond)).check(matches(isDisplayed()))
        onView(withId(R.id.butToFirst)).check(doesNotExist())
        onView(withId(R.id.butToThird)).check(doesNotExist())
        onView(withId(R.id.butToFirstFromThird)).check(doesNotExist())
        onView(withId(R.id.butToSecFromThird)).check(doesNotExist())
    }

    private fun secondActivityExist() {
        onView(withId(R.id.butToFirst)).check(matches(isDisplayed()))
        onView(withId(R.id.butToThird)).check(matches(isDisplayed()))
        onView(withId(R.id.butToSecond)).check(doesNotExist())
        onView(withId(R.id.butToFirstFromThird)).check(doesNotExist())
        onView(withId(R.id.butToSecFromThird)).check(doesNotExist())
    }

    private fun thirdActivityExist() {
       onView(withId(R.id.butToFirstFromThird)).check(matches(isDisplayed()))
       onView(withId(R.id.butToSecFromThird)).check(matches(isDisplayed()))
        onView(withId(R.id.butToFirst)).check(doesNotExist())
        onView(withId(R.id.butToThird)).check(doesNotExist())
        onView(withId(R.id.butToSecond)).check(doesNotExist())
    }

    private fun toAboutFromMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        onView(withText("About")).perform(click());
        onView(withText("About")).check(matches(isDisplayed()));
        pressBack()
    }
```

Методы используются в следующих тестах. Я проверяю перемещение между activity с помощью кнопок. Также в некоторых методах есть проверка  на корректную работу Options Menu - переход в About и возвращение по кнопке "Назад".
```
 @Test
    fun firstToSecond() {
        firstActivityExist()
        toAboutFromMenu()
        onView(withId(R.id.butToSecond)).perform(click())
        secondActivityExist()
    }

    @Test
    fun secondToThird() {
        firstToSecond()
        toAboutFromMenu()
        onView(withId(R.id.butToThird)).perform(click())
        thirdActivityExist()
    }

    @Test
    fun secondToFirst() {
        firstToSecond()
        onView(withId(R.id.butToFirst)).perform(click())
        firstActivityExist()
    }


    @Test
    fun thirdToSecond() {
        secondToThird()
        toAboutFromMenu()
        onView(withId(R.id.butToSecFromThird)).perform(click())
        secondActivityExist()
    }

    @Test
    fun thirdToFirst() {
        secondToThird()
        onView(withId(R.id.butToFirstFromThird)).perform(click())
        firstActivityExist()
    }
```
И проверяется backStack: при нажатии кнопки "Назад" мы должны вернуться в определенную Activity.
```
@Test
    fun backToFirstFromSecond() {
        firstToSecond()
        pressBack()
        firstActivityExist()
    }

    @Test
    fun backToSecondFromThird() {
        secondToThird()
        pressBack()
        secondActivityExist()
    }

    @Test
    fun backToFirstFromThird() {
        secondToThird()
        pressBack()
        pressBack()
        firstActivityExist()
    }
}
```

### Вывод
В процессе выполнения данной работы получен навык написания тестов с помощью framework Espresso, благодаря которым удалось проверить работоспособность приложения из лаб. 3. Были протестированы  
