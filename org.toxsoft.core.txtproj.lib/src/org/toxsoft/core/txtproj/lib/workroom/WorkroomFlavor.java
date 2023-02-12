package org.toxsoft.core.txtproj.lib.workroom;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The flavor of the workroom.
 * <p>
 * Flavour is the paid of globally unique flavor ID and an integer workroom format version.
 *
 * @author hazard157
 */
public final class WorkroomFlavor {

  private final String flavorId;
  private final int    formatVersion;

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<WorkroomFlavor> KEEPER =
      new AbstractEntityKeeper<>( WorkroomFlavor.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, WorkroomFlavor aEntity ) {
          aSw.incNewLine();
          aSw.writeAsIs( aEntity.flavorId() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.fomatVersion() );
          aSw.decNewLine();
        }

        @Override
        protected WorkroomFlavor doRead( IStrioReader aSr ) {
          String flavorId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          int formatVersion = aSr.readInt();
          return new WorkroomFlavor( flavorId, formatVersion );
        }
      };

  /**
   * Constructor.
   *
   * @param aFlavorId String - identifier (an IDpath) of the flavor
   * @param aFormatVersion int - format version, always > 0
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException the version <= 0
   */
  public WorkroomFlavor( String aFlavorId, int aFormatVersion ) {
    flavorId = StridUtils.checkValidIdPath( aFlavorId );
    TsIllegalArgumentRtException.checkTrue( aFormatVersion <= 0 );
    formatVersion = aFormatVersion;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the flavor ID.
   *
   * @return String - identifier (an IDpath) of the flavor
   */
  public String flavorId() {
    return flavorId;
  }

  /**
   * Returns the integer number of the flavor version.
   *
   * @return int - format version, always > 0
   */
  public int fomatVersion() {
    return formatVersion;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return flavorId + '(' + formatVersion + ')';
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof WorkroomFlavor that ) {
      return this.formatVersion == that.formatVersion && this.flavorId.equals( that.flavorId );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + flavorId.hashCode();
    result = TsLibUtils.PRIME * result + formatVersion;
    return result;
  }

}
