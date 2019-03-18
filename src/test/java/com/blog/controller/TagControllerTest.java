package com.blog.controller;

import com.blog.dao.TagDao;
import com.blog.messaging.TagJMSProducer;
import com.blog.model.Tag;
import com.blog.service.PageService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static com.blog.controller.JsonConverter.convertToJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TagControllerTest {

    private static List<Tag> TAGS;
    private static Tag TAG;
    private static Map<String, String> PAGE_PARAMS;

    private static Long TAG_ID = 1L;
    private static String LOCALE_MESSAGE;


    private MockMvc mockMvc;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TagDao tagDao;

    @Mock
    private TagJMSProducer tagJMSProducer;

    @Mock
    private PageService pageService;

    @InjectMocks
    private TagController tagController;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @BeforeClass
    public static void setData() {
        TAG = new Tag(TAG_ID, "someTestText", "");
        TAGS = new ArrayList<>(Collections.singleton(TAG));

        PAGE_PARAMS = new HashMap<>();
        PAGE_PARAMS.put("search", "true");
        PAGE_PARAMS.put("menuItem", "blog");

        LOCALE_MESSAGE = "localeMessage";
    }

    @Test
    public void getPageWithTags() throws Exception {
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        given(tagDao.getAllTags()).willReturn(TAGS);

        mockMvc.perform(get("/blog"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("blogCategories"))
                .andExpect(forwardedUrl("blogCategories"))
                .andExpect(model().attribute("tags", is(TAGS)))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));

        verify(tagDao, times(1)).getAllTags();
        verify(pageService, times(1)).getPageDefaultParams();
        verifyNoMoreInteractions(tagDao, pageService);
    }

    @Test
    public void getModalForAddTag() throws Exception {
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);

        mockMvc.perform(get("/blog/tags/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("modals::addTag"))
                .andExpect(forwardedUrl("modals::addTag"))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));

        verify(pageService, times(1)).getPageDefaultParams();
        verifyNoMoreInteractions(pageService);
    }

    @Test
    public void getModalForUpdateTag() throws Exception {
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        given(tagDao.getTagById(anyLong())).willReturn(TAG);

        mockMvc.perform(get("/blog/tags/{id}/update", TAG_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("modals::addTag"))
                .andExpect(forwardedUrl("modals::addTag"))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)))
                .andExpect(model().attribute("tag", is(TAG)));

        verify(pageService, times(1)).getPageDefaultParams();
        verify(tagDao, times(1)).getTagById(anyLong());
        verifyNoMoreInteractions(tagDao, pageService);
    }

    @Test
    public void updateTag() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);

        mockMvc.perform(put("/blog/tags/{id}/update", TAG_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(TAG)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(model().attribute("message", is(LOCALE_MESSAGE)));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(tagDao, times(1)).updateTag(any(Tag.class));
        verifyNoMoreInteractions(tagDao, pageService);
    }

    @Test
    public void addTag() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);

        mockMvc.perform(post("/blog/tags/add", TAG_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(TAG)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(model().attribute("message", is(LOCALE_MESSAGE)));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(tagJMSProducer, times(1)).sendTagMessage(any(Tag.class));
        verifyNoMoreInteractions(tagDao, pageService);
    }

    @Test
    public void deleteTag() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);

        mockMvc.perform(delete("/blog/tags/{id}", TAG_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(forwardedUrl("modals::success"))
                .andExpect(model().attribute("message", is(LOCALE_MESSAGE)));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(tagDao, times(1)).deleteTag(anyLong());
        verifyNoMoreInteractions(tagDao, pageService);
    }
}