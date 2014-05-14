package com.example.homework311nchanda;


public class ArticleData {

	 public ArticleData(String title, String content, int id,String image,String date,String link) {
         this.title = title;
         this.content = content;
         this.id = id;  
         this.image = image;
         this.date = date;
         this.link = link;
     }
     
     public ArticleData() {
		
	}

	public String title;
     public String content;
     public int id;
     public String image;
     public String date;
     public String link;
     
     public String getTitle() {return title;}
     public String getContent() {return content;}
     public int getID() {return id;}
     public String getImage() {return image;}
     public String getDate() {return date;}
     public String getLink(){return link;}
     
     public void setTitle(String title) {this.title = title;}
     public void setId(int id){this.id = id;}
     public void setContent(String content){this.content = content;}
     public void setImage(String image) {this.image = image;}   
     public void setDate(String date){this.date = date;}
     public void setLink(String link){this.link = link;}
 
}
