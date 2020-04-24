package com.simongarton.chess.model;

import lombok.Getter;

@Getter
public enum Side {

    WHITE("White"),
    BLACK("Black");

    private String name;

    Side(String name) {
        this.name = name;
    }
}
