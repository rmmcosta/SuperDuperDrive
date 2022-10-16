package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM credentials WHERE owner_username = #{ownerUsername}")
    List<Credential> getCredentials(String ownerUsername);

    @Select("SELECT * FROM credentials WHERE credential_id=#{credentialId} and owner_username = #{ownerUsername}")
    Credential getCredentialById(Integer credentialId, String ownerUsername);

    @Select("SELECT * FROM credentials WHERE url=#{url} and username=#{username} and owner_username = #{ownerUsername}")
    Credential getCredentialByUrlAndUsername(String url, String username, String ownerUsername);

    @Insert("INSERT INTO credentials(url, username, password, owner_username) VALUES(#{credential.url}, #{credential.username}, #{credential.password}, #{ownerUsername})")
    @Options(useGeneratedKeys = true, keyProperty = "credential.credentialId")
    int insertCredential(Credential credential, String ownerUsername);

    @Delete("DELETE FROM credentials WHERE credential_id = #{credentialId} and owner_username = #{ownerUsername}")
    boolean deleteCredential(Integer credentialId, String ownerUsername);

    @Update("UPDATE credentials SET url = #{credential.url}, username = #{credential.username}, password = #{credential.password} WHERE credential_id = #{credential.credentialId} and owner_username = #{ownerUsername}")
    boolean updateCredential(Credential credential, String ownerUsername);
}
