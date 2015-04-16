package edu.iis.mto.staticmock;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
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
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class })
public class NewsLoaderTest {

	IncomingNews incomingNews;
	Configuration configuration;
	PublishableNews publishableNews;
	NewsReaderFactory newsReaderFactory;
	
	@Before
	public void setUp() throws Exception 
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
		
		incomingNews = new IncomingNews();
		incomingNews.add(incomingInfo1);
		incomingNews.add(incomingInfo2);
		incomingNews.add(incomingInfo3);
		
		configuration = mock(Configuration.class);
		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(configurationLoader.loadConfiguration()).thenReturn(configuration);
		
		NewsReader reader = mock(NewsReader.class);
		newsReaderFactory = mock(NewsReaderFactory.class);
		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(reader);
		when(reader.read()).thenReturn(incomingNews);
		
		NewsLoader newsLoader = new NewsLoader();
		publishableNews = newsLoader.loadNews();
		
	}
	
	@Test
	public void loadNews_checkNewsListForPublicContent_shouldBeOnlyOnePublicContentsOnList() throws IllegalArgumentException, IllegalAccessException 
	{
		
		Field field = getField(PublishableNews.class,"publicContent");
		List<String> publicContentList = (List<String>)field.get(publishableNews);
		int result = publicContentList.size();

		assertThat(result,is(1));		
	}
	
	@Test
	public void loadNews_checkNewsListForSubsrcibedContent_shouldBeTwoContentsOnList() throws IllegalArgumentException, IllegalAccessException 
	{			
		Field field = getField(PublishableNews.class,"subscribentContent");
		List<String> subscribentContentList = (List<String>)field.get(publishableNews);
		int result = subscribentContentList.size();

		assertThat(result,is(2));		
	}
	
	@Test
	public void loadNews_checkCallOfgetReaderMethod_shouldBeCalledOnce() throws IllegalArgumentException, IllegalAccessException 
	{			
		Field field = getField(PublishableNews.class,"subscribentContent");
		List<String> subscribentContentList = (List<String>)field.get(publishableNews);
		int result = subscribentContentList.size();

		Mockito.verify( newsReaderFactory, times(1)).getReader(Mockito.anyString());		
	}
	
}
