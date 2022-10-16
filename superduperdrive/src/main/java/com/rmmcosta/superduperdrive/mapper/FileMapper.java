package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.File;
import com.rmmcosta.superduperdrive.model.FileName;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT file_id, file_name FROM files WHERE owner_username = #{ownerUsername}")
    List<FileName> getFileNames(String ownerUsername);

    @Select("SELECT * FROM files WHERE file_id=#{fileId} and owner_username = #{ownerUsername}")
    File getFileBinary(Integer fileId, String ownerUsername);

    @Select("SELECT file_name FROM files WHERE file_id=#{fileId} and owner_username = #{ownerUsername}")
    String getFileName(Integer fileId, String ownerUsername);

    @Select("SELECT file_id, file_name FROM files WHERE file_name = #{fileName} and owner_username = #{ownerUsername}")
    FileName getFileByName(String fileName, String ownerUsername);

    @Insert("INSERT INTO files(file_name, file_binary, owner_username) VALUES(#{file.fileName}, #{file.fileBinary}, #{ownerUsername})")
    @Options(useGeneratedKeys = true, keyProperty = "file.fileId")
    int insertFile(File file, String ownerUsername);

    @Delete("DELETE FROM files WHERE file_id = #{fileId} and owner_username = #{ownerUsername}")
    boolean deleteFile(Integer fileId, String ownerUsername);
}
