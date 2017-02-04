package com.vk.tottenham.main;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vk.tottenham.core.model.Article;

public class App
{
    public static void main( String[] args )
    {
        ApplicationContext appContext =
          new ClassPathXmlApplicationContext("com/vk/tottenham/applicationContext.xml");

        //ArticleDao articleDao = (ArticleDao)appContext.getBean("articleDao");

        /** insert **/
        Article article = new Article();
        article.setContent("content");
        article.setDescription("description");
        article.setGuid("guid");
        article.setId(1);
        article.setLink("link");
        article.setOriginalPubDate(new Date());
        article.setThumbnail("thumbnail");
        article.setTitle("title");
        //articleDao.save(article);

        System.out.println("Done");
    }
}