package org.toxsoft.core.tsgui.chart.impl;

/**
 * Отрезок графика состояний.
 * 
 * @author vs
 */
class StateGraphSegment {

  private final int startX;
  private final int length;
  private final int stateIdx;

  StateGraphSegment( int aStartX, int aLength, int aStateIdx ) {
    startX = aStartX;
    length = aLength;
    stateIdx = aStateIdx;
  }

  int startX() {
    return startX;
  }

  int length() {
    return length;
  }

  int stateIdx() {
    return stateIdx;
  }
}
