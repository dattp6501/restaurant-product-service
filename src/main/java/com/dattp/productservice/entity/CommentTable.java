package com.dattp.productservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dattp.productservice.dto.table.CommentTableRequestDTO;
import com.dattp.productservice.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="COMMENT_TABLE")
@Getter
@Setter
public class CommentTable implements Serializable {
    @Column(name="id") @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="star", nullable=false)
    private int star;

    @Column(name = "date_")
    private Long date;

    @Column(name="comment", columnDefinition = "LONGTEXT")
    private String comment;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="id", column=@Column(name="user_id")),
        @AttributeOverride(name="username", column=@Column(name="username"))
    })
    private User user;

    @ManyToOne
    @JoinColumn(name="table_id")
    @Lazy @JsonIgnore
    private TableE table;

    public CommentTable(){}

    public CommentTable(CommentTableRequestDTO CTR){
        copyProperties(CTR);
    }
    public void copyProperties(CommentTableRequestDTO CTR){
        BeanUtils.copyProperties(CTR, this);
        this.date = DateUtils.getCurrentMils();
        this.table = new TableE(); this.table.setId(CTR.getTableId());
    }
}