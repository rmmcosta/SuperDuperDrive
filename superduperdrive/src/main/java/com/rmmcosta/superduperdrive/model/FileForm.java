package com.rmmcosta.superduperdrive.model;

import lombok.Data;

@Data
public class FileForm {
    private String fileName;
    private Byte[] fileBinary;
}
