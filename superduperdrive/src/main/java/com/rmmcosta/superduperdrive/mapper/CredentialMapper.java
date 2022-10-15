package com.rmmcosta.superduperdrive.mapper;

import com.rmmcosta.superduperdrive.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM credentials")
    List<Credential> getCredentials();

    @Select("SELECT * FROM credentials WHERE credential_id=#{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Select("SELECT * FROM credentials WHERE url=#{url} and username=#{username}")
    Credential getCredentialByUrlAndUsername(String url, String username);

    @Insert("INSERT INTO credentials(url, username, password) VALUES(#{url}, #{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Delete("DELETE FROM credentials WHERE credential_id = #{credentialId}")
    boolean deleteCredential(Integer credentialId);

    @Update("UPDATE credentials SET url = #{url}, username = #{username}, password = #{password} WHERE credential_id = #{credentialId}")
    boolean updateCredential(Credential credential);
}
