package chat.server.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Message {
    String contact;
    String line;
    String[] meta;

    public Message(String contact, String line, String[] meta) {
        this.contact = contact;
        this.line = line;
        this.meta = meta;
    }

    public Message(String contact, String line, String meta) {
        this.contact = contact;
        this.line = line;
        this.meta = new String[]{meta};
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String[] getMeta() {
        return meta;
    }

    public void setMeta(String[] meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return String.format("<%s{%s}[%s]>", contact, line, String.join(";", meta));
    }
}
