package com.lrpcn.quickdev.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    SUCCEED("succeed"),
    RUNNING("running"),
    WAIT("wait");

    private final String value;
}
