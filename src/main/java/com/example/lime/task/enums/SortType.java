package com.example.lime.task.enums;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SortType {
    PRIORITY("priority"),
    DUE_DATE("dueDate");

    private final String stringValue;
}
