package com.udacity.jwdnd.course1.cloudstorage.repository;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> findFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData)" +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(Integer fileId);
}
