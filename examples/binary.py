from ca.rules import Rule
from ca.values import Binary


class LifeRule(Rule):

    __lut__ = []
    __radius__ = 1

    def __init__(self):
        lut_size = 18 # 9 for dead and 9 for alive
        self.__lut__ = [0] * lut_size
        for i in range(lut_size):
            sum = i % 9
            alive = i > 9
            if alive:
                # 2 or 3 alive neighbors
                self.__lut__[i] = Binary.ON if (sum == 3 or sum == 2) else Binary.OFF
            else:
                # exactly 3 live neighbors
                self.__lut__[i] = Binary.ON if sum == 3 else Binary.OFF

    def toString(self):
        return "(python) Game of Life"

    def supportedCells(self):
        return Binary

    def produceValue(self, world, pos):
        x, y = pos[0], pos[1]
        # shift 8 in LUT if at living cell
        lut_i = 8 if world.getCell(x,y) else 0
        for i in range(-self.__radius__, self.__radius__+1):
            for j in range(-self.__radius__, self.__radius__+1):
                if world.getCell(x + i, y + j):
                    lut_i += 1
        return self.__lut__[lut_i]


def get_classes():
    return {
        "rules": [LifeRule],
        "cells": [Binary]
    }