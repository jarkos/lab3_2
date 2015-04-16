package edu.iis.mto.staticmock;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.reflect.Whitebox.getField;
import static org.hamcrest.core.Is.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class })
public class NewsLoaderTest {

	@Test
	public void loadNews_checkNewsListForPublicContent_shouldBeOnlyOnePublicContentsOnList() throws IllegalArgumentException, IllegalAccessException 
	{
		mockStatic(NewsReaderFactory.class);
		mockStatic(ConfigurationLoader.class);
		
		SubsciptionType subTypeNone = SubsciptionType.NONE;
		SubsciptionType subTypeA = SubsciptionType.A;
		SubsciptionType subTypeB = SubsciptionType.B;
		
		IncomingInfo incomingInfo1 = mock(IncomingInfo.class);
		when(incomingInfo1.getSubscriptionType()).thenReturn(subTypeNone);
		when(incomingInfo1.requiresSubsciption()).thenReturn(false);
		
		IncomingInfo incomingInfo2 = mock(IncomingInfo.class);
		when(incomingInfo2.getSubscriptionType()).thenReturn(subTypeA);
		when(incomingInfo2.requiresSubsciption()).thenReturn(true);
		
		IncomingInfo incomingInfo3 = mock(IncomingInfo.class);
		when(incomingInfo3.getSubscriptionType()).thenReturn(subTypeB);
		when(incomingInfo3.requiresSubsciption()).thenReturn(true);
		
		IncomingNews incomingNews = new IncomingNews();
		incomingNews.add(incomingInfo1);
		incomingNews.add(incomingInfo2);
		incomingNews.add(incomingInfo3);
		
		Configuration configuration = mock(Configuration.class);

		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(configurationLoader.loadConfiguration()).thenReturn(configuration);

		FileNewsReader fileNewsReader = mock(FileNewsReader.class);
		when(fileNewsReader.read()).thenReturn(incomingNews);
		
		NewsReader reader = mock(NewsReader.class);
		NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(reader);
		when(reader.read()).thenReturn(incomingNews);

		NewsLoader newsLoader = new NewsLoader();
		PublishableNews publishableNews = newsLoader.loadNews();
		
		Field field = getField(PublishableNews.class,"publicContent");
		List<String> publicContentList = (List<String>)field.get(publishableNews);
		int result = publicContentList.size();

		assertThat(result,is(1));		
	}
	
	@Test
	public void loadNews_checkNewsListForSubsrcibedContent_shouldBeTwoContentsOnList() throws IllegalArgumentException, IllegalAccessException 
	{
		mockStatic(NewsReaderFactory.class);
		mockStatic(ConfigurationLoader.class);
		
		SubsciptionType subTypeNone = SubsciptionType.NONE;
		SubsciptionType subTypeA = SubsciptionType.A;
		SubsciptionType subTypeB = SubsciptionType.B;
		
		IncomingInfo incomingInfo1 = mock(IncomingInfo.class);
		when(incomingInfo1.getSubscriptionType()).thenReturn(subTypeNone);
		when(incomingInfo1.requiresSubsciption()).thenReturn(false);
		
		IncomingInfo incomingInfo2 = mock(IncomingInfo.class);
		when(incomingInfo2.getSubscriptionType()).thenReturn(subTypeA);
		when(incomingInfo2.requiresSubsciption()).thenReturn(true);
		
		IncomingInfo incomingInfo3 = mock(IncomingInfo.class);
		when(incomingInfo3.getSubscriptionType()).thenReturn(subTypeB);
		when(incomingInfo3.requiresSubsciption()).thenReturn(true);
		
		IncomingNews incomingNews = new IncomingNews();
		incomingNews.add(incomingInfo1);
		incomingNews.add(incomingInfo2);
		incomingNews.add(incomingInfo3);
		
		Configuration configuration = mock(Configuration.class);

		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(configurationLoader.loadConfiguration()).thenReturn(configuration);

		FileNewsReader fileNewsReader = mock(FileNewsReader.class);
		when(fileNewsReader.read()).thenReturn(incomingNews);
		
		NewsReader reader = mock(NewsReader.class);
		NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(reader);
		when(reader.read()).thenReturn(incomingNews);

		NewsLoader newsLoader = new NewsLoader();
		PublishableNews publishableNews = newsLoader.loadNews();
		
		Field field = getField(PublishableNews.class,"subscribentContent");
		List<String> subscribentContentList = (List<String>)field.get(publishableNews);
		int result = subscribentContentList.size();

		assertThat(result,is(2));		
	}
	
}
