package org.toxsoft.tslib.utils.misc;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * This is an exmple of final immutable object.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "boxing" } )
public class FooFinalImmutableObject {

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "FooFinalImmutableObject"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<FooFinalImmutableObject> KEEPER =
      new AbstractEntityKeeper<>( FooFinalImmutableObject.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, FooFinalImmutableObject aEntity ) {
          aSw.writeBoolean( aEntity.flag );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.counter );
          aSw.writeSeparatorChar();
          aSw.writeLong( aEntity.timestamp );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.value );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.text );
        }

        @Override
        protected FooFinalImmutableObject doRead( IStrioReader aSr ) {
          boolean flag = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          int counter = aSr.readInt();
          aSr.ensureSeparatorChar();
          long time = aSr.readLong();
          aSr.ensureSeparatorChar();
          double val = aSr.readDouble();
          aSr.ensureSeparatorChar();
          String txt = aSr.readQuotedString();
          return new FooFinalImmutableObject( flag, counter, time, val, txt );
        }

      };

  private final boolean flag;
  private final int     counter;
  private final long    timestamp;
  private final double  value;
  private final String  text;

  public FooFinalImmutableObject( boolean aFlag, int aCounter, long aTimestamp, double aValue, String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    flag = aFlag;
    counter = aCounter;
    timestamp = aTimestamp;
    value = aValue;
    text = aText;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public boolean isFlag() {
    return flag;
  }

  public int getCounter() {
    return counter;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public double getValue() {
    return value;
  }

  public String getText() {
    return text;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder( "FooFinalImmutableObject(" ); //$NON-NLS-1$
    sb.append( flag );
    sb.append( ',' );
    sb.append( counter );
    sb.append( ',' );
    sb.append( String.format( "%1$tF %1$tT", timestamp ) ); //$NON-NLS-1$
    sb.append( ',' );
    sb.append( value );
    sb.append( ',' );
    sb.append( text );
    sb.append( ')' );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof FooFinalImmutableObject ) {
      FooFinalImmutableObject that = (FooFinalImmutableObject)aThat;
      return this.flag == that.flag && //
          this.counter == that.counter && //
          this.timestamp == that.timestamp && //
          (Double.compare( this.timestamp, that.timestamp ) == 0) && //
          this.text.equals( that.text );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (flag ? 1 : 0);
    result = TsLibUtils.PRIME * result + counter;
    result = TsLibUtils.PRIME * result + (int)(timestamp ^ (timestamp >>> 32));
    long dblval = Double.doubleToRawLongBits( value );
    result = TsLibUtils.PRIME * result + (int)(dblval ^ (dblval >>> 32));
    result = TsLibUtils.PRIME * result + text.hashCode();
    return result;
  }

}
