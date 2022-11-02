package org.toxsoft.core.tsgui.ved.api.entity;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Exact identification of a VED entity class as a {@link EVedEntityKind} / String entityClassId pair.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class VedEntityType
    implements Comparable<VedEntityType> {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<VedEntityType> KEEPER =
      new AbstractEntityKeeper<>( VedEntityType.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, VedEntityType aEntity ) {
          EVedEntityKind.KEEPER.write( aSw, aEntity.kind() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.className() );
        }

        @Override
        protected VedEntityType doRead( IStrioReader aSr ) {
          EVedEntityKind kind = EVedEntityKind.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          String className = aSr.readQuotedString();
          return new VedEntityType( kind, className );
        }
      };

  private final EVedEntityKind kind;
  private final String         className;

  /**
   * Constructor.
   *
   * @param aKind {@link EVedEntityKind} - the entity kind
   * @param aEntityClassId String - идентификатор класса VED-сущности
   */
  public VedEntityType( EVedEntityKind aKind, String aEntityClassId ) {
    kind = TsNullArgumentRtException.checkNull( aKind );
    className = StridUtils.checkValidIdPath( aEntityClassId );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает вид сущности.
   *
   * @return {@link EVedEntityKind} - вид сущности
   */
  public EVedEntityKind kind() {
    return kind;
  }

  /**
   * Возвращает идентификатор класса VED-сущности.
   *
   * @return String - идентификатор класса VED-сущности
   */
  public String className() {
    return className;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return kind.id() + '[' + className + ']';
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof VedEntityType that ) {
      return kind == that.kind && className.equals( that.className );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + kind.hashCode();
    result = TsLibUtils.PRIME * result + className.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( VedEntityType aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    int c = kind.compareTo( aThat.kind );
    if( c != 0 ) {
      return c;
    }
    return className.compareTo( aThat.className );
  }

}
