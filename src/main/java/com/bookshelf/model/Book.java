package com.bookshelf.model;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(generator = "SEQ_BOOK", strategy= GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_BOOK", sequenceName="SEQ_BOOK", allocationSize = 1, initialValue= 1)
    @Column(name="ID")
    private Long id;
    @Column(name="TITLE")
    private String title;
    @Column(name="AUTHOR")
    private String author;
    @Column(name="CREATED_DATE")
    private Date createdDate;
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
