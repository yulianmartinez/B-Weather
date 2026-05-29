package com.bold.core.network.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiKeyInterceptorTest {

    @Test
    fun `intercept should add apiKey query parameter to request`() {
        // Given
        val testApiKey = "test_api_key_123"
        val interceptor = ApiKeyInterceptor(testApiKey)
        
        val originalRequest = Request.Builder()
            .url("https://api.weatherapi.com/v1/current.json?q=Bogota")
            .build()
            
        val chain = mockk<Interceptor.Chain>()
        val mockResponse = mockk<Response>()
        
        val requestSlot = slot<Request>()
        
        every { chain.request() } returns originalRequest
        every { chain.proceed(capture(requestSlot)) } returns mockResponse

        // When
        val result = interceptor.intercept(chain)

        // Then
        assertEquals(mockResponse, result)
        
        val capturedRequest = requestSlot.captured
        val capturedUrl = capturedRequest.url
        
        assertEquals("test_api_key_123", capturedUrl.queryParameter("key"))
        assertEquals("Bogota", capturedUrl.queryParameter("q"))
    }
}
