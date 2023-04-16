package com.example

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ServiceTest {


    @Test
    fun test() {
        mockkStatic("khttp.KHttp")
        verify { khttp.get(any()) }
        verify(exactly = 1) { khttp.get(url = "http://google.com") }
    }
}