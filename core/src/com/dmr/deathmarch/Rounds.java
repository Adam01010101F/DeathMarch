package com.dmr.deathmarch;

public enum Rounds {

    //ROUNDS01,ROUNDS02,ROUNDS03,ROUNDS04,ROUNDS05;

    ROUND00(0),
    ROUND01(1),
    ROUND02(2),
    ROUND03(3),
    ROUND04(4),
    ROUND05(5),
    ROUND06(6);


    private final int roundCode;

    Rounds(int roundCode) {
        this.roundCode = roundCode;
    }

    public int getRoundCode() {
        return this.roundCode;
    }

}
