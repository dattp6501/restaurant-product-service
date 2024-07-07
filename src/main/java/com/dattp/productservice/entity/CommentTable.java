package com.dattp.productservice.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.dattp.productservice.dto.table.CommentTableRequestDTO;
import com.dattp.productservice.utils.DateUtils;

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

    @Column(name = "create_at")
    private Long createAt;

    @Column(name = "update_at")
    private Long updateAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = this.updateAt = DateUtils.getCurrentMils();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updateAt = DateUtils.getCurrentMils();
    }

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