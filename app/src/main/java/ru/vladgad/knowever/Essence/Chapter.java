package ru.vladgad.knowever.Essence;

public class Chapter {
    public String id,name,annotation,book,createdata,editingdata;
    public int position;
    public Chapter(){

    }
    public Chapter(String id, String name, String annotation, String book, String createdata, String editingdata, int position) {
        this.id = id;
        this.name = name;
        this.annotation = annotation;
        this.book = book;
        this.createdata = createdata;
        this.editingdata = editingdata;
        this.position = position;
    }
}
