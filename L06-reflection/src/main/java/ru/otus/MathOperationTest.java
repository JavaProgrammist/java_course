package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class MathOperationTest {

    @Before
    public void before() {
        System.out.println("before");
    }

    @Test
    public void plusTest() {
        int number1 = 3;
        int number2 = 5;
        assertEquals(8,number1 + number2);
    }

    @Test
    public void minusTest() {
        int number1 = 4;
        int number2 = 5;
        assertEquals(-2,number1 - number2);
    }

    @After
    public void after() {
        System.out.println("after");
    }

    private void assertEquals(int expected, int actual) {
        if (expected != actual)
            throw new RuntimeException(String.format("expected-%s, actual-%s", expected, actual));
    }
}
