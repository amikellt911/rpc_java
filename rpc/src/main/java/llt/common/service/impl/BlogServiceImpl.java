package llt.common.service.impl;

import llt.common.pojo.Blog;
import llt.common.service.BlogService;

public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        System.out.println("服务端接到了客户端的请求，查询id为" + id + "的博客");
        Blog blog = Blog.builder().id(id).userId(22).title("我的第一篇博客").build();
        return blog;
    }
} 