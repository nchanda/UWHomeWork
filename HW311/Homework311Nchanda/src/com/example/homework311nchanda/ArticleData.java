package com.example.homework311nchanda;


public class ArticleData {

	 public ArticleData(String title, String content, int id) {
         this.title = title;
         this.content = content;
         this.id = id;            
     }
     
     public String title;
     public String content;
     public int id;
     public String type;
     
     public String getTitle() {return title;}
     public String getContent() {return content;}
     public int getID() {return id;}
     
     public void setTitle(String title) {this.title = title;}
     public void setId(int id){this.id = id;}
     public void setContent(String content){this.content = content;}
     
 
}
