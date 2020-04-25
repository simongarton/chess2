package com.simongarton.chess.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResult {

    private Outcome outcome;
    int moves;

}
