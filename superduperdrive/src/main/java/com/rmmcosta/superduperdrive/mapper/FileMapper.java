package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.File;
import com.rmmcosta.superduperdrive.model.FileName;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT file_id, file_name FROM files")
    List<FileName> getFileNames();

    @Select("SELECT * FROM files WHERE file_id=#{fileId}")
    File getFileBinary(Integer fileId);

    @Select("SELECT file_name FROM files WHERE file_id=#{fileId}")
    String getFileName(Integer fileId);

    @Insert("INSERT INTO files(file_name, file_binary) VALUES(#{fileName}, #{fileBinary})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM files WHERE file_id = #{fileId}")
    boolean deleteFile(Integer fileId);
}
