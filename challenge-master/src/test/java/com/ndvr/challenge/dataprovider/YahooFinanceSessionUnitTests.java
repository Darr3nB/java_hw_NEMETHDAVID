package com.ndvr.challenge.dataprovider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class YahooFinanceSessionUnitTests {

	@InjectMocks
    private YahooFinanceSession session;

	@Mock
	private HttpHandler httpHandler;

	@Test
	void testInvalidate() {
		CookieStore cookieStore = new BasicCookieStore();
		doReturn(cookieStore).when(httpHandler).getCookieStore();
		session.invalidate();
		assertNull(session.getCrumb());
	}

	@Test
	void testAcquireCrumbWithTicker() throws UnsupportedOperationException, IOException {
		String ticker = "LOGM";
		String crumb = "5h8F6Ab9PIU";
		String input = "SomeLine\nCrumbStore\":{\"crumb\":\"" + crumb + "\"}";

		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);

        doReturn(httpResponse).when(httpHandler).fetchResponse(any());

		InputStream stream = new ByteArrayInputStream(input.getBytes());

		doReturn(entity).when(httpResponse).getEntity();
		doReturn(stream).when(entity).getContent();

		session.acquireCrumbWithTicker(ticker);

		assertEquals(session.getCrumb(), crumb);

		// Already acquired
		session.acquireCrumbWithTicker(ticker);
	}

	@Test
	void testAcquireCrumbWithTickerNotFound() throws UnsupportedOperationException, IOException {
		String ticker = "LOGM";
		String input = "SomeLine\nSomeOtherLine";

		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);

        doReturn(httpResponse).when(httpHandler).fetchResponse(any());

		InputStream stream = new ByteArrayInputStream(input.getBytes());

		doReturn(entity).when(httpResponse).getEntity();
		doReturn(stream).when(entity).getContent();

		session.acquireCrumbWithTicker(ticker);

		assertNull(session.getCrumb());
	}

	@Test
	void testAcquireCrumbWithTickerIOException() throws IOException {
		String ticker = "LOGM";

		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity entity = mock(HttpEntity.class);

        doReturn(httpResponse).when(httpHandler).fetchResponse(any());

		doReturn(entity).when(httpResponse).getEntity();
		doThrow(IOException.class).when(entity).getContent();

		session.acquireCrumbWithTicker(ticker);

		assertNull(session.getCrumb());
	}

}
