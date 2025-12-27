package com.mypensamiento.mypensamiento.domain.ports;

import com.mypensamiento.mypensamiento.domain.model.Comment;

public interface CommentPort {

     void save(Comment comment);

}
