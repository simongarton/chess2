package com.simongarton.chess.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActualMove {
    public String from;
    public String to;
}
