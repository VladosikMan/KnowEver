package ru.vladgad.knowever.Essence;

public class Block {
    public String id, chapter, value;
    public int type, position;
    public Block(){

    }
    public Block(String id, String chapter, String value, int type, int position) {
        this.id = id;
        this.chapter = chapter;
        this.value = value;
        this.type = type;
        this.position = position;
    }
}
