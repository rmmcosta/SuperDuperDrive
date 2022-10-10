package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT file_name FROM files")
    List<String> getFileNames();

    @Select("SELECT file_binary FROM files WHERE file_id=#{fileId}")
    Byte[] getFileBinary(Integer fileId);

    @Insert("INSERT INTO files(file_name, file_binary) VALUES(#{fileName}, #{fileBinary})")
    @Options(useGeneratedKeys = true, keyProperty = "file_id")
    int insertFile(File file);

    @Delete("DELETE FROM files WHERE file_id = #{fileId}")
    boolean deleteFile(Integer fileId);
}
