package ru.vladgad.knowever.Essence;

public class Test {
    public String id,chapter_id,ask,quen;
    public int type, position;
    public  Test(){

    }
    public Test(String id, String chapter_id, String ask, String quen, int type, int position) {
        this.id = id;
        this.chapter_id = chapter_id;
        this.ask = ask;
        this.quen = quen;
        this.type = type;
        this.position = position;
    }
}
