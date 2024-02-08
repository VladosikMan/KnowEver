package ru.vladgad.knowever.Essence;

public class Book {
    public  String id,name,annotation,image,createdata,editingdate;
    public Book(){

            }
    public Book(String id, String name, String annotation, String image, String createdata, String editingdate) {
        this.id = id;
        this.name = name;
        this.annotation = annotation;
        this.image = image;
        this.createdata = createdata;
        this.editingdate = editingdate;
    }
}
