package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link IAtomicValue} keeper.
 * <p>
 * <p>
 * The textual representation format depends on the atomic type:
 * <ul>
 * <li>{@link EAtomicType#NONE NONE} - is stored as <code><b>None</b></code>, the same as {@link EAtomicType#id()
 * EAtomicType.NONE.id()};</li>
 * <li>{@link EAtomicType#BOOLEAN BOOLEAN} - is stored as <code><b>true</b></code> or <code><b>false</b></code>, as
 * declared in {@link IStrioWriter#writeBoolean(boolean)};</li>
 * <li>{@link EAtomicType#INTEGER INTEGER} - is stored as <code><b>123</b></code>, as declared in
 * {@link IStrioWriter#writeInt(int)};</li>
 * <li>{@link EAtomicType#FLOATING FLOATING} - is stored as <code><b>1.2345E-67</b></code>, as declared in
 * {@link IStrioWriter#writeDouble(double)};</li>
 * <li>{@link EAtomicType#TIMESTAMP TIMESTAMP} - is stored as <code><b>YYYY-MM-DD_HH:MM:SS.mmm</b></code>, as declared
 * in {@link IStrioWriter#writeDateTime(long)};</li>
 * <li>{@link EAtomicType#STRING STRING} - is stored as <code><b>"Quoted string"</b></code>, as declared in
 * {@link IStrioWriter#writeQuotedString(String)};</li>
 * <li>{@link EAtomicType#VALOBJ VALOBJ} - is stored as "<code><b>@keeperId{kept_content}</b></code>".</li>
 * </ul>
 *
 * @author hazard157
 */
public class AtomicValueKeeper
    extends AbstractEntityKeeper<IAtomicValue> {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "av"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IAtomicValue> KEEPER = new AtomicValueKeeper();

  private AtomicValueKeeper() {
    super( IAtomicValue.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IAtomicValue aEntity ) {
    switch( aEntity.atomicType() ) {
      case NONE:
        aSw.writeAsIs( EAtomicType.NONE.id() );
        break;
      case BOOLEAN:
        aSw.writeBoolean( aEntity.asBool() );
        break;
      case TIMESTAMP:
        aSw.writeTimestamp( aEntity.asLong() );
        break;
      case INTEGER:
        aSw.writeLong( aEntity.asLong() );
        break;
      case FLOATING:
        aSw.writeDouble( aEntity.asDouble() );
        break;
      case STRING:
        aSw.writeQuotedString( aEntity.asString() );
        break;
      case VALOBJ: {
        if( aEntity != AV_VALOBJ_NULL ) {
          AvValobjImpl avImpl = (AvValobjImpl)aEntity;
          aSw.writeAsIs( avImpl.getKtor() );
        }
        else {
          aSw.writeAsIs( AvValobjNullImpl.KTOR );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected IAtomicValue doRead( IStrioReader aSr ) {
    return AtomicValueReaderUtils.readAtomicValueOrException( aSr );
  }

}
