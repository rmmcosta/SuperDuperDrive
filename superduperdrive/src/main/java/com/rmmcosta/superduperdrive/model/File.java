package com.rmmcosta.superduperdrive.model;

import lombok.Data;

@Data
public class File {
    private Integer fileId;
    private String fileName;
    private Byte[] fileBinary;
}
