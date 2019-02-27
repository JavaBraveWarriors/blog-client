package com.blog.controller;
import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import com.blog.model.*;
import com.blog.service.PageService;
import com.blog.service.PostService;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.blog.controller.JsonConverter.convertToJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RunWith(MockitoJUnitRunner.class)
public class PostsControllerTest {
    private static PostListWrapper POST_LIST_WRAPPER;
    private static List<ResponsePostDto> POSTS;
    private static ResponsePostDto POST;
    private static RequestPostDto REQUEST_POST;
    private static Pagination PAGINATION;
    private static List<Tag> TAGS;
    private static Tag TAG;
    private static Map<String, String> PAGE_PARAMS;
    private static Long POST_ID = 1L;
    private static Long AUTHOR_ID = 1L;
    private static Long COUNT_PAGES = 1L;
    private static Long TAG_ID = 1L;
    private static Cookie COOKIE;
    private static String SORT;
    private static String SEARCH;
    private static String LOCALE_MESSAGE;
    private MockMvc mockMvc;
    @Mock
    private MessageSource messageSource;
    @Mock
    private PostService postService;
    @Mock
    private PostDao postDao;
    @Mock
    private TagDao tagService;
    @Mock
    private PageService pageService;
    @InjectMocks
    private PostsController postsController;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postsController).build();
    }
    @BeforeClass
    public static void setData() {
        REQUEST_POST = new RequestPostDto(POST_ID, "testTitle", "test", "test", "test", AUTHOR_ID);
        TAG = new Tag(TAG_ID, "someTestText", "");
        TAGS = new ArrayList<>(Collections.singleton(TAG));
        PAGE_PARAMS = new HashMap<>();
        PAGE_PARAMS.put("search", "true");
        PAGE_PARAMS.put("menuItem", "blog");
        POST = new ResponsePostDto(POST_ID, "testTitle", "test", "test", "test", AUTHOR_ID);
        POSTS = new ArrayList<>(Collections.singleton(POST));
        POST_LIST_WRAPPER = new PostListWrapper();
        POST_LIST_WRAPPER.setPosts(POSTS);
        POST_LIST_WRAPPER.setCountPages(COUNT_PAGES);
        POST_LIST_WRAPPER.setCountPosts((long) POSTS.size());
        PAGINATION = new Pagination();
        PAGINATION.setCurrentPage(1L);
        PAGINATION.setTotalPages(COUNT_PAGES);
        PAGINATION.setSize(POST_LIST_WRAPPER.getCountPages());
        SORT = "test";
        SEARCH = "someText";
        COOKIE = new Cookie("sort", SORT);
        LOCALE_MESSAGE = "localeMessage";
    }
    @Test
    public void getPageWithPosts() throws Exception {
        given(postService.routeRequestForListPosts(
                anyLong(), anyLong(), anyString(), anyString(), anyString(), any()))
                .willReturn(POST_LIST_WRAPPER);
        given(pageService.getPagination(anyLong(), anyLong(), anyLong())).willReturn(PAGINATION);
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        mockMvc.perform(get("/blog/posts")
                .param("search", SEARCH)
                .param("sort", SORT)
                .cookie(COOKIE))
                .andExpect(status().isOk())
                .andExpect(view().name("blogPosts"))
                .andExpect(forwardedUrl("blogPosts"))
                .andExpect(model().attribute("posts", hasSize(POSTS.size())))
                .andExpect(model().attribute("pagination", is(PAGINATION)))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));
        verify(postService, times(1))
                .routeRequestForListPosts(
                        anyLong(), anyLong(), anyString(), anyString(), anyString(), any(HttpServletResponse.class));
        verify(pageService, times(1)).getPageDefaultParams();
        verify(pageService, times(1)).getPagination(anyLong(), anyLong(), anyLong());
        verifyNoMoreInteractions(postService, pageService);
    }
    @Test
    public void getPagePost() throws Exception {
        given(postDao.getPostById(anyLong())).willReturn(POST);
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        mockMvc.perform(get("/blog/posts/{id}", anyLong()))
                .andExpect(status().isOk())
                .andExpect(view().name("blogPost"))
                .andExpect(forwardedUrl("blogPost"))
                .andExpect(model().attribute("post", is(POST)))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));
        verify(postDao, times(1)).getPostById(anyLong());
        verify(pageService, times(1)).getPageDefaultParams();
        verifyNoMoreInteractions(postDao, pageService);
    }
    @Test
    public void getPageForUpdatePost() throws Exception {
        given(postDao.getPostById(anyLong())).willReturn(POST);
        given(tagService.getAllTags()).willReturn(TAGS);
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);
        mockMvc.perform(get("/blog/posts/{id}/update", POST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("formPost"))
                .andExpect(forwardedUrl("formPost"))
                .andExpect(model().attribute("post", is(POST)))
                .andExpect(model().attribute("allTags", is(TAGS)))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));
        verify(postDao, times(1)).getPostById(anyLong());
        verify(tagService, times(1)).getAllTags();
        verify(pageService, times(1)).getPageDefaultParams();
        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verifyNoMoreInteractions(postDao, messageSource, tagService, pageService);
    }
    @Test
    public void updatePost() throws Exception {
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        mockMvc.perform(post("/blog/posts/{postId}/update", POST_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(REQUEST_POST)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/blog/posts/" + POST_ID.toString()));
        verify(postDao, times(1)).updatePost(any(RequestPostDto.class));
        verifyNoMoreInteractions(postDao);
    }
    @Test
    public void getPageForAddPost() throws Exception {
        given(tagService.getAllTags()).willReturn(TAGS);
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);
        mockMvc.perform(get("/blog/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("formPost"))
                .andExpect(forwardedUrl("formPost"))
                .andExpect(model().attribute("title", is(LOCALE_MESSAGE)))
                .andExpect(model().attribute("allTags", is(TAGS)))
                .andExpect(model().attribute("page", is(PAGE_PARAMS)));
        verify(tagService, times(1)).getAllTags();
        verify(pageService, times(1)).getPageDefaultParams();
        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verifyNoMoreInteractions(messageSource, tagService, pageService);
    }
    @Test
    public void addPost() throws Exception {
        given(pageService.getPageDefaultParams()).willReturn(PAGE_PARAMS);
        given(postDao.addPost(any(RequestPostDto.class))).willReturn(POST_ID);
        mockMvc.perform(post("/blog/posts/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(REQUEST_POST)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/blog/posts/" + POST_ID.toString()));
        verify(postDao, times(1)).addPost(any(RequestPostDto.class));
        verifyNoMoreInteractions(postDao);
    }
}